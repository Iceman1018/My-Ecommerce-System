package com.example.trade.web.portal.mq;

import com.alibaba.fastjson.JSON;
import com.example.trade.common.utils.RedisWorker;
import com.example.trade.web.portal.client.GoodsFeignClient;
import com.example.trade.web.portal.client.model.DealActivity;
import com.example.trade.web.portal.client.model.Goods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class CacheMessageReceiver {
    @Autowired
    GoodsFeignClient goodsService;
    @Autowired
    RedisWorker redisWorker;


    @RabbitListener(queues="deal.to.cache.queue")
    public void process(String msg){
        log.info("接收时间："+ LocalDateTime.now()+"接收内容："+msg);
        DealActivity dealActivity=JSON.parseObject(msg,DealActivity.class);
        Goods goods = goodsService.queryGoodsById(dealActivity.getGoodsId());
        if(goods!=null) {
            redisWorker.setValue("dealActivity:" + dealActivity.getId(), JSON.toJSONString(dealActivity));
            redisWorker.setValue("dealActivity_goods:" + dealActivity.getGoodsId(), JSON.toJSONString(goods));
            log.info("Add deal activity to cache,key:{}","dealActivity:" + dealActivity.getId());
        }
    }
}
