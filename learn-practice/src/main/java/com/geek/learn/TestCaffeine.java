package com.geek.learn;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/**
 * @author LiGuanNan
 * @date 2022/4/10 下午5:20
 */
public class TestCaffeine {

    public static void main(String[] args) {
        LoadingCache<String, Integer> cache = Caffeine.newBuilder()
                .maximumSize(1024 * 1024)
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .refreshAfterWrite(2, TimeUnit.SECONDS)
                .build((CacheLoader<String, Integer>) key -> {
                    System.out.println("加载缓存值");
                    return key.equals("test") ? 99 : 88;
                });

        Integer test = cache.get("test");
        System.out.println(test);

        test = cache.get("hahh");
        System.out.println(test);

        cache.put("test",77);

        test = cache.get("test");
        System.out.println(test);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //本次请求，将执行自动刷新，并立即返回旧值。
        test = cache.get("test");
        System.out.println("2s 后1， test ： " + test);

        test = cache.get("test");
        System.out.println("2s 后2， test ： " + test);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        test = cache.get("test");
        System.out.println("3s 后， test ： " + test);


        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //此时缓存已经删除，重新初始化
        test = cache.get("test");
        System.out.println("5s 后， test ： " + test);
    }
}
