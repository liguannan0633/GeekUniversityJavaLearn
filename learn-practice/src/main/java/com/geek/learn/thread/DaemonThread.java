package com.geek.learn.thread;

/**
 * 测试守护线程,在没有用户线程存活时,守护线程也就随jvm自动退出
 */
public class DaemonThread {
    
    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
                try {
                    Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                Thread t = Thread.currentThread();
                System.out.println("当前线程:" + t.getName());
        };
        Thread thread = new Thread(task);
        thread.setName("test-thread-1");
        thread.setDaemon(true);
        thread.start();

        Thread.sleep(5500);
    }
    
    
}
