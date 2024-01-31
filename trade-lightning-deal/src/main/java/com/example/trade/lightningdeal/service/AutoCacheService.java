package com.example.trade.lightningdeal.service;


import com.alibaba.fastjson.JSON;
import com.example.trade.common.utils.RedisWorker;
import com.example.trade.lightningdeal.db.dao.DealActivityRepository;
import com.example.trade.lightningdeal.db.model.DealActivity;
import com.example.trade.lightningdeal.mq.CartItemExpirationMessageSender;
import com.example.trade.lightningdeal.mq.DealOrderMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Component
@Slf4j
public class AutoCacheService {
    @Autowired
    DealActivityRepository dealActivityDao;
    @Autowired
    RedisWorker redisWorker;
    @Autowired
    DealOrderMessageSender dealOrderMessageSender;

    @Autowired
    CartItemExpirationMessageSender cartItemExpirationMessageSender;
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
                if(dealActivityDao.updateDealActivityStatus(expiredDeal.getId())>0) {
                    int restStock = expiredDeal.getAvailableStock() + expiredDeal.getLockStock();
                    dealOrderMessageSender.sendActivityExpirationMessage(expiredDeal.getGoodsId() + ":" + restStock);
                    cartItemExpirationMessageSender.sendCartItemExpirationMessage(String.valueOf(expiredDeal.getId()));
                }
                redisWorker.removeKey(key);
                redisWorker.removeKey("dealActivity_goods:" + expiredDeal.getGoodsId());
                log.info("Remove deal {} activity from cache",expiredDeal.getId());
            }
        }

        List<DealActivity> nextActivities=dealActivityDao.queryActivitiesByStartTime(nowTime,nextcheckTime);
        for(DealActivity dealActivity:nextActivities) {
            dealOrderMessageSender.sendDealCacheMessage(JSON.toJSONString(dealActivity));
        }
    }
}
