
package com.geek.learn.thread.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NewScheduledThreadExecutorDemo {

    public static void main(String[] args) {

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(16);

        for (int i = 0; i < 100; i++) {
            final int no = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("start:" + no);
                        Thread.sleep(1000L);
                        System.out.println("end:" + no);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            if(i > 50){
                // 10s后执行
                executorService.schedule(runnable, 20, TimeUnit.SECONDS);
            }else {
                // 5s后执行
                executorService.schedule(runnable, 5, TimeUnit.SECONDS);
            }
            System.out.println("xxx" + no);
        }
        executorService.shutdown();
        System.out.println("Main Thread End!");


    }

}
