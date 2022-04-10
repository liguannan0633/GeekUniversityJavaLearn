package com.geek.learn.cache;

import java.util.Date;
import java.util.Random;

import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class CacheRedissonCaffeineService {

    @Autowired
    RedissonClient redissonClient;
    @Autowired
    RedissonCaffeineCacheManager redisCaffeineCacheManager;

    private final Logger logger = LoggerFactory.getLogger(CacheRedissonCaffeineService.class);

    @PostConstruct
    private void init(){

    }


    @Cacheable(key = "'cache_user_id_' + #id", value = "userIdCache", cacheManager = "cacheManager")
    public User get(Integer id) {
        logger.info("get by id from db");
        User user = new User();
        user.setId(id);
        user.setWxName("name" + id);
        user.setCreateTime(new Date());
        return user;
    }

    @Cacheable(key = "'cache_user_name_' + #name", value = "userNameCache", cacheManager = "cacheManager")
    public User get(String name) {
        logger.info("get by name from db");
        User user = new User();
        user.setId(new Random().nextInt());
        user.setWxName(name);
        user.setCreateTime(new Date());
        return user;
    }

    @CachePut(key = "'cache_user_id_' + #userVO.id", value = "userIdCache", cacheManager = "cacheManager")
    public User update(User userVO) {
        logger.info("update to db");
        userVO.setCreateTime(new Date());
        return userVO;
    }

    @CacheEvict(key = "'cache_user_id_' + #id", value = "userIdCache", cacheManager = "cacheManager")
    public void delete(long id) {
        logger.info("delete from db");
    }
}
