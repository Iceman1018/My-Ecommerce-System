package com.example.trade.web.portal;

import com.example.trade.common.utils.RedisWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {


    @Autowired
    public RedisWorker redisWorker;

    @Test
    public void setValue() {
        redisWorker.setValue("testName","你好");
    }

    @Test
    public void getValue() {
        System.out.println(redisWorker.getValueByKey("testName"));
    }

    @Test
    public void setJmeterStockTest(){redisWorker.setValue("stock:2",10L);}

}
