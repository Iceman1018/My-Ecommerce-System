package com.example.trade.lightningdeal.API;
import com.example.trade.common.model.Order;
import com.alibaba.fastjson.JSON;
import com.example.trade.lightningdeal.db.model.DealActivity;
import com.example.trade.lightningdeal.service.DealActivityService;
import com.example.trade.common.model.TradeResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
public class DealAPI {
    @Autowired
    private DealActivityService dealActivityService;

    /**
     * 插入一个秒杀活动
     *
     * @param dealActivity
     * @return
     */
    @PostMapping("/api/deal/insertDealActivity")
    @ResponseBody
    public boolean insertDealActivity(@RequestBody DealActivity dealActivity){
        log.info("insertDealActivity dealActivity:{}", JSON.toJSON(dealActivity));
        return dealActivityService.insertDealActivity(dealActivity);
    }
    /**
     * 查询秒杀活动
     *
     * @param id
     * @return
     */
    @GetMapping("/api/deal/queryDealActivityById")
    @ResponseBody
    public DealActivity queryDealActivityById(long id) {
        log.info("queryDealActivityById id:{}", id);
        return dealActivityService.queryDealActivityById(id);
    }

    @GetMapping("/api/deal/queryActivitysByStatus")
    @ResponseBody
    public List<DealActivity> queryActivitysByStatus(int status) {
        log.info("queryActivitysByStatus status:{}", status);
        return dealActivityService.queryActivitysByStatus(status);
    }


    /**
     * 处理秒杀请求
     *
     * @param userId
     * @param dealActivityId
     * @return
     */
    @GetMapping("/api/deal/processDeal")
    @ResponseBody
    public TradeResultDTO<Order> processDeal(long userId, long dealActivityId) {
        TradeResultDTO<Order> res = new TradeResultDTO<>();
        try {
            log.info("processDeal userId:{} dealActivityId:{}", userId, dealActivityId);
            Order order = dealActivityService.processDeal(userId, dealActivityId);
            res.setData(order);
            res.setCode(200);
        } catch (Exception ex) {
            res.setCode(500);
            res.setErrorMessage(ex.getMessage());
        }
        return res;

    }

    /**
     * 锁定商品的库存
     *
     * @param id
     * @return
     */
    @GetMapping("/api/deal/lockStock")
    @ResponseBody
    public boolean lockStock(long id) {
        log.info("lockStock id:{}", id);
        return dealActivityService.lockStock(id);
    }

    /**
     * 库存扣减
     *
     * @param id
     * @return
     */
    @GetMapping("/api/deal/deductStock")
    @ResponseBody
    public boolean deductStock(long id) {
        log.info("deductStock id:{}", id);
        return dealActivityService.deductStock(id);
    }

    /**
     * 锁定的库存回补
     *
     * @param id
     * @return
     */
    @GetMapping("/api/deal/revertStock")
    @ResponseBody
    public boolean revertStock(long id) {
        log.info("revertStock id:{}", id);
        return dealActivityService.revertStock(id);
    }

    /**
     * 缓存预热
     * 将秒杀信息写入Redis中
     *
     * @param id
     */

}

