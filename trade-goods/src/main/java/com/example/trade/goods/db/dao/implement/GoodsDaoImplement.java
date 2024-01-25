package com.example.trade.goods.db.dao.implement;

import com.example.trade.goods.db.dao.GoodsDao;
import com.example.trade.goods.db.mappers.GoodsMapper;
import com.example.trade.goods.db.model.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GoodsDaoImplement implements GoodsDao {
    @Autowired
    private GoodsMapper goodsMapper;
    @Override
    public boolean insertGoods(Goods goods){
        int result = goodsMapper.insert(goods);
        return result>0;

    }
    @Override
    public boolean deleteGoods(long id){
        int result= goodsMapper.deleteByPrimaryKey(id);
        return result>0;
    }
    @Override
    public Goods queryGoodsById(long id){
        Goods goods = goodsMapper.selectByPrimaryKey(id);
        return goods;
    }
    @Override
    public boolean updateGoods(Goods goods){
        int result = goodsMapper.updateByPrimaryKey(goods);
        return result>0;
    }
    @Override
    public boolean lockStock(long id){
        int result=goodsMapper.lockStock(id);
        return result>0;
    }

    @Override
    public boolean deductStock(long id){
        int result=goodsMapper.deductStock(id);
        return result>0;
    }

    @Override
    public boolean revertStock(long id){
        int result=goodsMapper.revertStock(id);
        return result>0;
    }

    @Override
    public boolean lockStock(long id,int num){
        int result=goodsMapper.lockStockMulti(id,num);
        return result>0;
    }

    @Override
    public boolean deductStock(long id,int num){
        int result=goodsMapper.deductStockMulti(id,num);
        return result>0;
    }

    @Override
    public boolean revertStock(long id,int num){
        int result=goodsMapper.revertStockMulti(id,num);
        return result>0;
    }
}
