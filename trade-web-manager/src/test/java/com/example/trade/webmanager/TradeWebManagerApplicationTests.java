package com.example.trade.webmanager;

import com.example.trade.goods.db.model.Goods;
import com.example.trade.goods.service.GoodsService;
import com.example.trade.lightningdeal.db.model.SeckillActivity;
import com.example.trade.lightningdeal.service.SeckillActivityService;
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
    GoodsService goodsService;

    @Autowired
    SeckillActivityService seckillActivityService;

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
    void addSeckillTest() {
        try {
            SeckillActivity seckillActivity = new SeckillActivity();
            seckillActivity.setActivityName("恒宝大寿");
            seckillActivity.setGoodsId(new Long(1));

            String startTime = "2024-01-20 14:18:00";
            String endTime = "2024-01-20 14:18:00";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            seckillActivity.setStartTime(format.parse(startTime));
            seckillActivity.setEndTime(format.parse(endTime));
            seckillActivity.setAvailableStock(50);

            seckillActivity.setActivityStatus(1);

            seckillActivity.setLockStock(0);
            seckillActivity.setSeckillPrice(10);
            seckillActivity.setOldPrice(100);
            seckillActivity.setCreateTime(new Date());
            seckillActivityService.insertSeckillActivity(seckillActivity);
            log.info("seckill created");
        }catch (Exception e) {
            log.error("error");
        }
    }


}
