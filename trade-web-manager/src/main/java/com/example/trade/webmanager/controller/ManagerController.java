package com.example.trade.webmanager.controller;

import com.example.trade.goods.db.dao.GoodsDao;
import com.example.trade.goods.db.model.Goods;
import com.example.trade.goods.service.GoodsService;
import com.example.trade.lightningdeal.db.model.SeckillActivity;
import com.example.trade.lightningdeal.service.SeckillActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
public class ManagerController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SeckillActivityService seckillActivityService;


    @RequestMapping("/addGoodsAction")
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

    @RequestMapping("/addSkillActivityAction")
    public ResponseEntity<?> addSkillActivityAction(@RequestParam("activityName") String activityName,
                                                    @RequestParam("goodsId") long goodsId,
                                                    @RequestParam("startTime") String startTime,
                                                    @RequestParam("endTime") String endTime,
                                                    @RequestParam("availableStock") int availableStock,
                                                    @RequestParam("seckillPrice") int seckillPrice,
                                                    @RequestParam("oldPrice") int oldPrice){
        try{
            SeckillActivity seckillActivity = new SeckillActivity();
            seckillActivity.setActivityName(activityName);
            seckillActivity.setGoodsId(goodsId);

            startTime=startTime.substring(0,10)+" "+startTime.substring(11);
            endTime=endTime.substring(0,10)+" "+endTime.substring(11);
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm");
            seckillActivity.setStartTime(format.parse(startTime));
            seckillActivity.setEndTime(format.parse(endTime));
            seckillActivity.setAvailableStock(availableStock);

            seckillActivity.setActivityStatus(1);

            seckillActivity.setLockStock(0);
            seckillActivity.setSeckillPrice(seckillPrice);
            seckillActivity.setOldPrice(oldPrice);
            seckillActivity.setCreateTime(new Date());
            seckillActivityService.insertSeckillActivity(seckillActivity);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            log.error("addSkillActivityAction error",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding skill activity");
        }
    }


    @RequestMapping("/pushSeckillCacheAction")
    public ResponseEntity<?> pushSkillCache(HttpSession session,
                                            @RequestParam("seckillId")long seckillId){
        try {
            if(session.getAttribute("userId")==null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You don't have the admin authority");
            seckillActivityService.pushSeckillActivityInfoToCache(seckillId);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding activity into cache");
        }
    }
}
