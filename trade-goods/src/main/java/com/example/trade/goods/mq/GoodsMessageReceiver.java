package com.example.trade.goods.mq;
import com.alibaba.fastjson.JSON;
import com.example.trade.common.model.Order;
import com.example.trade.common.utils.RedisWorker;
import com.example.trade.goods.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GoodsMessageReceiver {
    @Autowired
    GoodsService goodsService;

    /**
     * 秒杀支付成功消息处理
     *
     * @param message
     */
    @RabbitListener(queues = "lock.goods.stock.queue")
    public void process1(String message) {
        log.info("lock some stock, got the message:{}", message);
        String[] msg=message.split(":");
        goodsService.lockStock(Long.valueOf(msg[0]),Integer.valueOf(msg[1]));
    }

    /**
     * 秒杀支付超时消息处理
     *
     * @param message
     */
    @RabbitListener(queues = "revert.goods.stock.queue")
    public void process2(String message) {
        log.info("Revert some stock, got the message:{}", message);
        String[] msg=message.split(":");
        goodsService.revertStock(Long.valueOf(msg[0]),Integer.valueOf(msg[1]));
    }
}
