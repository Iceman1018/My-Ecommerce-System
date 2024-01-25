package com.example.trade.order.mq;

import com.alibaba.fastjson.JSON;
import com.example.trade.order.client.GoodsFeignClient;
import com.example.trade.order.db.dao.OrderDao;
import com.example.trade.order.db.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class OrderPayCheckReceiver {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private GoodsFeignClient goodsService;

    @Autowired
    private OrderMessageSender orderMessageSender;


    @RabbitListener(queues="order.pay.status.check.queue")
    public void process(String msg){
        log.info("接收时间："+ LocalDateTime.now()+"接收内容："+msg);
        Order order= JSON.parseObject(msg,Order.class);
        // query order information
        Order orderInfo=orderDao.queryOrderById(order.getId());
        //status:0 no stock; 1 waiting for payment; 2 payment completed; 99 close order
        if(orderInfo.getStatus()==1){
            log.info("订单{}超时支付，关闭订单",orderInfo.getId());
            orderInfo.setStatus(99);
            orderDao.updateOrder(orderInfo);
            if(orderInfo.getActivityType()==1)
                orderMessageSender.sendRevertDealOrderMessage(JSON.toJSONString(order));
            else
                goodsService.revertStock(orderInfo.getGoodsId());

        }


    }
}
