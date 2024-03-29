package com.example.trade.lightningdeal.service.impl;
import com.alibaba.fastjson.JSON;
import com.example.trade.common.model.Order;
import com.example.trade.common.utils.RedisWorker;
import com.example.trade.common.utils.SnowflakeIdWorker;
import com.example.trade.lightningdeal.db.dao.DealActivityRepository;
import com.example.trade.lightningdeal.db.model.DealActivity;
import com.example.trade.lightningdeal.mq.DealOrderMessageSender;
import com.example.trade.lightningdeal.service.DealActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class DealActivityServiceImpl implements DealActivityService {
    @Autowired
    private DealActivityRepository dealActivityDao;

    @Autowired
    private DealOrderMessageSender dealOrderMessageSender;

    @Autowired
    private RedisWorker redisWorker;

    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(6, 8);

    @Scheduled(fixedRate = 600000)
    public void updateDealStatus() {
        List<DealActivity> list=dealActivityDao.queryExpiredActivities(new Date());
        for(DealActivity dealActivity:list)
        {
            log.info("Found expired Activity id: {}",dealActivity.getId());
            if(dealActivityDao.updateDealActivityStatus(dealActivity.getId())>0) {
                int restStock = dealActivity.getAvailableStock() + dealActivity.getLockStock();
                dealOrderMessageSender.sendActivityExpirationMessage(dealActivity.getGoodsId() + ":" + restStock);
            }
        }
    }


    @Override
    public boolean insertDealActivity(DealActivity dealActivity) {
        if(dealActivityDao.save(dealActivity)!=null) {
            String key="stock:" + dealActivity.getId();
            System.out.println(key);
            redisWorker.setValue(key, new Long(dealActivity.getAvailableStock()));
            dealOrderMessageSender.sendCreateActivityMessage(dealActivity.getGoodsId()+":"+dealActivity.getAvailableStock());
            return true;
        }
        return false;
    }

    @Override
    public DealActivity queryDealActivityById(long id) {
        return dealActivityDao.findById(id).orElse(null);
    }

    @Override
    public List<DealActivity> queryActivitysByStatus(int status) {
        return dealActivityDao.queryActivitiesByStatus(status);
    }

    @Override
    public Order processDeal(long userId, long dealActivityId, int num) {
        //1.查询对应的秒杀活动信息
        DealActivity dealActivity=dealActivityDao.findById(dealActivityId).orElse(null);
        if(dealActivity==null){
            log.error("ActivityId={} No such deal Activity",dealActivityId);
            throw new RuntimeException("No such deal Activity");
        }
        if(dealActivity.getActivityStatus()==0){
            log.error("ActivityId={} deal activity OVER",dealActivityId);
            throw new RuntimeException("deal activity over");
        }
        //2.校验用户是否有购买资格
        if (!redisWorker.addLimit(dealActivityId, userId,num,dealActivity.getLimitPerUser())) {
            log.error("当前用户达到购买上限不能重复购买 dealActivityId={} userId={}", dealActivityId, userId);
            throw new RuntimeException("当前用户达到购买上限不能重复购买");
        }
        //3.使用Redis中Lua先进行库存校验
        boolean checkResult = redisWorker.stockDeductCheck(dealActivityId,num);
        if (!checkResult) {
            log.error("Out of stock, dealActivityId={} userId={}", dealActivityId, userId);
            redisWorker.removeLimit(dealActivityId,userId,num);
            throw new RuntimeException("Sold out, purchase failed");
        }
        //4.锁定库存
        boolean lockStockRes = dealActivityDao.lockStock(dealActivityId, num) > 0;
        if (!lockStockRes) {
            log.info("Out of stock, dealActivityId={} userId={}", dealActivityId, userId);
            redisWorker.removeLimit(dealActivityId,userId,num);
            redisWorker.stockRevert(dealActivityId,num);
            throw new RuntimeException("Sold out, purchase failed");
        }
        try {
            log.info("Purchase successfully, dealActivityId={} userId={}", dealActivityId, userId);

            Order order = new Order();
            order.setId(snowflakeIdWorker.nextId());
            order.setActivityId(dealActivityId);
            //type=1表示秒杀活动
            order.setActivityType(1);
            order.setGoodsId(dealActivity.getGoodsId());
            order.setGoodsNum(num);
            order.setPayPrice(dealActivity.getDealPrice());
            order.setUserId(userId);
            /*
             * 状态:0,没有可用库存订单创建失败;1,已创建，等待付款;2 已支付,等待发货;99 订单关闭，超时未付款
             */
            order.setStatus(1);
            order.setCreateTime(new Date());

            //5.创建订单，发送创建订单消息
            dealOrderMessageSender.sendCreateDealOrderMessage(JSON.toJSONString(order));
            return order;
        }catch (Exception e){
            log.error("订单发送失败:{}",e.getMessage());
            redisWorker.removeLimit(dealActivityId,userId,num);
            redisWorker.stockRevert(dealActivityId,num);
            throw new RuntimeException("Deal Order creation failed");
        }
    }

    @Override
    public boolean lockStock(long id,int num) {
        log.info("Deal lock stock, dealActivityId:{}", id);
        return dealActivityDao.lockStock(id,num)>0;
    }

    @Override
    public boolean deductStock(long id,int num) {
        log.info("Deal deduct stock, dealActivityId:{}", id);
        return dealActivityDao.deductStock(id,num)>0;
    }

    @Override
    public boolean revertStock(long id,int num) {
        log.info("Deal revert stock, dealActivityId:{}", id);
        return dealActivityDao.revertStock(id,num)>0;
    }


}
