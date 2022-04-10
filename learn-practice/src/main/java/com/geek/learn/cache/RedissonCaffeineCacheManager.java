package com.geek.learn.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class RedissonCaffeineCacheManager implements CacheManager {

    private final Logger logger = LoggerFactory.getLogger(RedissonCaffeineCacheManager.class);

    private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();

    private CacheRedissonCaffeineProperties cacheRedisCaffeineProperties;

    private RedissonClient redissonClient;

    private boolean dynamic = true;

    private Set<String> cacheNames;

    public RedissonCaffeineCacheManager(CacheRedissonCaffeineProperties cacheRedisCaffeineProperties,
                                        RedissonClient redissonClient) {
        super();
        this.cacheRedisCaffeineProperties = cacheRedisCaffeineProperties;
        this.redissonClient = redissonClient;
        this.dynamic = cacheRedisCaffeineProperties.isDynamic();
        this.cacheNames = cacheRedisCaffeineProperties.getCacheNames();
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = cacheMap.get(name);
        if(cache != null) {
            return cache;
        }
        if(!dynamic && !cacheNames.contains(name)) {
            return cache;
        }

        cache = new RedissonCaffeineCache(name, redissonClient, caffeineCache(), cacheRedisCaffeineProperties);
        Cache oldCache = cacheMap.putIfAbsent(name, cache);
        logger.debug("create cache instance, the cache name is : {}", name);
        return oldCache == null ? cache : oldCache;
    }

    public com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache(){
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if(cacheRedisCaffeineProperties.getCaffeine().getExpireAfterAccess() > 0) {
            cacheBuilder.expireAfterAccess(cacheRedisCaffeineProperties.getCaffeine().getExpireAfterAccess(), TimeUnit.MILLISECONDS);
        }
        if(cacheRedisCaffeineProperties.getCaffeine().getExpireAfterWrite() > 0) {
            cacheBuilder.expireAfterWrite(cacheRedisCaffeineProperties.getCaffeine().getExpireAfterWrite(), TimeUnit.MILLISECONDS);
        }
        if(cacheRedisCaffeineProperties.getCaffeine().getInitialCapacity() > 0) {
            cacheBuilder.initialCapacity(cacheRedisCaffeineProperties.getCaffeine().getInitialCapacity());
        }
        if(cacheRedisCaffeineProperties.getCaffeine().getMaximumSize() > 0) {
            cacheBuilder.maximumSize(cacheRedisCaffeineProperties.getCaffeine().getMaximumSize());
        }
        if(cacheRedisCaffeineProperties.getCaffeine().getRefreshAfterWrite() > 0) {
            cacheBuilder.refreshAfterWrite(cacheRedisCaffeineProperties.getCaffeine().getRefreshAfterWrite(), TimeUnit.MILLISECONDS);
        }
        //设置弱引用
        cacheBuilder.weakValues();
        return cacheBuilder.build();
    }

    @Override
    public Collection<String> getCacheNames() {
        return this.cacheNames;
    }

    public void clearLocal(String cacheName, Object key) {
        Cache cache = cacheMap.get(cacheName);
        if(cache == null) {
            return ;
        }

        RedissonCaffeineCache redisCaffeineCache = (RedissonCaffeineCache) cache;
        redisCaffeineCache.clearLocal(key);
    }
}
