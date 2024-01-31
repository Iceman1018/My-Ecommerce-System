package com.example.trade.order.db.dao;

import com.example.trade.order.db.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("FROM Order WHERE userId = :userId")
    List<Order> queryOrderListByUserId(Long userId);
}
