package com.example.trade.order.Service.impl;

import com.alibaba.fastjson.JSON;
import com.example.trade.goods.db.dao.GoodsDao;
import com.example.trade.goods.db.model.Goods;
import com.example.trade.goods.service.GoodsService;
import com.example.trade.order.Service.OrderService;
import com.example.trade.order.db.dao.OrderDao;
import com.example.trade.order.db.model.Order;
import com.example.trade.order.mq.OrderMessageSender;
import com.example.trade.order.utils.SnowflakeIdWorker;
import com.example.trade.user.Service.UserService;
import com.example.trade.user.db.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    GoodsService goodsService;

    @Autowired
    UserService userService;

    @Autowired
    private OrderMessageSender orderMessageSender;

    private SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker(6,8);
    @Override
    public Order createOrder(long userId, long goodsId){
        User user=userService.queryUser(userId);
        Goods goods=goodsService.queryGoodsById(goodsId);
        if(user==null){
            log.error("user is null userId={}",userId);
            throw new RuntimeException("user does not exist");
        }
        if(goods==null){
            log.error("goods is null goodsId={}",goodsId);
            throw new RuntimeException("goods does not exist");
        }
        if(goods.getAvailableStock()<=0){
            log.error("goods stock not enough, goodsId={},userId={}",goodsId,userId);
            throw new RuntimeException("goods stock not enough");
        }
        boolean lockResult=goodsService.lockStock(goodsId);
        if(!lockResult){
            log.error("order lock stock error goods={}",JSON.toJSONString(goods));
            throw new RuntimeException("order stock lock failed");
        }

        Order order=new Order();
        order.setId(snowflakeIdWorker.nextId());
        order.setActivityId(0L);
        order.setActivityType(0);
        order.setGoodsId(goodsId);
        order.setUserId(userId);
        order.setStatus(1);
        order.setCreateTime(new Date());
        order.setPayPrice(goods.getPrice());
//        boolean res=orderDao.insertOrder(order);
//        if(!res){
//            log.error("order insert error order={}", JSON.toJSONString(order));
//            throw new RuntimeException("order creating failed");
//        }
//        //send delay message
//        orderMessageSender.sendPayStatusCheckDelayMessage(JSON.toJSONString(order));
        orderMessageSender.sendCreateOrderMessage(JSON.toJSONString(order));
        return order;
    }
    @Override
    public Order queryOrder(long OrderId) {
        return orderDao.queryOrderById(OrderId);
    }

    @Override
    public List<Order> queryOrderListByUserId(long userId){
        return orderDao.queryOrderListByUserId(userId);
    }
    @Override
    public void payOrder(long orderId){
        log.info("Pay for the order: {}",orderId);
        Order order=orderDao.queryOrderById(orderId);
        if(order==null){
            log.error("orderId={} order didn't exist",orderId );
            throw new RuntimeException("Order didn't exist");
        }
        if(order.getStatus()==99){
            log.error("orderId={} order cannot be payed",orderId);
            throw new RuntimeException("Order has been canceled");
        }else if(order.getStatus()==2){
            log.error("orderId={} order cannot be payed",orderId);
            throw new RuntimeException("Order has been finished");
        }
        log.info("payment simulation");
        order.setPayTime(new Date());
        order.setStatus(2);
        boolean updateResult=orderDao.updateOrder(order);
        if(!updateResult){
            log.error("orderId={} payment status update failed",orderId);
            throw new RuntimeException("payment status update failed");
        }
        boolean deductResult= goodsService.deductStock(order.getGoodsId());
        if(!deductResult){
            log.error("orderId={} deduct stock failed",orderId);
            throw new RuntimeException("deduct stock failed");
        }
        orderDao.updateOrder(order);
        if(order.getActivityType()==1)
            orderMessageSender.sendSecPaySuccessMessage(JSON.toJSONString(order));
    }
}