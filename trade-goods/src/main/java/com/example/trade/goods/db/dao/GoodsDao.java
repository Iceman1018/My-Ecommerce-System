package com.example.trade.goods.db.dao;
import com.example.trade.goods.db.model.Goods;
import org.springframework.stereotype.Repository;


public interface GoodsDao {
    boolean insertGoods(Goods goods);

    boolean deleteGoods(long id);

    Goods queryGoodsById(long id);

    boolean updateGoods(Goods goods);

    boolean lockStock(long id);

    boolean deductStock(long id);

    boolean revertStock(long id);
}
