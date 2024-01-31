package com.example.trade.web.portal.controller;

import com.alibaba.fastjson.JSON;
import com.example.trade.common.model.DealActivity;
import com.example.trade.common.model.Goods;
import com.example.trade.common.model.Order;
import com.example.trade.common.model.TradeResultDTO;
import com.example.trade.common.utils.RedisWorker;
import com.example.trade.web.portal.client.DealFeignClient;
import com.example.trade.web.portal.client.GoodsFeignClient;
import com.example.trade.web.portal.client.OrderFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class DealController {
    @Autowired
    OrderFeignClient orderService;
    @Autowired
    GoodsFeignClient goodsService;
    @Autowired
    DealFeignClient dealActivityService;

    @Autowired
    RedisWorker redisWorker;
    @RequestMapping("/deal/{dealId}")
    public ResponseEntity<?> dealInfo(HttpSession session,
                                         @PathVariable long dealId) {
        if(session.getAttribute("userId")==null){
            log.error("You haven't logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        DealActivity dealActivity;
        String dealActivityInfo=redisWorker.getValueByKey("dealActivity:"+dealId);
        String dealStock=redisWorker.getValueByKey("stock:"+dealId);
        log.info("checking redis cache:{}","dealActivity:"+dealId);
        if(dealActivityInfo!=null&&dealStock!=null){
            //从redis查询到数据
            dealActivity = JSON.parseObject(dealActivityInfo, DealActivity.class);
            dealActivity.setAvailableStock(Integer.valueOf(dealStock));
            log.info("Hit the activity cache!:{}", dealActivityInfo);
        }else{
            dealActivity = dealActivityService.queryDealActivityById(dealId);
        }

        if (dealActivity == null) {
            log.error("no such a deal activity: {}", dealId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DealActivity doesn't exist");
        }
        log.info("dealId={}, dealActivity={}", dealId, JSON.toJSON(dealActivity));
        Goods goods;
        String goodsInfo=redisWorker.getValueByKey("dealActivity_goods:"+dealActivity.getGoodsId());
        if(goodsInfo!=null){
            goods=JSON.parseObject(goodsInfo,Goods.class);

            log.info("Hit the goods cache!{}",goodsInfo);
        }else {
            goods=goodsService.queryGoodsById(dealActivity.getGoodsId());
        }
        if (goods == null) {
            log.error("no such a goods id: {} for this deal activity id: {}", dealActivity.getGoodsId(), dealId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Goods doesn't exist");
        }
        Map<String,Object> resultMap=new HashMap();
        resultMap.put("dealActivity", dealActivity);
        resultMap.put("goods", goods);
        return ResponseEntity.ok(resultMap);
    }

    @RequestMapping("/deal/list")
    public ResponseEntity<?> dealList(HttpSession session){
        if(session.getAttribute("userId")==null){
            log.error("You haven't logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        List<DealActivity> dealActivities = dealActivityService.queryActivitysByStatus(1);
        return ResponseEntity.ok(dealActivities);
    }
    @RequestMapping("/deal/buy/{dealId}")
    public ResponseEntity<?> dealBuy(HttpSession session,
                                     @PathVariable long dealId,
                                     @RequestParam("num") int num) {
        if(session.getAttribute("userId")==null){
            log.error("You haven't logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        Long userId=((Number)session.getAttribute("userId")).longValue();
        TradeResultDTO<Order> res = dealActivityService.processDeal(userId, dealId,num);
        if(res.getCode()!=200){
            log.error(res.getErrorMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res.getErrorMessage());
        }else{
            log.info("You got it!");
            return ResponseEntity.ok(res.getData());
        }
    }
}
