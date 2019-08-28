package com.shizy.config.task;

import com.shizy.service.user.UserService;
import com.shizy.utils.format.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Configuration
public class ClearRedisCache {

    private static final Logger logger = LoggerFactory.getLogger(ClearRedisCache.class);

    private static List cacheKeys = new ArrayList();

    static {
        cacheKeys.add(UserService.cacheKey);
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 清理指定cacheKeys的redis缓存
     * 运行时间：每周一0点
     */
    @Scheduled(cron = "0 0 0 ? * MON")
    public synchronized void clearRedisCache() {

        logger.info("=============[TimerTask]===============");
        logger.info("- delete cache.");
        logger.info("- run time: " + DateUtil.getDate());
        logger.info("- clear keys: " + cacheKeys.toString());

        Long results = redisTemplate.delete(cacheKeys);

        logger.info("- clear results: " + results);
        logger.info("========================================");
    }


}




















