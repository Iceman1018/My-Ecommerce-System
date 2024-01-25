package com.example.trade.lightningdeal.service;

import com.example.trade.common.model.Order;
import com.example.trade.lightningdeal.db.model.DealActivity;

import java.time.LocalDateTime;
import java.util.List;

public interface DealActivityService {
    boolean insertDealActivity(DealActivity dealActivity);

    DealActivity queryDealActivityById(long id);
    List<DealActivity> queryActivitysByStatus(int status);

    Order processDeal(long userId, long dealActivityId);
    boolean lockStock(long id);

    boolean deductStock(long id);
    boolean revertStock(long id);


}
