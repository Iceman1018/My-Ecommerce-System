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
     * @param dealActivityId
     * @param userId
     */
    public void addLimitMemeber(long dealActivityId,long userId){
        Jedis jedisClient=jedisPool.getResource();
        jedisClient.sadd("deal_activity_members:"+dealActivityId,String.valueOf(userId));
        jedisClient.close();
        log.info("Add a member to the purchase limitation list, userId:{} dealActivityId:{}",userId,dealActivityId);
    }

    /**
     * 移除限制购买的名单
     * redis srem移除元素
     * @param dealActivityId
     * @param userId
     */

    public void removeLimitMember(long dealActivityId,long userId){
        Jedis jedisClient=jedisPool.getResource();
        jedisClient.srem("deal_activity_members:"+dealActivityId,String.valueOf(userId));
        jedisClient.close();
        log.info("Remove a member from the purchase limitation list, userId:{} dealActivityId:{}",userId,dealActivityId);
    }
    /**
     * 判断是否在限制购买名单中
     * redis sismember 命令判断成员元素是否是集合的成员
     * @param dealActivityId
     * @param userId
     * @return
     */
    public boolean isInLimitMember(long dealActivityId,long userId){
        Jedis jedisClient=jedisPool.getResource();
        boolean sismember=jedisClient.sismember("deal_activity_members:"+dealActivityId,String.valueOf(userId));
        jedisClient.close();
        log.info("Whether the member is in the purchase limitation list:{} userId:{} dealActivityId:{}",sismember,userId,dealActivityId);
        return sismember;
    }
}
