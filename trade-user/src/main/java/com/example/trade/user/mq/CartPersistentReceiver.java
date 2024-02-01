package com.example.trade.user.mq;

import com.alibaba.fastjson.JSON;
import com.example.trade.common.utils.RedisWorker;
import com.example.trade.user.Service.CartItemService;
import com.example.trade.user.db.model.CartItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ComponentScan("com.example.trade.common")
@Slf4j
public class CartPersistentReceiver {
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private RedisWorker redisWorker;

    @RabbitListener(queues = "create.cart.item.queue")
    public void createCartItem(String msg){
        log.info("create Cart Item Message consuming, got a message: {}",msg);
        CartItem cartItem= JSON.parseObject(msg, CartItem.class);
        cartItemService.addCartItem(cartItem);
    }

    @RabbitListener(queues = "update.cart.item.queue")
    public void updateCartItem(String msg){
        log.info("update Cart Item Message consuming, got a message: {}",msg);
        String[] pair=msg.split(",");
        cartItemService.updateCartItemNum(Long.valueOf(pair[0]),Integer.valueOf(pair[1]));
    }

    @RabbitListener(queues = "delete.cart.item.queue")
    public void deleteCartItem(String msg){
        log.info("Delete Cart Item Message consuming, got a message: {}",msg);
        cartItemService.deleteCartItem(Long.valueOf(msg));
    }

    @RabbitListener(queues = "invalid.cart.item.goods.queue")
    public void invalidCartItemGoods(String msg){
        log.info("Invalid Cart Item Message consuming, got a message: {}",msg);
        cartItemService.itemInvalidationByGoodsId(Long.valueOf(msg));
        List<CartItem> list=cartItemService.findByGoodsId(Long.valueOf(msg));
        for(CartItem cartItem:list)
            redisWorker.addItemToSet("invalid_cart_item",String.valueOf(cartItem.getId()));
    }

    @RabbitListener(queues = "invalid.cart.item.deal.queue")
    public void invalidCartItemDeal(String msg){
        log.info("Invalid Cart Item Message consuming, got a message: {}",msg);
        boolean updated=cartItemService.itemInvalidationByDealId(Long.valueOf(msg));
        if(updated)
            log.info("update items related to expired deal");
        List<CartItem> list=cartItemService.findByDealId(Long.valueOf(msg));
        for(CartItem cartItem:list) {
            log.info("create item invalid redis message,item id:{}",cartItem.getId());
            redisWorker.addItemToSet("invalid_cart_item", String.valueOf(cartItem.getId()));
        }
    }
}
