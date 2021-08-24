
package com.geek.learn.thread.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockDemo2 {

    private final Map<String, Object> map = new HashMap<>();

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public Object readWrite(String key) {
        Object value = null;
        rwLock.readLock().lock();
        System.out.println("1.首先开启读锁去缓存中取数据");
        try {
            value = map.get(key);
            if (value == null) {
                rwLock.readLock().unlock();
                rwLock.writeLock().lock();
                System.out.println("2.数据不存在，则释放读锁，开启写锁");
                try {
                    if (value == null) {
                        value = "aaaa";
                        map.put(key,value);
                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } finally {
                    rwLock.writeLock().unlock();
                    System.out.println("3.释放写锁");
                }
                rwLock.readLock().lock();//因为上面已经释放了读锁,所以这里要在加上,不然finally代码块中释放读锁会报错
                System.out.println("4.开启读锁");
            }else {
                System.out.println("数据存在");
            }
        } finally {
            rwLock.readLock().unlock();
            System.out.println("5.释放读锁");
        }
        return value;
    }

    public static void main(String[] args) {
        ReentrantReadWriteLockDemo2 demo2 = new ReentrantReadWriteLockDemo2();

        new Thread(()->{
            //for (int i = 0 ; i < 100; i++){
                demo2.readWrite("bingfabiancheng" );
            //}
        }).start();

        new Thread(()->{
            //for (int i = 0 ; i < 100; i++){
                demo2.readWrite("bingfabiancheng");
            //}
        }).start();
    }

}
