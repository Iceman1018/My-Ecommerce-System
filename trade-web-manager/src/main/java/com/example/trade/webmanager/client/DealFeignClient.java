package com.example.trade.webmanager.client;

import com.example.trade.webmanager.client.model.DealActivity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@FeignClient(name="Deal-service",contextId = "trade-web-manager")
public interface DealFeignClient {
    @RequestMapping("/api/deal/insertDealActivity")
    boolean insertDealActivity(@RequestBody DealActivity dealActivity);

}
