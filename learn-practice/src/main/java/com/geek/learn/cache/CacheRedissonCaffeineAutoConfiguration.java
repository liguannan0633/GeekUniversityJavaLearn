package com.geek.learn.cache;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(CacheRedissonCaffeineProperties.class)
public class CacheRedissonCaffeineAutoConfiguration implements BeanPostProcessor {

    @Autowired
    private CacheRedissonCaffeineProperties cacheRedissonCaffeineProperties;


    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public RedissonCaffeineCacheManager cacheManager(RedissonClient redissonClient) {
        return new RedissonCaffeineCacheManager(cacheRedissonCaffeineProperties, redissonClient);
    }

    @Bean
    public CacheMessageListener cacheMessageListener(RedissonClient redissonClient,
                                                                       RedissonCaffeineCacheManager redisCaffeineCacheManager) {
        RTopic deleteTopic = redissonClient.getTopic(cacheRedissonCaffeineProperties.getRedis().getTopic());
        CacheMessageListener cacheMessageListener = new CacheMessageListener(redissonClient, redisCaffeineCacheManager);
        deleteTopic.addListener(CacheMessage.class,cacheMessageListener);
        return cacheMessageListener;
    }

}
