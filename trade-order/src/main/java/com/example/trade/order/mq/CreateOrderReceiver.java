package com.example.trade.order.mq;

import com.alibaba.fastjson.JSON;
import com.example.trade.goods.service.GoodsService;
import com.example.trade.order.Service.LimitBuyService;
import com.example.trade.order.db.dao.OrderDao;
import com.example.trade.order.db.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateOrderReceiver {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderMessageSender orderMessageSender;
    @Autowired
    private LimitBuyService limitBuyService;

    /**
     * 创建订单消息处理
     * @param message
     */
    @RabbitListener(queues = "create.order.queue")
    public void process(String message){
        log.info("Order Creation Message consuming, got a message: {}",message);
        Order order=JSON.parseObject(message,Order.class);
        boolean res=orderDao.insertOrder(order);
        if(!res){
            log.error("order insert error order={}", JSON.toJSONString(order));
            throw new RuntimeException("order creating failed");
        }
        //send delay message
        orderMessageSender.sendPayStatusCheckDelayMessage(JSON.toJSONString(order));

        if(order.getActivityType()==1){
            limitBuyService.addLimitMemeber(order.getActivityId(),order.getUserId());
        }
    }
}
