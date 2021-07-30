package com.geek.learn.thread.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 验证interrupte之后,阻塞状态是立即被打断还是拿到锁之后再打断
 * 结论,立即被打断
 * 原因是:线程阻塞是通过LockSupport.park()方法实现的,而park()方法是可以被interrupt()方法打断的,当线程被中断,就会结束阻塞状态,doAcquireInterruptibly()方法执行到throw new InterruptedException();这一行
 * @author LiGuanNan
 * @date 2021/7/27 5:39 下午
 */
public class ReentrantInterapteLockDemo {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock(false);

        new Thread(()->{
            //没有加锁直接解锁会抛异常,所以,lock()放了try之外,防止加锁失败抛异常,进入finally代码块解锁失败
            lock.lock();
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println("释放锁: " + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*Thread thread = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("获取到锁: " + System.currentTimeMillis());
            }finally {
                lock.unlock();
            }
        });*/

        Thread thread = new Thread(() -> {
            try {
                lock.lockInterruptibly();
                System.out.println("获取到锁: " + System.currentTimeMillis());
                lock.unlock();
            } catch (InterruptedException e) {
                System.out.println("阻塞被中断..." + System.currentTimeMillis());
                e.printStackTrace();
            }
        });

        thread.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread.interrupt();
        System.out.println("外部中断..." + System.currentTimeMillis());
    }
}
