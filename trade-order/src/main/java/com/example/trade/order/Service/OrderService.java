package com.example.trade.order.Service;

import com.example.trade.order.db.model.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderService {
    Order createOrder(long userId, long goodsId,int goodsNum);
    Order queryOrder(long orderId);
    List<Order> queryOrderListByUserId(long userId);
    void payOrder(long orderId);
}
