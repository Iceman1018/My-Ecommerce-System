package com.example.trade.goods.service.impl;

import com.example.trade.goods.db.dao.GoodsRepository;
import com.example.trade.goods.db.model.Goods;
import com.example.trade.goods.service.GoodsService;
import com.example.trade.goods.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class GoodsServiceimpl implements GoodsService {
    @Autowired
    private GoodsRepository goodsDao;
    @Autowired
    private SearchService searchService;

    @Override
    public boolean insertGoods(Goods goods) {
        try {
            goodsDao.save(goods);
            searchService.addGoodsToES(goods);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    @Override
    public Goods queryGoodsById(long id){
        return goodsDao.findById(id).orElse(null);
    }

    @Override
    public boolean lockStock(long id,int num){ return goodsDao.lockStock(id,num)>0; }

    @Override
    public boolean deductStock(long id,int num){
        return goodsDao.deductStock(id,num)>0;
    }

    @Override
    public boolean revertStock(long id,int num){
        return goodsDao.revertStock(id,num)>0;
    }





}
