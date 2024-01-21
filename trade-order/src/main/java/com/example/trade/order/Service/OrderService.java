package com.example.trade.order.Service;

import com.example.trade.order.db.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(long userId, long goodsId);
    Order queryOrder(long orderId);
    List<Order> queryOrderListByUserId(long userId);
    void payOrder(long orderId);
}
