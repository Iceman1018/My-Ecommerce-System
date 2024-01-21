package com.example.trade.goods.service.impl;

import com.example.trade.goods.db.dao.GoodsDao;
import com.example.trade.goods.db.model.Goods;
import com.example.trade.goods.service.GoodsService;
import com.example.trade.goods.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceimpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private SearchService searchService;

    @Override
    public boolean insertGoods(Goods goods) {
        boolean res=goodsDao.insertGoods(goods);
        searchService.addGoodsToES(goods);
        return res;
    }
    @Override
    public Goods queryGoodsById(long id){
        return goodsDao.queryGoodsById(id);
    }

    @Override
    public boolean lockStock(long id){
        return goodsDao.lockStock(id);
    }

    @Override
    public boolean deductStock(long id){
        return goodsDao.deductStock(id);
    }

    @Override
    public boolean revertStock(long id){
        return goodsDao.revertStock(id);
    }





}
