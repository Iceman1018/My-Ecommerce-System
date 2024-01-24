package com.example.trade.web.portal.client;

import com.example.trade.web.portal.client.model.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="Goods-service",contextId="trade-web-portal")
public interface GoodsFeignClient {
    @RequestMapping("/api/goods/queryGoodsById")
    Goods queryGoodsById(@RequestParam("id") long id);

    @RequestMapping("/api/goods/searchGoodsList")
    List<Goods> searchGoodsList(@RequestParam("keyword") String keyword, @RequestParam("from") int from, @RequestParam("size") int size);
}
