package com.example.trade.lightningdeal.db.mappers;

import com.example.trade.lightningdeal.db.model.DealActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface DealActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DealActivity record);

    int insertSelective(DealActivity record);

    DealActivity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DealActivity record);

    int updateByPrimaryKey(DealActivity record);

    List<DealActivity> queryActivitiesByStatus(int status);

    int updateAvailableStockByPrimaryKey(Long id);

    int lockStock(Long id);

    int deductStock(Long id);

    int revertStock(Long id);

    List<DealActivity> queryActivitiesByStartTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}