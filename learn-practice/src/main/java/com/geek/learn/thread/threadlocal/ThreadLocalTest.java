package com.geek.learn.thread.threadlocal;

/**
 * @author LiGuanNan
 * @date 2021/12/13 2:23 下午
 */
public class ThreadLocalTest {
    ThreadLocal<Long> longLocal = new ThreadLocal<Long>();
    ThreadLocal<String> stringLocal = new ThreadLocal<String>();

    public void set() {
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }

    public long getLong() {
        return longLocal.get();
    }

    public void remove(){
        longLocal.remove();
        stringLocal.remove();
    }

    public String getString() {
        return stringLocal.get();
    }

    public static void main(String[] args) throws InterruptedException {
        final ThreadLocalTest test = new ThreadLocalTest();

        test.set();
        System.out.println(test.getLong());
        System.out.println(test.getString());

        Thread thread1 = new Thread(() -> {
            //test.set();
            System.out.println(test.getLong());
            System.out.println(test.getString());
        },"my-thread");
        thread1.start();
        thread1.join();

        System.out.println(test.getLong());
        System.out.println(test.getString());

        test.remove();
    }
}
