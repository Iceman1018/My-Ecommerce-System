package com.example.trade.lightningdeal.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DealOrderMessageSender {
    @Autowired
    private AmqpTemplate amqpTemplate;
    public void sendCreateDealOrderMessage(String message){
        log.info("Sending order creation message: {}",message);
        amqpTemplate.convertAndSend("order-event-exchange","to.create.order",message);
    }
}
