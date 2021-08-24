
package com.geek.learn.thread.lock;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class ReentrantLockDemo {

    public static void main(String[] args) {
        final Count count = new Count();

        for (int i = 0; i < 2; i++) {
            new Thread() {
                public void run() {
                    count.get();
                }
            }.start();

            for (int j = 0; j < 2; j++) {
                new Thread() {
                    public void run() {
                        count.put();
                    }
                }.start();
            }
        }


    }
}
