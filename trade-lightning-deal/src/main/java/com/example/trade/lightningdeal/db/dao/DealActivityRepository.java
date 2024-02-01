package com.example.trade.lightningdeal.db.dao;

import com.example.trade.lightningdeal.db.model.DealActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface DealActivityRepository extends JpaRepository<DealActivity,Long> {

    @Query("SELECT da FROM DealActivity da WHERE da.endTime > :startDate AND :endDate >= da.startTime")
    List<DealActivity> queryActivitiesByStartTime(Date startDate, Date endDate);

    @Query("SELECT da FROM DealActivity da WHERE da.activityStatus = :status")
    List<DealActivity> queryActivitiesByStatus(int status);


    @Query("SELECT da FROM DealActivity da WHERE :nowTime >= da.endTime")
    List<DealActivity> queryExpiredActivities(Date nowTime);

    @Modifying
    @Transactional
    @Query("UPDATE DealActivity da SET da.activityStatus = 0 WHERE da.id = :id and da.activityStatus = 1")
    int updateDealActivityStatus(long id);

    @Modifying
    @Transactional
    @Query("UPDATE DealActivity da SET da.availableStock = da.availableStock - :num, da.lockStock = da.lockStock + :num WHERE da.id = :id AND da.availableStock >= :num")
    int lockStock(long id,int num);

    @Modifying
    @Transactional
    @Query("UPDATE DealActivity da SET da.lockStock = da.lockStock - :num WHERE da.id = :id")
    int deductStock(long id, int num);

    @Modifying
    @Transactional
    @Query("UPDATE DealActivity da SET da.availableStock = da.availableStock + :num, da.lockStock = da.lockStock - :num WHERE da.id = :id")
    int revertStock(long id, int num);
}
