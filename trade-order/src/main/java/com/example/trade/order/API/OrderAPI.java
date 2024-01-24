package com.example.trade.order.API;


import com.example.trade.order.Service.OrderService;
import com.example.trade.order.db.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
public class OrderAPI {
    @Autowired
    private OrderService orderService;

    @GetMapping("/api/order/createOrder")
    @ResponseBody
    public Order createOrder(long userId, long goodsId) {
        log.info("createOrder userId:{} goodsId:{}", userId, goodsId);
        return orderService.createOrder(userId, goodsId);
    }
    @GetMapping("/api/order/queryOrder")
    @ResponseBody
    public Order queryOrder(long orderId) {
        log.info("queryOrder orderId:{}", orderId);
        return orderService.queryOrder(orderId);
    }

    @GetMapping("/api/order/payOrder")
    @ResponseBody
    public void payOrder(long orderId) {
        log.info("payOrder orderId:{}", orderId);
        orderService.payOrder(orderId);
    }

    @GetMapping("/api/order/queryOrderListByUserId")
    @ResponseBody
    public List<Order> queryOrderListByUserId(long userId){
        log.info("queryOrderListByUserId userId:{}", userId);
        return orderService.queryOrderListByUserId(userId);
    }
}
