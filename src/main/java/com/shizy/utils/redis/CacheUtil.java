package com.shizy.utils.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * @param <H>  hash本身的key的类型
 * @param <HK> hash中，每一行的key的类型
 * @param <HV> hash中，每一行的value的类型
 */
@Component
public class CacheUtil<H, HK, HV> {

    @Value("${redis.enable : false}")
    private boolean enable = false;

    @Autowired
    private RedisTemplate redisTemplate;

    /**********************************************/

    public HV getHash(H key, HK hashKey) {
        if (!enable) {
            return null;
        }
        return (HV) redisTemplate.opsForHash().get(key, hashKey);
    }

    public void putHash(H key, HK hashKey, HV value) {
        if (!enable) {
            return;
        }
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public void deleteHash(H key, Object... hashKeys) {
        if (!enable) {
            return;
        }
        redisTemplate.opsForHash().delete(key, hashKeys);
    }


}



























