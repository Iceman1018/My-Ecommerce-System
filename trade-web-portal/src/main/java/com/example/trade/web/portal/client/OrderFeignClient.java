package com.example.trade.web.portal.client;
import com.example.trade.common.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Order-service", contextId = "trade-web-portal")
public interface OrderFeignClient {

    @RequestMapping("/api/order/createOrder")
    Order createOrder(@RequestParam("userId") long userId,
                      @RequestParam("goodsId") long goodsId,
                      @RequestParam("goodsNum") long goodsNum);

    @RequestMapping("/api/order/queryOrder")
    Order queryOrder(@RequestParam("orderId") long orderId);

    @RequestMapping("/api/order/payOrder")
    boolean payOrder(@RequestParam("orderId") long orderId);

}
