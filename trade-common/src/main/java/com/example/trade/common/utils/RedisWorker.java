package com.example.trade.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class RedisWorker {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 向Redis中设置key-value值
     *
     * @param key
     * @param value
     */
    public void setValue(String key, String value) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.set(key, value);
        jedisClient.close();
    }

    /**
     * 根据key从Redis中获取对应的值
     *
     * @param key
     * @return
     */
    public String getValueByKey(String key) {
        Jedis jedisClient = jedisPool.getResource();
        String value = jedisClient.get(key);
        jedisClient.close();
        return value;
    }

    /**
     * 向Redis中设置key-value值
     *
     * @param key
     * @param value
     */
    public void setValue(String key, Long value) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.set(key, value.toString());
        jedisClient.close();
    }

    public void removeKey(String key){
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.del(key);
        jedisClient.close();
    }

    public List<String> findKeys(String prefix){
        List<String> res=new LinkedList<>();
        Jedis jedisClient = jedisPool.getResource();
        String cursor= ScanParams.SCAN_POINTER_START;
        ScanParams scanParams = new ScanParams().match(prefix+"*");
        do{
            ScanResult<String> scanResult = jedisClient.scan(cursor, scanParams);
            cursor = scanResult.getCursor();
            res.addAll(scanResult.getResult());
        }while (!cursor.equals(ScanParams.SCAN_POINTER_START));
        return res;
    }

    public List<String> findKeySlow(String prefix){
        List<String> res=new LinkedList<>();
        Jedis jedisClient = jedisPool.getResource();
        for(String key:jedisClient.keys(prefix+"*")){
            res.add(key);
        }
        jedisClient.close();
        return res;
    }

    /**
     * 通过Redis中Lua，判断库存和对库存进行扣减
     *
     * @param key
     * @return
     */
    public boolean stockDeductCheck(String key) {
        Jedis jedisClient = null;
        try {
            jedisClient = jedisPool.getResource();

            String script = "if redis.call('exists',KEYS[1]) == 1 then\n" +
                    "                 local availableStock = tonumber(redis.call('get', KEYS[1]))\n" +
                    "                 if( availableStock <=0 ) then\n" +
                    "                    return -1\n" +
                    "                 end;\n" +
                    "                 redis.call('decr',KEYS[1]);\n" +
                    "                 return availableStock - 1;\n" +
                    "             end;\n" +
                    "             return -1;";

            /*
             * 执行脚本
             * redis自从2.6.0版本起就采用内置的Lua解释器通过EVAL命令去执行脚本
             */
            long scriptResult = (Long) jedisClient.eval(script,
                    Collections.singletonList(key),
                    Collections.emptyList());

            if (scriptResult < 0) {
                log.info("很遗憾，库存不足,抢购失败");
                return false;
            } else {
                log.info("抢购成功，恭喜你");
            }
            return true;
        } catch (Exception e) {
            log.error("库存扣减异常:{}", e.getMessage());
            return false;
        } finally {
            if (jedisClient != null) {
                jedisClient.close();
            }
        }
    }

    public void stockRevert(Long dealActivityId){
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.incr("stock:"+dealActivityId);
        jedisClient.close();
    }
    public void addLimitMember(long dealActivityId, long userId) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.sadd("deal_activity_members:" + dealActivityId, String.valueOf(userId));
        jedisClient.close();
        log.info("添加限制购买名单 userId:{}  dealActivityId:{}  ", userId, dealActivityId);
    }

    /**
     * 移除限制购买的名单
     * redis srem 命令用于移除集合中的一个或多个成员元素，不存在的成员元素会被忽略
     *
     * @param dealActivityId
     * @param userId
     */
    public void removeLimitMember(long dealActivityId, long userId) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.srem("deal_activity_members:" + dealActivityId, String.valueOf(userId));
        jedisClient.close();
        log.info("移除限制购买名单 userId:{}  dealActivityId:{}  ", userId, dealActivityId);
    }

    public boolean isInLimitMember(long dealActivityId,long userId){
        Jedis jedisClient=jedisPool.getResource();
        boolean sismember=jedisClient.sismember("deal_activity_members:"+dealActivityId,String.valueOf(userId));
        jedisClient.close();
        log.info("Whether the member is in the purchase limitation list:{} userId:{} dealActivityId:{}",sismember,userId,dealActivityId);
        return sismember;
    }

}
