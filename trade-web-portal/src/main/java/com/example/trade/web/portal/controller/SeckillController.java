package com.example.trade.web.portal.controller;

import com.alibaba.fastjson.JSON;
import com.example.trade.goods.db.model.Goods;
import com.example.trade.goods.service.GoodsService;
import com.example.trade.lightningdeal.db.model.SeckillActivity;
import com.example.trade.lightningdeal.service.SeckillActivityService;
import com.example.trade.order.Service.OrderService;
import com.example.trade.order.db.model.Order;
import com.example.trade.order.utils.RedisWorker;
import com.example.trade.web.portal.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class SeckillController {
    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;
    @Autowired
    SeckillActivityService seckillActivityService;

    @Autowired
    RedisWorker redisWorker;
    @RequestMapping("/seckill/{seckillId}")
    public ResponseEntity<?> seckillInfo(HttpSession session,
                                         @PathVariable long seckillId) {
        if(session.getAttribute("userId")==null){
            log.error("You haven't logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        SeckillActivity seckillActivity;
        String seckillActivityInfo=redisWorker.getValueByKey("seckillActivity:"+seckillId);
        if(seckillActivityInfo!=null){
            //从redis查询到数据
            seckillActivity = JSON.parseObject(seckillActivityInfo, SeckillActivity.class);
            log.info("Hit the activity cache!:{}", seckillActivityInfo);
        }else{
            seckillActivity = seckillActivityService.querySeckillActivityById(seckillId);
        }

        if (seckillActivity == null) {
            log.error("no such a seckill activity: {}", seckillId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SeckillActivity doesn't exist");
        }
        log.info("seckillId={}, seckillActivity={}", seckillId, JSON.toJSON(seckillActivity));
        Goods goods;
        String goodsInfo=redisWorker.getValueByKey("seckillActivity_goods:"+seckillActivity.getGoodsId());
        if(goodsInfo!=null){
            goods=JSON.parseObject(goodsInfo,Goods.class);
            log.info("Hit the goods cache!{}",goodsInfo);
        }else {
            goods=goodsService.queryGoodsById(seckillActivity.getGoodsId());
        }
        if (goods == null) {
            log.error("no such a goods id: {} for this seckill activity id: {}", seckillActivity.getGoodsId(), seckillId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Goods doesn't exist");
        }
        Map<String,Object> resultMap=new HashMap();
        resultMap.put("seckillActivity", seckillActivity);
        resultMap.put("goods", goods);
        return ResponseEntity.ok(resultMap);
    }

    @RequestMapping("/seckill/list")
    public ResponseEntity<?> seckillList(HttpSession session){
        if(session.getAttribute("userId")==null){
            log.error("You haven't logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        List<SeckillActivity> seckillActivities = seckillActivityService.queryActivitysByStatus(1);
        return ResponseEntity.ok(seckillActivities);
    }
    @RequestMapping("/seckill/buy/{seckillId}")
    public ResponseEntity<?> seckillBuy(HttpSession session,
                                        @PathVariable long seckillId) {
        if(session.getAttribute("userId")==null){
            log.error("You haven't logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        Long userId=((Number)session.getAttribute("userId")).longValue();
        try {
            Order order = seckillActivityService.processSeckill(userId, seckillId);
            log.info("You got it!");
            return ResponseEntity.ok(order);
        }catch (Exception e)
        {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
