package com.example.trade.lightningdeal.service.impl;
import com.alibaba.fastjson.JSON;
import com.example.trade.goods.db.model.Goods;
import com.example.trade.goods.service.GoodsService;
import com.example.trade.lightningdeal.db.dao.SeckillActivityDao;
import com.example.trade.lightningdeal.db.model.SeckillActivity;
import com.example.trade.lightningdeal.service.SeckillActivityService;
import com.example.trade.order.utils.RedisWorker;
import com.example.trade.order.db.model.Order;
import com.example.trade.order.mq.OrderMessageSender;
import com.example.trade.order.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.trade.order.Service.LimitBuyService;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SeckillActivityServiceImpl implements SeckillActivityService {
    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Autowired
    private RedisWorker redisWorker;

    @Autowired
    private LimitBuyService limitBuyService;

    @Autowired
    private GoodsService goodsService;


    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(6, 8);

    @Autowired
    private OrderMessageSender orderMessageSender;
    @Override
    public boolean insertSeckillActivity(SeckillActivity seckillActivity) {
        if(seckillActivityDao.insertSeckillActivity(seckillActivity)) {
            String key="stock:" + seckillActivity.getId();
            System.out.println(key);
            redisWorker.setValue(key, new Long(seckillActivity.getAvailableStock()));

            return true;
        }
        return false;
    }

    @Override
    public SeckillActivity querySeckillActivityById(long id) {
        return seckillActivityDao.querySeckillActivityById(id);
    }

    @Override
    public List<SeckillActivity> queryActivitysByStatus(int status) {
        return seckillActivityDao.queryActivitiesByStatus(status);
    }

    @Override
    public boolean processSeckillReqBase(long seckillActivityId){
        SeckillActivity seckillActivity=seckillActivityDao.querySeckillActivityById(seckillActivityId);

        if(seckillActivity==null){
            log.error("seckActivityId={} No such seckill Activity",seckillActivityId);
            throw new RuntimeException("No such seckill Activity");
        }
        int availableStock=seckillActivity.getAvailableStock();
        if(availableStock>0){
            log.info("Purchase successfully");
            seckillActivityDao.updateAvailableStockByPrimaryKey(seckillActivityId);
            return true;
        }else{
            log.info("Sold out");
            return false;
        }
    }
    @Override
    public boolean processSeckillReqRedis(long seckillActivityId) {

        String key="stock:"+seckillActivityId;
        boolean checkResult=redisWorker.stockDeductCheck(key);
        if(!checkResult){
            return false;
        }
        SeckillActivity seckillActivity=seckillActivityDao.querySeckillActivityById(seckillActivityId);

        if(seckillActivity==null){
            log.error("seckActivityId={} No such seckill Activity",seckillActivityId);
            throw new RuntimeException("No such seckill Activity");
        }
        int availableStock=seckillActivity.getAvailableStock();
        if(availableStock>0){
            log.info("Purchase successfully");
            seckillActivityDao.updateAvailableStockByPrimaryKey(seckillActivityId);
            return true;
        }else{
            log.info("Sold out");
            return false;
        }

    }

    @Override
    public Order processSeckill(long userId, long seckillActivityId) {
        //1.校验用户是否有购买资格
        if (limitBuyService.isInLimitMember(seckillActivityId, userId)) {
            log.error("当前用户已经购买过不能重复购买 seckillActivityId={} userId={}", seckillActivityId, userId);
            throw new RuntimeException("当前用户已经购买过,不能重复购买");
        }
        //2.使用Redis中Lua先进行库存校验
        String key="stock:"+seckillActivityId;
        boolean checkResult=redisWorker.stockDeductCheck(key);
        if(!checkResult){
            log.error("Out of stock, seckillActivityId={} userId={}", seckillActivityId, userId);
            throw new RuntimeException("Sold out, purchase failed");
        }
        //3.查询对应的秒杀活动信息
        SeckillActivity seckillActivity=seckillActivityDao.querySeckillActivityById(seckillActivityId);
        if(seckillActivity==null){
            log.error("seckActivityId={} No such seckill Activity",seckillActivityId);
            throw new RuntimeException("No such seckill Activity");
        }

        //4.锁定库存
        boolean lockStockRes = seckillActivityDao.lockStock(seckillActivityId);
        if (!lockStockRes) {
            log.info("Out of stock, seckillActivityId={} userId={}", seckillActivityId, userId);
            throw new RuntimeException("Sold out, purchase failed");
        }
        log.info("Purchase successfully, seckillActivityId={} userId={}", seckillActivityId, userId);

        Order order = new Order();
        order.setId(snowflakeIdWorker.nextId());
        order.setActivityId(seckillActivityId);
        //type=1表示秒杀活动
        order.setActivityType(1);
        order.setGoodsId(seckillActivity.getGoodsId());
        order.setPayPrice(seckillActivity.getSeckillPrice());
        order.setUserId(userId);
        /*
         * 状态:0,没有可用库存订单创建失败;1,已创建，等待付款;2 已支付,等待发货;99 订单关闭，超时未付款
         */
        order.setStatus(1);
        order.setCreateTime(new Date());

        //5.创建订单，发送创建订单消息
        orderMessageSender.sendCreateOrderMessage(JSON.toJSONString(order));
        return order;

    }

    @Override
    public boolean lockStock(long id) {
        log.info("Deal lock stock, seckillActivityId:{}", id);
        return seckillActivityDao.lockStock(id);
    }

    @Override
    public boolean deductStock(long id) {
        log.info("Deal deduct stock, seckillActivityId:{}", id);
        return seckillActivityDao.deductStock(id);
    }

    @Override
    public boolean revertStock(long id) {
        log.info("Deal revert stock, seckillActivityId:{}", id);
        return seckillActivityDao.revertStock(id);
    }

    @Override
    public void pushSeckillActivityInfoToCache(long id){
        SeckillActivity seckillActivity=seckillActivityDao.querySeckillActivityById(id);
        redisWorker.setValue("stock:"+id,Long.valueOf(seckillActivity.getAvailableStock()));
        redisWorker.setValue("seckillActivity:"+id,JSON.toJSONString(seckillActivity));
        Goods goods=goodsService.queryGoodsById(seckillActivity.getGoodsId());
        redisWorker.setValue("seckillActivity_goods:"+seckillActivity.getGoodsId(),JSON.toJSONString(goods));
    }
}
