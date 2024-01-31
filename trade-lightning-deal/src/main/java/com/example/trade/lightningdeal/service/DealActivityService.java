package com.example.trade.lightningdeal.service;

import com.example.trade.common.model.Order;
import com.example.trade.lightningdeal.db.model.DealActivity;

import java.time.LocalDateTime;
import java.util.List;

public interface DealActivityService {
    boolean insertDealActivity(DealActivity dealActivity);

    DealActivity queryDealActivityById(long id);
    List<DealActivity> queryActivitysByStatus(int status);

    Order processDeal(long userId, long dealActivityId, int num);
    boolean lockStock(long id,int num);

    boolean deductStock(long id,int num);
    boolean revertStock(long id,int num);


}
