package com.example.trade.lightningdeal.db.dao;

import com.example.trade.lightningdeal.db.model.DealActivity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface DealActivityDao {

    List<DealActivity> queryActivitiesByStartTime(Date startDate, Date endDate);


    boolean insertDealActivity(DealActivity dealActivity);
    DealActivity queryDealActivityById(long id);

    List<DealActivity> queryActivitiesByStatus(int status);

    boolean updateDealActivityStatus(long id);

    List<DealActivity> queryExpiredActivities(Date nowTime);

    boolean closeExpiredActivities(Date nowTime);
    /**
     * 锁定秒杀的库存
     * @param id
     * @return
     */
    boolean lockStock(long id);

    /**
     * 库存扣减
     * @param id
     * @return
     */
    boolean deductStock(long id);

    /**
     * 锁定的库存回补
     * @param id
     * @return
     */
    boolean revertStock(long id);
}
