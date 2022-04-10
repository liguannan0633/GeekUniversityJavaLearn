package com.geek.learn.cache;

import com.github.benmanes.caffeine.cache.Cache;
import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class RedissonCaffeineCache extends AbstractValueAdaptingCache {
    private final Logger logger = LoggerFactory.getLogger(RedissonCaffeineCache.class);
    private String name;
    private RedissonClient redissonClient;
    private Cache<Object, Object> caffeineCache;
    private String cachePrefix;
    private long defaultExpiration = 0;
    private Map<String, Long> expires;
    private String topic = "cache:redis:caffeine:topic";
    protected RedissonCaffeineCache(boolean allowNullValues) {
        super(allowNullValues);
    }

    public RedissonCaffeineCache(String name, RedissonClient redissonClient, Cache<Object, Object> caffeineCache, CacheRedissonCaffeineProperties cacheRedisCaffeineProperties) {
        super(cacheRedisCaffeineProperties.isCacheNullValues());
        this.name = name;
        this.redissonClient = redissonClient;
        this.caffeineCache = caffeineCache;
        this.cachePrefix = cacheRedisCaffeineProperties.getCachePrefix();
        this.defaultExpiration = cacheRedisCaffeineProperties.getRedis().getDefaultExpiration();
        this.expires = cacheRedisCaffeineProperties.getRedis().getExpires();
        this.topic = cacheRedisCaffeineProperties.getRedis().getTopic();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = lookup(key);
        if(value != null) {
            return (T) value;
        }

        //缓存没有命中的时候,需要加锁再设置缓存值,防止并发时value覆盖
        RLock lock = redissonClient.getLock(getKey(key));
        lock.lock(1000,TimeUnit.MILLISECONDS);
        try {
            value = lookup(key);
            if(value != null) {
                return (T) value;
            }
            //执行业务逻辑,获取value
            value = valueLoader.call();
            Object storeValue = toStoreValue(value);
            put(key, storeValue);
            return (T) value;
        } catch (Exception e) {
            try {
                Class<?> c = Class.forName("org.springframework.cache.Cache$ValueRetrievalException");
                Constructor<?> constructor = c.getConstructor(Object.class, Callable.class, Throwable.class);
                RuntimeException exception = (RuntimeException) constructor.newInstance(key, valueLoader, e.getCause());
                throw exception;
            } catch (Exception e1) {
                throw new IllegalStateException(e1);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(Object key, Object value) {
        if (!super.isAllowNullValues() && value == null) {
            this.evict(key);
            return;
        }
        //往redis中放
        RBucket<Object> bucket = redissonClient.getBucket(getKey(key));
        long expire = getExpire();
        if(expire > 0) {
            bucket.set(toStoreValue(value), expire, TimeUnit.MILLISECONDS);
        } else {
            bucket.set(toStoreValue(value));
        }

        //发送redis订阅消息,通知删除各个分布式节点的本地缓存
        push(new CacheMessage(this.name, key));

        caffeineCache.put(key, toStoreValue(value));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        String cacheKey = getKey(key);
        Object prevValue = null;
        RLock lock = redissonClient.getLock(cacheKey);
        try {
            lock.tryLock(1000,TimeUnit.MILLISECONDS);
            RBucket<Object> bucket = redissonClient.getBucket(cacheKey);
            prevValue = bucket.get();
            if(prevValue == null) {
                long expire = getExpire();
                if(expire > 0) {
                    bucket.set(toStoreValue(value), expire, TimeUnit.MILLISECONDS);
                } else {
                    bucket.set(toStoreValue(value));
                }

                push(new CacheMessage(this.name, key));

                caffeineCache.put(key, toStoreValue(value));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return toValueWrapper(prevValue);
    }

    @Override
    public void evict(Object key) {
        // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
        redissonClient.getBucket(getKey(key)).delete();

        push(new CacheMessage(this.name, key));

        clearLocal(key);
    }

    /**
     * 清除当前分组下所有缓存
     */
    @Override
    public void clear() {
        // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
        RKeys keys1 = redissonClient.getKeys();
        Iterable<String> keysByPattern = keys1.getKeysByPattern(this.name.concat(":"));
        for(String key : keysByPattern) {
            redissonClient.getBucket(getKey(key)).delete();
            push(new CacheMessage(this.name, key));
            clearLocal(key);
        }
    }

    @Override
    protected Object lookup(Object key) {
        //拼接key
        String cacheKey = getKey(key);
        //先从本地缓存中获取value,在从redis中获取
        return caffeineCache.get(key, this::getByRedis);
    }

    private Object getByRedis(Object key) {
        //从redis中获取value
        Object value = redissonClient.getBucket(getKey(key)).get();
        if(value != null) {
            //将redis中的value放入本地缓存
            logger.info("get cache from redis and put in caffeine, the key is : {}", key);
            caffeineCache.put(key, value);
        }
        return value;
    }

    private String getKey(Object key) {
        String str = this.name.concat(":").concat(StringUtils.isEmpty(cachePrefix) ? key.toString() : cachePrefix.concat(":").concat(key.toString()));
        logger.info("============>key<==========:" + str);
        return str;
    }

    private long getExpire() {
        long expire = defaultExpiration;
        Long cacheNameExpire = expires.get(this.name);
        return cacheNameExpire == null ? expire : cacheNameExpire.longValue();
    }

    /**
     * @description 缓存变更时通知其他节点清理本地缓存
     * @param message
     */
    private void push(CacheMessage message) {
        RTopic topic = redissonClient.getTopic(this.topic);
        long clientsReceivedMessage = topic.publish(message);
        logger.info("delete first cache , {}个实例接收到信息", clientsReceivedMessage);
    }

    /**
     * @description 清理本地缓存
     * @param key
     */
    public void clearLocal(Object key) {
        logger.info("clear local cache, the key is : {}", key);
        if(key == null) {
            caffeineCache.invalidateAll();
        } else {
            caffeineCache.invalidate(key);
        }
    }
}