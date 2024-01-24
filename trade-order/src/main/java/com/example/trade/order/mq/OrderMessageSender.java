package com.example.trade.order.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderMessageSender {
    @Autowired
    private AmqpTemplate amqpTemplate;
    public void sendPayStatusCheckDelayMessage(String message){
        log.info("Send order message to delay queue：{}",message);
        amqpTemplate.convertAndSend("order-event-exchange","order.create",message);
    }

    public void sendSecPaySuccessMessage(String message){
        log.info("Send paying success message:{}",message);
        amqpTemplate.convertAndSend("order-event-exchange","deal.order.pay.success",message);
    }

    public void sendRevertDealOrderMessage(String message){
        log.info("Deal order revert message:{}",message);
        amqpTemplate.convertAndSend("order-event-exchange","deal.order.revert",message);
    }

    public void sendCreateOrderMessage(String message){
        log.info("Sending order creation message: {}",message);
        amqpTemplate.convertAndSend("order-event-exchange","to.create.order",message);
    }
}
