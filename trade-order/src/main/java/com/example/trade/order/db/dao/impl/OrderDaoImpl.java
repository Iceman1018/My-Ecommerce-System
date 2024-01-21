package com.example.trade.order.db.dao.impl;

import com.example.trade.order.db.dao.OrderDao;
import com.example.trade.order.db.mappers.OrderMapper;
import com.example.trade.order.db.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public boolean insertOrder(Order order){
        int result = orderMapper.insert(order);
        if(result>0){
            return true;
        }
        return false;
    }
    @Override
    public boolean deleteOrder(long id){
        int result = orderMapper.deleteByPrimaryKey(id);
        return result>0;
    }
    @Override
    public Order queryOrderById(long id){
        Order goods=orderMapper.selectByPrimaryKey(id);
        return goods;
    }
    @Override
    public boolean updateOrder(Order order){
        int result = orderMapper.updateByPrimaryKey(order);
        return result>0;
    }
    @Override
    public List<Order> queryOrderListByUserId(Long id){
        return orderMapper.queryOrderListByUserId(id);
    }
}
