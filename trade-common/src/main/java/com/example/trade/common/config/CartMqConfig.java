package com.example.trade.common.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CartMqConfig {
    @Bean
    public Exchange CartEventExchange() {
        return new TopicExchange("shopping-cart-event-exchange", true, false);
    }

    @Bean
    public Queue createCartItemQueue(){
        Queue queue=new Queue("create.cart.item.queue",true,false,false);
        return queue;
    }

    @Bean
    public Queue updateCartItemQueue(){
        Queue queue=new Queue("update.cart.item.queue",true,false,false);
        return queue;
    }

    @Bean
    public Queue deleteCartItemQueue(){
        Queue queue=new Queue("delete.cart.item.queue",true,false,false);
        return queue;
    }

    @Bean
    public Queue invalidCartItemByGoodsIdQueue(){
        Queue queue=new Queue("invalid.cart.item.goods.queue",true,false,false);
        return queue;
    }

    @Bean
    public Queue invalidCartItemByDealIdQueue(){
        Queue queue=new Queue("invalid.cart.item.deal.queue",true,false,false);
        return queue;
    }



    @Bean
    public Binding createCartItemBinding() {
        return new Binding("create.cart.item.queue",
                Binding.DestinationType.QUEUE,
                "shopping-cart-event-exchange",
                "to.create.cart.item",
                null);
    }

    @Bean
    public Binding updateCartItemBinding() {
        return new Binding("update.cart.item.queue",
                Binding.DestinationType.QUEUE,
                "shopping-cart-event-exchange",
                "to.update.cart.item",
                null);
    }

    @Bean
    public Binding deleteCartItemBinding() {
        return new Binding("delete.cart.item.queue",
                Binding.DestinationType.QUEUE,
                "shopping-cart-event-exchange",
                "to.delete.cart.item",
                null);
    }

    @Bean
    public Binding invalidCartItemGoodsBinding() {
        return new Binding("invalid.cart.item.goods.queue",
                Binding.DestinationType.QUEUE,
                "shopping-cart-event-exchange",
                "to.invalid.cart.item.goods",
                null);
    }

    @Bean
    public Binding invalidCartItemDealBinding() {
        return new Binding("invalid.cart.item.deal.queue",
                Binding.DestinationType.QUEUE,
                "shopping-cart-event-exchange",
                "to.invalid.cart.item.deal",
                null);
    }


}
