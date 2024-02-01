package com.example.trade.web.portal;

import com.example.trade.common.utils.RedisWorker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TradeWebPortalApplicationTests {
    @Autowired
    private RedisWorker redisWorker;
    @Test
    void contextLoads() {
        System.out.println(redisWorker.isItemInSet("invalid_cart_item","275957413943840768"));
    }

}
