package com.example.trade.lightningdeal.db.dao;

import com.example.trade.lightningdeal.db.model.SeckillActivity;

import java.util.List;

public interface SeckillActivityDao {
    boolean insertSeckillActivity(SeckillActivity seckillActivity);
    SeckillActivity querySeckillActivityById(long id);

    List<SeckillActivity> queryActivitiesByStatus(int status);

    boolean updateAvailableStockByPrimaryKey(long id);

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
