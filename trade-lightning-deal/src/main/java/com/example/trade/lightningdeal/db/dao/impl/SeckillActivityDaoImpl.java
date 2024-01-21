package com.example.trade.lightningdeal.db.dao.impl;

import com.example.trade.lightningdeal.db.dao.SeckillActivityDao;
import com.example.trade.lightningdeal.db.mappers.SeckillActivityMapper;
import com.example.trade.lightningdeal.db.model.SeckillActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class SeckillActivityDaoImpl implements SeckillActivityDao {
    @Autowired
    private SeckillActivityMapper seckillActivityMapper;

    @Override
    public boolean insertSeckillActivity(SeckillActivity seckillActivity){
        int result= seckillActivityMapper.insert(seckillActivity);
        return result>0;
    }
    @Override
    public SeckillActivity querySeckillActivityById(long id){
        return seckillActivityMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SeckillActivity> queryActivitiesByStatus(int status){
        return seckillActivityMapper.queryActivitiesByStatus(status);
    }

    @Override
    public boolean updateAvailableStockByPrimaryKey(long id){
        return seckillActivityMapper.updateAvailableStockByPrimaryKey(id)>0;
    }

    @Override
    public boolean lockStock(long id) {
        int result = seckillActivityMapper.lockStock(id);
        //大于0 表示插入成功
        if (result < 0) {
            log.error("lock stock failed");
            return false;
        }
        return true;
    }

    @Override
    public boolean deductStock(long id) {
        int result = seckillActivityMapper.deductStock(id);
        if (result < 1) {
            log.error("stock deduction failed");
            return false;
        }
        return true;
    }

    @Override
    public boolean revertStock(long id) {
        int result = seckillActivityMapper.revertStock(id);
        if (result < 1) {
            log.error("reverting stock failed");
            return false;
        }
        return true;
    }

}
