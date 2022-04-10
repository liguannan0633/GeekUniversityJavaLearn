package com.geek.learn.cache;

import com.geek.learn.utils.IpUtil;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheMessageListener implements MessageListener<CacheMessage> {
    private final Logger logger = LoggerFactory.getLogger(CacheMessageListener.class);
    private RedissonClient redissonClient;
    private RedissonCaffeineCacheManager redissonCaffeineCacheManager;
    public CacheMessageListener(RedissonClient redissonClient,
                                RedissonCaffeineCacheManager redissonCaffeineCacheManager) {
        super();
        this.redissonClient = redissonClient;
        this.redissonCaffeineCacheManager = redissonCaffeineCacheManager;
    }

    @Override
    public void onMessage(CharSequence channel, CacheMessage cacheMessage) {
        if(IpUtil.getHostIp().equals(cacheMessage.getFromIp())){
            logger.info("this is my send, not need do");
            return;
        }
        logger.info("recevice a redis topic message, clear local cache, the cacheName is {}, the key is {}", cacheMessage.getCacheName(), cacheMessage.getKey());
        redissonCaffeineCacheManager.clearLocal(cacheMessage.getCacheName(), cacheMessage.getKey());
    }
}
