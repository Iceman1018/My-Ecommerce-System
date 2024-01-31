package com.example.trade.web.portal.client;

import com.example.trade.common.model.DealActivity;
import com.example.trade.common.model.Order;
import com.example.trade.common.model.TradeResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "Deal-service",contextId = "trade-web-portal")
public interface DealFeignClient {
    @RequestMapping("/api/deal/queryDealActivityById")
    DealActivity queryDealActivityById(@RequestParam("id") long id);

    @RequestMapping("/api/deal/queryActivitysByStatus")
    List<DealActivity> queryActivitysByStatus(@RequestParam("status") int status);
    @RequestMapping("/api/deal/processDeal")
    TradeResultDTO<Order> processDeal(@RequestParam("userId") long userId,
                                      @RequestParam("dealActivityId") long dealActivityId,
                                      @RequestParam("num") int num);


}
