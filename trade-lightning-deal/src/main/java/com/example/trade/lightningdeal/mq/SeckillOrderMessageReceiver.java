package com.example.trade.lightningdeal.mq;
import com.alibaba.fastjson.JSON;
import com.example.trade.lightningdeal.service.SeckillActivityService;
import com.example.trade.order.Service.LimitBuyService;
import com.example.trade.order.db.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SeckillOrderMessageReceiver {
    @Autowired
    private SeckillActivityService seckillActivityService;

    @Autowired
    private LimitBuyService limitBuyService;


    /**
     * 秒杀支付成功消息处理
     *
     * @param message
     */
    @RabbitListener(queues = "seckill.order.pay.success.queue")
    public void process1(String message) {
        log.info("seckill order pay finished, got the message:{}", message);
        Order order = JSON.parseObject(message, Order.class);
        //扣减库存
        seckillActivityService.deductStock(order.getActivityId());
    }

    /**
     * 秒杀支付超时消息处理
     *
     * @param message
     */
    @RabbitListener(queues = "seckill.order.revert.queue")
    public void process2(String message) {
        log.info("Seckill order pay unfinished,got the message:{}", message);
        Order order = JSON.parseObject(message, Order.class);
        limitBuyService.removeLimitMember(order.getActivityId(), order.getUserId());
        //秒杀库存回补
        seckillActivityService.revertStock(order.getActivityId());
    }
}
