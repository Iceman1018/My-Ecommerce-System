package com.example.trade.order.mq;

import com.alibaba.fastjson.JSON;
import com.example.trade.order.db.dao.OrderRepository;
import com.example.trade.order.db.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateOrderReceiver {
    @Autowired
    private OrderRepository orderDao;
    @Autowired
    private OrderMessageSender orderMessageSender;

    /**
     * 创建订单消息处理
     * @param message
     */
    @RabbitListener(queues = "create.order.queue")
    public void process(String message){
        log.info("Order Creation Message consuming, got a message: {}",message);
        Order order=JSON.parseObject(message,Order.class);
        orderDao.save(order);
        //send delay message
        orderMessageSender.sendPayStatusCheckDelayMessage(JSON.toJSONString(order));
    }
}
