package com.example.trade.order.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@Service
public class LimitBuyService {
    @Autowired
    private JedisPool jedisPool;
    /**
     * 添加限制购买的名单
     * redis sadd命令向set中添加元素
     * @param seckillActivityId
     * @param userId
     */
    public void addLimitMemeber(long seckillActivityId,long userId){
        Jedis jedisClient=jedisPool.getResource();
        jedisClient.sadd("seckill_activity_members:"+seckillActivityId,String.valueOf(userId));
        jedisClient.close();
        log.info("Add a member to the purchase limitation list, userId:{} seckillActivityId:{}",userId,seckillActivityId);
    }

    /**
     * 移除限制购买的名单
     * redis srem移除元素
     * @param seckillActivityId
     * @param userId
     */

    public void removeLimitMember(long seckillActivityId,long userId){
        Jedis jedisClient=jedisPool.getResource();
        jedisClient.srem("seckill_activity_members:"+seckillActivityId,String.valueOf(userId));
        jedisClient.close();
        log.info("Remove a member from the purchase limitation list, userId:{} seckillActivityId:{}",userId,seckillActivityId);
    }
    /**
     * 判断是否在限制购买名单中
     * redis sismember 命令判断成员元素是否是集合的成员
     * @param seckillActivityId
     * @param userId
     * @return
     */
    public boolean isInLimitMember(long seckillActivityId,long userId){
        Jedis jedisClient=jedisPool.getResource();
        boolean sismember=jedisClient.sismember("seckill_activity_members:"+seckillActivityId,String.valueOf(userId));
        jedisClient.close();
        log.info("Whether the member is in the purchase limitation list:{} userId:{} seckillActivityId:{}",sismember,userId,seckillActivityId);
        return sismember;
    }
}
