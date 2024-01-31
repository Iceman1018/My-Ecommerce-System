package com.example.trade.web.portal.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CartPersistentSender {
    @Autowired
    private AmqpTemplate amqpTemplate;
    public void sendCreateCartItemMessage(String message){
        log.info("Send message to Create Cart Item Queue：{}",message);
        amqpTemplate.convertAndSend("shopping-cart-event-exchange","to.create.cart.item",message);
    }

    public void sendUpdateCartItemMessage(String message){
        log.info("Send message to Update Cart Item Queue：{}",message);
        amqpTemplate.convertAndSend("shopping-cart-event-exchange","to.update.cart.item",message);
    }

    public void sendDeleteCartItemMessage(String message){
        log.info("Send message to Delete Cart Item Queue：{}",message);
        amqpTemplate.convertAndSend("shopping-cart-event-exchange","to.delete.cart.item",message);
    }
}
