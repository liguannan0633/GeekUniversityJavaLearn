package com.geek.learn.thread.threadlocal;

/**
 * @author LiGuanNan
 * @date 2021/12/13 4:38 下午
 */
public class ContextHolder {

    private static ThreadLocal<String> localThreadCache = new InheritableThreadLocal<>();

    public static void set(String cacheValue) {
        localThreadCache.set(cacheValue);
    }

    public static String get() {
        return localThreadCache.get();
    }

    public static void remove() {
        localThreadCache.remove();
    }

}
