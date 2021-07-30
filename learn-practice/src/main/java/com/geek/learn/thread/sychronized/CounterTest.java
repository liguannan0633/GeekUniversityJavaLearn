package com.geek.learn.thread.sychronized;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterTest {
    
    private AtomicInteger sum = new AtomicInteger();
    private Object lock = new Object();

    public void incr() {
        //synchronized(lock) {
            sum.incrementAndGet();
        //}
    }
    public int getSum() {
        return sum.intValue();
    }
    
    public static void main(String[] args) throws InterruptedException {
        int loop = 10_0000;
        
        // test single thread
        CounterTest counterTest = new CounterTest();
        for (int i = 0; i < loop; i++) {
            counterTest.incr();
        }

        System.out.println("single thread: " + counterTest.getSum());
    
        // test multiple threads
        final CounterTest counterTest2 = new CounterTest();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < loop / 2; i++) {
                counterTest2.incr();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < loop / 2; i++) {
                counterTest2.incr();
            }
        });
        t1.start();
        t2.start();
        Thread.sleep(1000);

        System.out.println("multiple threads: " + counterTest2.getSum());
    
    
    }
}
