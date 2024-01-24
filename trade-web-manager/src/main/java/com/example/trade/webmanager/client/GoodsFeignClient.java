package com.example.trade.webmanager.client;


import com.example.trade.webmanager.client.model.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="Goods-service",contextId = "trade-web-manager")
public interface GoodsFeignClient {
    @RequestMapping("/api/goods/insertGoods")
    boolean insertGoods(@RequestBody Goods goods);
}
