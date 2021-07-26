package com.geek.learn.thread;

import java.util.concurrent.CountDownLatch;

/**
 * @author LiGuanNan
 * @date 2021/7/25 10:07 下午
 */
public class CountDownLatchTest {

    static CountDownLatch countDownLatch = new CountDownLatch(7);

    public static void main(String[] args) {
        new Thread(()->{
            int i = 0;
            for(;i < 10;){
                countDownLatch.countDown();
                System.out.println(i++);
            }
        }).start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(11111);
    }
}
