package com.example.trade.lightningdeal.service;


import com.alibaba.fastjson.JSON;
import com.example.trade.common.model.Goods;
import com.example.trade.common.utils.RedisWorker;
import com.example.trade.lightningdeal.client.GoodsFeignClient;
import com.example.trade.lightningdeal.db.dao.DealActivityDao;
import com.example.trade.lightningdeal.db.model.DealActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Component
@Slf4j
public class AutoCacheService {
    @Autowired
    DealActivityDao dealActivityDao;
    @Autowired
    RedisWorker redisWorker;

    @Autowired
    GoodsFeignClient goodsFeignClient;
    @Scheduled(fixedRate = 120000)
    public void updateRedisCache(){
        log.info("AntoCache Management Running");
        Date nowTime=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowTime);
        calendar.add(Calendar.MINUTE, 2);
        Date nextcheckTime=calendar.getTime();
        List<String> keys=redisWorker.findKeySlow("dealActivity:");
        for(String key:keys){
            DealActivity expiredDeal=JSON.parseObject(redisWorker.getValueByKey(key), DealActivity.class);
            log.info("deal {}'s end time is {}",expiredDeal.getId(),expiredDeal.getEndTime().toString());
            if(expiredDeal.getEndTime().before(new Date())){
                redisWorker.removeKey(key);
                redisWorker.removeKey("dealActivity_goods:" + expiredDeal.getGoodsId());
                log.info("Remove deal {} activity from cache",expiredDeal.getId());
            }
        }

        List<DealActivity> nextActivities=dealActivityDao.queryActivitiesByStartTime(nowTime,nextcheckTime);
        for(DealActivity dealActivity:nextActivities) {
            Goods goods = goodsFeignClient.queryGoodsById(dealActivity.getGoodsId());
            if(goods!=null) {
                redisWorker.setValue("dealActivity:" + dealActivity.getId(), JSON.toJSONString(dealActivity));
                redisWorker.setValue("dealActivity_goods:" + dealActivity.getGoodsId(), JSON.toJSONString(goods));
                log.info("Add deal activity to cache,key:{}","dealActivity:" + dealActivity.getId());
            }
        }
    }
}
