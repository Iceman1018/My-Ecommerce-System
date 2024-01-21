package com.example.trade.lightningdeal.db.mappers;

import com.example.trade.lightningdeal.db.model.SeckillActivity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SeckillActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillActivity record);

    int insertSelective(SeckillActivity record);

    SeckillActivity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillActivity record);

    int updateByPrimaryKey(SeckillActivity record);

    List<SeckillActivity> queryActivitiesByStatus(int status);

    int updateAvailableStockByPrimaryKey(Long id);

    int lockStock(Long id);

    int deductStock(Long id);

    int revertStock(Long id);
}