package com.example.trade.goods.service;

import com.example.trade.goods.db.model.Goods;

import java.util.Optional;

public interface GoodsService {
    boolean insertGoods(Goods goods);


    Goods queryGoodsById(long id);


    boolean lockStock(long id,int num);

    boolean deductStock(long id,int num);

    boolean revertStock(long id,int num);

}
