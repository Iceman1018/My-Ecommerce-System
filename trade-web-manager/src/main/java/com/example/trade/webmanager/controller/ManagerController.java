package com.example.trade.webmanager.controller;
import com.example.trade.webmanager.client.DealFeignClient;
import com.example.trade.webmanager.client.GoodsFeignClient;
import com.example.trade.webmanager.client.model.DealActivity;
import com.example.trade.webmanager.client.model.Goods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
@RestController
public class ManagerController {
    @Autowired
    private GoodsFeignClient goodsService;

    @Autowired
    private DealFeignClient dealActivityService;


    @RequestMapping("/admin/addGoodsAction")
    public ResponseEntity<?> addGoodsAction(@RequestParam("title") String title,
                                         @RequestParam("number") String number,
                                         @RequestParam("brand") String brand,
                                         @RequestParam("image") String image,
                                         @RequestParam("description") String description,
                                         @RequestParam("price") int price,
                                         @RequestParam("keywords") String keywords,
                                         @RequestParam("category") String category,
                                         @RequestParam("stock") int stock){
        Goods goods = new Goods();
        goods.setTitle(title);
        goods.setNumber(number);
        goods.setBrand(brand);
        goods.setImage(image);
        goods.setDescription(description);
        goods.setPrice(price);
        goods.setKeywords(keywords);
        goods.setCategory(category);
        goods.setAvailableStock(stock);
        goods.setLockStock(0);
        goods.setStatus(1);
        goods.setSaleNum(0);
        goods.setCreateTime(new Date());
        boolean result=goodsService.insertGoods(goods);
        log.info("add goods /result={}",result);
        if(result)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding goods");
    }

    @RequestMapping("/admin/addDealActivityAction")
    public ResponseEntity<?> addDealActivityAction(@RequestParam("activityName") String activityName,
                                                    @RequestParam("goodsId") long goodsId,
                                                    @RequestParam("startTime") String startTime,
                                                    @RequestParam("endTime") String endTime,
                                                    @RequestParam("availableStock") int availableStock,
                                                    @RequestParam("dealPrice") int dealPrice,
                                                    @RequestParam("oldPrice") int oldPrice){
        try{
            DealActivity dealActivity = new DealActivity();
            dealActivity.setActivityName(activityName);
            dealActivity.setGoodsId(goodsId);

            startTime=startTime.substring(0,10)+" "+startTime.substring(11);
            endTime=endTime.substring(0,10)+" "+endTime.substring(11);
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm");
            dealActivity.setStartTime(format.parse(startTime));
            dealActivity.setEndTime(format.parse(endTime));
            dealActivity.setAvailableStock(availableStock);

            dealActivity.setActivityStatus(1);

            dealActivity.setLockStock(0);
            dealActivity.setDealPrice(dealPrice);
            dealActivity.setOldPrice(oldPrice);
            dealActivity.setCreateTime(new Date());
            dealActivityService.insertDealActivity(dealActivity);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            log.error("addSkillActivityAction error",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding skill activity");
        }
    }
}
