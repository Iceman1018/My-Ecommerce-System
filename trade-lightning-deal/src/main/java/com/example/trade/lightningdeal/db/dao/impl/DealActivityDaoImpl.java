package com.example.trade.lightningdeal.db.dao.impl;

import com.example.trade.lightningdeal.db.dao.DealActivityDao;
import com.example.trade.lightningdeal.db.mappers.DealActivityMapper;
import com.example.trade.lightningdeal.db.model.DealActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Slf4j
@Repository
public class DealActivityDaoImpl implements DealActivityDao {
    @Autowired
    private DealActivityMapper dealActivityMapper;

    @Override
    public List<DealActivity> queryActivitiesByStartTime(Date startDate, Date endDate){
        return dealActivityMapper.queryActivitiesByStartTime(startDate,endDate);
    }


    @Override
    public boolean insertDealActivity(DealActivity dealActivity){
        int result= dealActivityMapper.insert(dealActivity);
        return result>0;
    }
    @Override
    public DealActivity queryDealActivityById(long id){
        return dealActivityMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<DealActivity> queryActivitiesByStatus(int status){
        return dealActivityMapper.queryActivitiesByStatus(status);
    }


    @Override
    public boolean lockStock(long id) {
        int result = dealActivityMapper.lockStock(id);
        //大于0 表示插入成功
        if (result < 0) {
            log.error("lock stock failed");
            return false;
        }
        return true;
    }

    @Override
    public boolean deductStock(long id) {
        int result = dealActivityMapper.deductStock(id);
        if (result < 1) {
            log.error("stock deduction failed");
            return false;
        }
        return true;
    }

    @Override
    public boolean revertStock(long id) {
        int result = dealActivityMapper.revertStock(id);
        if (result < 1) {
            log.error("reverting stock failed");
            return false;
        }
        return true;
    }

    @Override
    public boolean updateDealActivityStatus(long id){
        return dealActivityMapper.setActivityStatus(id)>0;
    }

    @Override
    public boolean closeExpiredActivities(Date nowTime){
        return dealActivityMapper.closeExpiredActivities(nowTime)>0;
    }

    @Override
    public List<DealActivity> queryExpiredActivities(Date nowTime){return dealActivityMapper.queryExpiredActivities(nowTime);}
}
