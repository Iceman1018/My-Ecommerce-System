package com.example.trade.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * 消息队列配置类
 */

@Configuration
public class RabbitMqConfig {

    /**
     * 订单相关事件，交换机
     * TopicExchange
     *
     * @return
     */
    @Bean
    public Exchange orderEventExchange() {
        /*
         *   String name,
         *   boolean durable,
         *   boolean autoDelete,
         *   Map<String, Object> arguments
         * */

        return new TopicExchange("order-event-exchange", true, false);
    }

    /**
     * 创建订单队列（普通队列）
     *
     * @return
     */
    @Bean
    public Queue createOrderQueue(){
        Queue queue=new Queue("create.order.queue",true,false,false);
        return queue;
    }
    /**
     * 订单状态校验，绑定
     * 订单状态校验队列 绑定交换机
     * @return
     */
    @Bean
    public Binding createOrderBinding(){
        return new Binding("create.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "to.create.order",
                null);
    }


    /**
     * 订单延迟队列（死信队列）
     *
     * @return
     */
    @Bean
    public Queue orderDelayQueue() {

     /*
            Queue(String name,  队列名字
            boolean durable,  是否持久化
            boolean exclusive,  是否排他
            boolean autoDelete, 是否自动删除
            Map<String, Object> arguments) 属性
         */
        HashMap<String, Object> arguments = new HashMap<>();
        //到期后转发的交换机
        arguments.put("x-dead-letter-exchange", "order-event-exchange");
        //到期后转发的路由key
        arguments.put("x-dead-letter-routing-key", "order.pay.status.check");
        //消息过期时间 1分钟
        arguments.put("x-message-ttl", 60000);

        return new Queue("order.delay.queue", true, false, false, arguments);
    }

    /**
     * 实际超时队列,用来订单支付状态检查 (普通队列)
     *
     * @return
     */
    @Bean
    public Queue orderReleaseQueue() {

        Queue queue = new Queue("order.pay.status.check.queue", true, false, false);
        return queue;
    }

    /**
     * 秒杀活动超时队列，用来检查秒杀活动的支付情况
     * @return
     */
    @Bean
    public Queue DealReleaseQueue(){
        Queue queue = new Queue("deal.order.revert.queue", true, false, false);
        return queue;
    }

    /**
     * 用于deal与order通信，获得order支付成功消息
     *
     * @return
     */
    @Bean
    public Queue dealPaySuccessQueue(){
        Queue queue = new Queue("deal.order.pay.success.queue",true,false,false);
        return queue;
    }


    @Bean
    public Queue DealtoCacheQueue() {
        Queue queue = new Queue("deal.to.cache.queue", true, false, false);
        return queue;
    }

    @Bean
    public Queue lockGoodsStockQueue(){
        Queue queue = new Queue("lock.goods.stock.queue",true,false,false);
        return queue;
    }


    @Bean
    public Queue revertGoodsStockQueue() {
        Queue queue = new Queue("revert.goods.stock.queue", true, false, false);
        return queue;
    }


    /**
     * 创建订单成功消息，绑定
     * 订单延迟队列 绑定到交换机
     *
     * @return
     */
    @Bean
    public Binding orderCreateBinding() {
        /*
         * String destination, 目的地（队列名或者交换机名字）
         * DestinationType destinationType, 目的地类型（Queue、Exhcange）
         * String exchange,
         * String routingKey,
         * Map<String, Object> arguments
         * */
        return new Binding("order.delay.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.create",  // 路由key一般为事件名
                null);
    }

    /**
     * 订单状态校验，绑定
     * 订单状态校验队列 绑定到交换机
     *
     * @return
     */
    @Bean
    public Binding orderReleaseBinding() {
        return new Binding("order.pay.status.check.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.pay.status.check",
                null);
    }

    /**
     * 秒杀订单支付超时 绑定
     * 秒杀订单支付超时队列 绑定到交换机
     *
     * @return
     */
    @Bean
    public Binding dealReleaseBinding() {
        return new Binding("deal.order.revert.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "deal.order.revert",
                null);
    }

    /**
     * 秒杀订单支付成功 绑定
     * 秒杀订单支付成功队列 绑定到交换机
     *
     * @return
     */
    @Bean
    public Binding dealPaySuccessBinding() {
        return new Binding("deal.order.pay.success.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "deal.order.pay.success",
                null);
    }

    @Bean
    public Binding dealToCacheBinding() {
        return new Binding("deal.to.cache.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "deal.to.cache",
                null);
    }

    @Bean
    public Binding lockGoodsStockBinding() {
        return new Binding("lock.goods.stock.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "to.lock.goods.stock",
                null);
    }

    @Bean
    public Binding revertGoodsStockBinding() {
        return new Binding("revert.goods.stock.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "to.revert.goods.stock",
                null);
    }
}