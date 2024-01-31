package com.example.trade.goods.db.dao;

import com.example.trade.goods.db.model.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface GoodsRepository extends JpaRepository<Goods,Long>{
    @Modifying
    @Transactional
    @Query("UPDATE Goods g SET g.availableStock = g.availableStock - :num, g.lockStock = g.lockStock + :num WHERE g.id = :id AND g.availableStock >= :num")
    int lockStock(long id,int num);
    @Modifying
    @Transactional
    @Query("UPDATE Goods g SET g.lockStock = g.lockStock - :num, g.saleNum = g.saleNum + :num WHERE g.id = :id")
    int deductStock(long id, int num);
    @Modifying
    @Transactional
    @Query("UPDATE Goods g SET g.availableStock = g.availableStock + :num, g.lockStock = g.lockStock - :num WHERE g.id = :id")
    int revertStock(long id, int num);
}
