
package com.geek.learn.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewFixedThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(16);
        for (int i = 0; i < 100; i++) {
            final int no = i;
            executorService.execute(() -> {
                try {
                    System.out.println("start:" + no);
                    Thread.sleep(1000L);
                    System.out.println("end:" + no);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();//shutdown方法不会立即停止线程池,而是等现有的任务处理完在停止
        System.out.println("Main Thread End!");
    }

}
