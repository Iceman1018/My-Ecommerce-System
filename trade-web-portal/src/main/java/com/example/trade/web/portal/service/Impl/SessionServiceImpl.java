package com.example.trade.web.portal.service.Impl;
import com.alibaba.fastjson.JSON;
import com.example.trade.common.model.CartItem;
import com.example.trade.common.utils.RedisWorker;
import com.example.trade.common.utils.SnowflakeIdWorker;
import com.example.trade.web.portal.mq.CartPersistentSender;
import com.example.trade.web.portal.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class SessionServiceImpl implements SessionService {
    @Autowired
    private RedisWorker redisWorker;
    @Autowired
    private CartPersistentSender cartPersistentSender;

    private SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker(1,1);
    public void getCartItem(HttpSession session){
        Map<Long, CartItem> map=(Map<Long,CartItem>)session.getAttribute("userCart");
        for(Map.Entry<Long,CartItem> entry:map.entrySet()){
            CartItem cartItem=entry.getValue();
            log.info("cart id:{}",String.valueOf(cartItem.getId()));
            if(redisWorker.isItemInSet("invalid_cart_item",String.valueOf(cartItem.getId()))) {
                cartItem.setStatus(0);
                log.info("deleted data inconsistency, update session data");
                redisWorker.removeItemFromSet("invalid_cart_item",String.valueOf(cartItem.getId()));
            }
        }
        session.setAttribute("userCart",map);
    }
    public void createCartItem(HttpSession session, CartItem cartItem){
        log.info("got the carItem OBJ:{}", JSON.toJSONString(cartItem));
        Map<Long, CartItem> map=(Map<Long,CartItem>)session.getAttribute("userCart");
        for(Map.Entry<Long,CartItem> entry:map.entrySet()){
            CartItem c=entry.getValue();
            if(c.getGoodsId().equals(cartItem.getGoodsId())&&c.getDealId().equals(cartItem.getDealId())) {
                log.info("合并新物品{},更新前num:{},更新后num:{}",c.getId(),c.getGoodsNum(),c.getGoodsNum() + cartItem.getGoodsNum());
                updateCartItem(session, c.getId(), c.getGoodsNum() + cartItem.getGoodsNum());
                return;
            }
        }
        cartItem.setId(snowflakeIdWorker.nextId());
        cartItem.setUserId((Long)session.getAttribute("userId"));
        cartItem.setCreateTime(new Date());
        cartItem.setStatus(1);
        map.put(cartItem.getId(),cartItem);
        session.setAttribute("userCart",map);
        cartPersistentSender.sendCreateCartItemMessage(JSON.toJSONString(cartItem));
    }
    public void updateCartItem(HttpSession session, Long cartItemId,Integer itemNum){
        Map<Long,CartItem> map=(Map<Long,CartItem>)session.getAttribute("userCart");
        if(itemNum<=0){
            deleteCartItem(session,cartItemId);
            return;
        }
        map.get(cartItemId).setGoodsNum(itemNum);
        session.setAttribute("userCart",map);
        cartPersistentSender.sendUpdateCartItemMessage(cartItemId+","+itemNum);
    }
    public void deleteCartItem(HttpSession session, Long cartItemId){
        Map<Long,CartItem> map=(Map<Long,CartItem>)session.getAttribute("userCart");
        map.remove(cartItemId);
        session.setAttribute("userCart",map);
        redisWorker.removeItemFromSet("invalid_cart_item",String.valueOf(cartItemId));
        cartPersistentSender.sendDeleteCartItemMessage(String.valueOf(cartItemId));
    }
}
