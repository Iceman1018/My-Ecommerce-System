package com.example.trade.goods.service;

import com.example.trade.goods.db.model.Goods;

import java.util.List;

public interface SearchService {
    void addGoodsToES(Goods goods);
    List<Goods> searchGoodsList(String keyword, int from, int size);
}
