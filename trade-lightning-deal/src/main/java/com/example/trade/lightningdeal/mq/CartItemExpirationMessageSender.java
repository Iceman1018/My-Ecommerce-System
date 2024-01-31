package com.example.trade.lightningdeal.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CartItemExpirationMessageSender {
    @Autowired
    private AmqpTemplate amqpTemplate;
    public void sendCartItemExpirationMessage(String message){
        log.info("Sending cart item expiration message: {}",message);
        amqpTemplate.convertAndSend("shopping-cart-event-exchange","to.invalid.cart.item.deal",message);
    }
}
