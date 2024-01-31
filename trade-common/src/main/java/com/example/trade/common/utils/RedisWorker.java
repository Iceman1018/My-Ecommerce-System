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

    public void addItemToSet(String key,String item){
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.sadd(key,item);
        jedisClient.close();
    }

    public void removeItemFromSet(String key,String item){
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.srem(key,item);
        jedisClient.close();
    }

    public boolean isItemInSet(String key,String item){
        Jedis jedisClient = jedisPool.getResource();
        boolean res = jedisClient.sismember(key, item);
        jedisClient.close();
        return res;
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
     * @param
     * @return
     */
    public boolean stockDeductCheck(long dealActivityId,int num) {
        String key = "stock:" + dealActivityId;
        Jedis jedisClient = null;
        try {
            jedisClient = jedisPool.getResource();

            String script = "if redis.call('exists',KEYS[1]) == 1 then\n" +
                    "                 local availableStock = tonumber(redis.call('get', KEYS[1]))\n" +
                    "                 local num = tonumber(ARGV[1])\n"+
                    "                 if( availableStock < num ) then\n" +
                    "                    return -1\n" +
                    "                 end;\n" +
                    "                 redis.call('decrBy',KEYS[1],num);\n" +
                    "                 return availableStock - num;\n" +
                    "             end;\n" +
                    "             return -1;";
            long scriptResult = (Long) jedisClient.eval(script,1,key,String.valueOf(num));

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

    public void stockRevert(long dealActivityId,int num){
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.incrBy("stock:"+dealActivityId,num);
        jedisClient.close();
    }
    public boolean addLimit(long dealActivityId, long userId, int num,int limitNum) {
        Jedis jedisClient = jedisPool.getResource();
        try{
            String key = "deal_activity_members:" + dealActivityId;
            String luaScript =
                    "local key = KEYS[1]\n" +
                            "local userId = ARGV[1]\n" +
                            "local num = tonumber(ARGV[2])\n" +
                            "local limitNum = tonumber(ARGV[3])\n" +
                            "local currentValue = redis.call('hget', key, userId)\n" +
                            "\n" +
                            "if currentValue == false then\n" +
                            "    if num > limitNum then\n" +
                            "        return false\n" +
                            "    end\n" +
                            "    redis.call('hset', key, userId, tostring(num))\n" +
                            "    --redis.log(redis.LOG_NOTICE,\"success!\")\n"+
                            "else\n" +
                            "    currentValue = tonumber(currentValue)\n" +
                            "    if currentValue + num > limitNum then\n" +
                            "        --redis.log(redis.LOG_NOTICE,\"false!\")\n"+
                            "        return false\n" +
                            "    end\n" +
                            "    redis.call('hset', key, userId, tostring(currentValue + num))\n" +
                            "    --redis.log(redis.LOG_NOTICE,\"success!\")\n"+
                            "end\n" +
                            "--redis.log(redis.LOG_NOTICE,\"finally success!\")\n"+
                            "return true";

            long result = (long)jedisClient.eval(luaScript, 1, key, String.valueOf(userId), String.valueOf(num), String.valueOf(limitNum));
            log.info("redis有这些值:{}",jedisClient.hgetAll(key));
            log.info("增加限购记录是否成功:{}, userId:{}, dealId:{}",result==1,userId,dealActivityId);
            return result==1;
        }catch (Exception e){
            log.error("限购异常:{}", e.getMessage());
            return false;
        }finally {
            jedisClient.close();
        }
    }

    public void removeLimit(long dealActivityId, long userId,int num) {
        Jedis jedisClient = jedisPool.getResource();
        try{
            String key="deal_activity_members:" + dealActivityId;
            String luaScript=
                    "local key = KEYS[1]\n"+
                            "local userId = ARGV[1]\n"+
                            "local num = tonumber(ARGV[2])\n"+
                            "local currentValue = redis.call('hget', key, userId)\n"+
                            "if currentValue ~= false then\n"+
                            "    currentValue = tonumber(currentValue)\n"+
                            "    local newValue = currentValue >= num and (currentValue - num) or 0\n"+
                            "    redis.call('hset', key, userId, tostring(newValue))\n"+
                            "end";
            jedisClient.eval(luaScript,1,key,String.valueOf(userId),String.valueOf(num));
        }catch (Exception e){
            log.error("回退限购失败");
        }finally {
            jedisClient.close();
            log.info("增加用户购买额度 userId:{}  dealActivityId:{}  ", userId, dealActivityId);
        }
    }

}
