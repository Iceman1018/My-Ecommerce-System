package com.example.trade.lightningdeal.service;

import com.example.trade.lightningdeal.db.model.SeckillActivity;
import com.example.trade.order.db.model.Order;

import java.util.List;

public interface SeckillActivityService {
    boolean insertSeckillActivity(SeckillActivity seckillActivity);

    SeckillActivity querySeckillActivityById(long id);
    List<SeckillActivity> queryActivitysByStatus(int status);
    boolean processSeckillReqBase(long seckillActivityId);
    boolean processSeckillReqRedis(long seckillActivityId);

    Order processSeckill(long userId, long seckillActivityId);
    boolean lockStock(long id);

    boolean deductStock(long id);
    boolean revertStock(long id);

    void pushSeckillActivityInfoToCache(long id);
}
