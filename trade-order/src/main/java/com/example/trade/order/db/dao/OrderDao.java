package com.example.trade.order.db.dao;

import com.example.trade.order.db.model.Order;

import java.util.List;

public interface OrderDao {
    boolean insertOrder(Order order);
    boolean deleteOrder(long id);
    Order queryOrderById(long id);
    boolean updateOrder(Order order);
    List<Order> queryOrderListByUserId(Long userId);
}
