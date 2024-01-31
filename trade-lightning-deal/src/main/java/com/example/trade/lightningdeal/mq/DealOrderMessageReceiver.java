package com.example.trade.lightningdeal.mq;
import com.alibaba.fastjson.JSON;
import com.example.trade.common.model.Order;
import com.example.trade.common.utils.RedisWorker;
import com.example.trade.lightningdeal.service.DealActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DealOrderMessageReceiver {
    @Autowired
    private DealActivityService dealActivityService;

    @Autowired
    private RedisWorker redisWorker;


    /**
     * 秒杀支付成功消息处理
     *
     * @param message
     */
    @RabbitListener(queues = "deal.order.pay.success.queue")
    public void process1(String message) {
        log.info("deal order pay finished, got the message:{}", message);
        Order order = JSON.parseObject(message, Order.class);
        //扣减库存
        dealActivityService.deductStock(order.getActivityId(),order.getGoodsNum());
    }

    /**
     * 秒杀支付超时消息处理
     *
     * @param message
     */
    @RabbitListener(queues = "deal.order.revert.queue")
    public void process2(String message) {
        log.info("Deal order pay unfinished,got the message:{}", message);
        Order order = JSON.parseObject(message, Order.class);
        redisWorker.removeLimit(order.getActivityId(), order.getUserId(),order.getGoodsNum());
        redisWorker.stockRevert(order.getActivityId(), order.getGoodsNum());
        //秒杀库存回补
        dealActivityService.revertStock(order.getActivityId(),order.getGoodsNum());

    }
}
