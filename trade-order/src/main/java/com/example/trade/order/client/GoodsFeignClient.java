package com.example.trade.order.client;

import com.example.trade.common.model.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="Goods-service",contextId="trade-order")
public interface GoodsFeignClient {
    @RequestMapping("/api/goods/queryGoodsById")
    Goods queryGoodsById(@RequestParam("id") long id);

    @RequestMapping("/api/goods/lockStock")
    boolean lockStock(@RequestParam("id") long id);

    @RequestMapping("/api/goods/deductStock")
    boolean deductStock(@RequestParam("id") long id);

    @RequestMapping("/api/goods/revertStock")
    boolean revertStock(@RequestParam("id") long id);

    @RequestMapping("/api/goods/searchGoodsList")
    List<Goods> searchGoodsList(@RequestParam("keyword") String keyword, @RequestParam("from") int from, @RequestParam("size") int size);
}
