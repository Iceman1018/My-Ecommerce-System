package com.example.trade.webmanager;

import com.example.trade.webmanager.client.DealFeignClient;
import com.example.trade.webmanager.client.GoodsFeignClient;
import com.example.trade.webmanager.client.model.DealActivity;
import com.example.trade.webmanager.client.model.Goods;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@Slf4j
class TradeWebManagerApplicationTests {

    @Autowired
    GoodsFeignClient goodsService;

    @Autowired
    DealFeignClient dealActivityService;

    @Test
    void addGoodsTest() {
        Goods goods = new Goods();
        goods.setTitle("Diet coke");
        goods.setNumber("2");
        goods.setBrand("COCA");
        goods.setImage("");
        goods.setDescription("zero sugar");
        goods.setPrice(100);
        goods.setKeywords("diet");
        goods.setCategory("Beverage");
        goods.setAvailableStock(1000);
        goods.setLockStock(0);
        goods.setStatus(1);
        goods.setSaleNum(0);
        goods.setCreateTime(new Date());
        boolean result=goodsService.insertGoods(goods);
        log.info("add goods /result={}",result);
    }
    @Test
    void addDealTest() {
        try {
            DealActivity dealActivity = new DealActivity();
            dealActivity.setActivityName("Black Friday");
            dealActivity.setGoodsId(new Long(21));

            String startTime = "2024-01-28 0:00:00";
            String endTime = "2024-01-28 18:45:00";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            dealActivity.setStartTime(format.parse(startTime));
            dealActivity.setEndTime(format.parse(endTime));
            dealActivity.setAvailableStock(50);

            dealActivity.setActivityStatus(1);

            dealActivity.setLockStock(0);
            dealActivity.setDealPrice(10);
            dealActivity.setOldPrice(100);
            dealActivity.setCreateTime(new Date());
            dealActivityService.insertDealActivity(dealActivity);
            log.info("deal created");
        }catch (Exception e) {
            log.error("error");
        }
    }


}
