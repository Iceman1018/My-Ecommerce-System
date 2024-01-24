package com.example.trade.lightningdeal.client;

import com.example.trade.common.model.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="Goods-service",contextId="trade-order")
public interface GoodsFeignClient {
    @RequestMapping("/api/goods/queryGoodsById")
    Goods queryGoodsById(@RequestParam("id") long id);

}
