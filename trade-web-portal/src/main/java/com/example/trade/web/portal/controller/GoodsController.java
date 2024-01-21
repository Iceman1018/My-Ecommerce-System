package com.example.trade.web.portal.controller;

import com.example.trade.goods.db.model.Goods;
import com.example.trade.goods.service.GoodsService;
import com.example.trade.goods.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Slf4j
@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SearchService searchService;

    @RequestMapping("/goods/searchAction")
    public ResponseEntity<?> goodsSearch(@RequestParam("searchWords") String searchWords){
        log.info("search searchWord:{}",searchWords);
        List<Goods> goodsList=searchService.searchGoodsList(searchWords,0,10);
        return ResponseEntity.ok(goodsList);
    }
    @RequestMapping("/goods/{goodsId}")
    public ResponseEntity<?> goodsDetail(@PathVariable long goodsId){
        Goods goods = goodsService.queryGoodsById(goodsId);
        log.info("goodsId={},goods={}",goodsId, goods);
        return ResponseEntity.ok(goods);
    }


}
