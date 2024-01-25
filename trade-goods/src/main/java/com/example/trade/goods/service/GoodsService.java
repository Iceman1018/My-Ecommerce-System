package com.example.trade.goods.service;

import com.example.trade.goods.db.model.Goods;

public interface GoodsService {
    boolean insertGoods(Goods goods);


    Goods queryGoodsById(long id);


    boolean lockStock(long id);


    boolean deductStock(long id);


    boolean revertStock(long id);

    boolean lockStock(long id,int num);

    boolean revertStock(long id,int num);

}
