package com.example.trade.web.portal.controller;

import com.alibaba.fastjson.JSON;
import com.example.trade.order.Service.OrderService;
import com.example.trade.order.db.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
public class OrderController {
    @Autowired
    OrderService orderService;
    @RequestMapping("/order/query/{orderId}")
    public ResponseEntity<?> orderQuery(HttpSession session, @PathVariable long orderId){
        if(session.getAttribute("userId")==null){
            log.error("You haven't logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        Order order=orderService.queryOrder(orderId);
        if(order==null){
            log.error("This order does not exist");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("This order does not exist");
        }
        if(!session.getAttribute("userId").equals(order.getUserId())){
            log.error("This order does not belong to you");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This order does not belong to you");
        }
        log.info("orderId={} order={}",orderId, JSON.toJSON(order));
        return ResponseEntity.ok(order);
    }
    @RequestMapping("/order/payOrder/{orderId}")
    public ResponseEntity<?> payOrder(HttpSession session,@PathVariable long orderId){
        if(session.getAttribute("userId")==null){
            log.error("You haven't logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        Order order=orderService.queryOrder(orderId);
        if(order==null){
            log.error("This order does not exist");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("This order does not exist");
        }
        if(!session.getAttribute("userId").equals(order.getUserId())){
            log.error("This order does not belong to you");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This order does not belong to you");
        }
        try {
            orderService.payOrder(orderId);
            log.info("Pay successfully");
            return ResponseEntity.ok().build();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @RequestMapping("/order/create/{goodsId}")
    public ResponseEntity<?> buy(HttpSession session,@PathVariable long goodsId){
        if(session.getAttribute("userId")==null){
            log.error("You haven't logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        Long userId=((Number)session.getAttribute("userId")).longValue();
        log.info("userId={},goodsId={}",userId,goodsId);
        try {
            Order order = orderService.createOrder(userId, goodsId);
            log.info("Order created!");
            return ResponseEntity.ok(order);
        }catch (Exception e)
        {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
