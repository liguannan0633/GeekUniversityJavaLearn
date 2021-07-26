package com.geek.learn.thread;

/**
 * @author LiGuanNan
 * @date 2021/7/24 8:20 下午
 */
public class WaitAndNotifyTest1 {

    static class A{
        private int MAX_COUNT = 20;
        private volatile int productCount = 0;

        public synchronized void produce(){
            int i = 0;
            while (i < 100){
                System.out.println(Thread.currentThread().getName() + "库存总量：" + productCount);

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(productCount >= MAX_COUNT){
                    System.out.println(Thread.currentThread().getName() + ":库存已满，不用生产了");
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    //System.out.println(Thread.currentThread().getName() + ":::" + i++);
                    productCount ++;
                }

                //虽然每次+1都唤醒其他线程，但是因为该方法是synchronized修饰的，只有当前线程释放锁了别的线程才有机会执行
                notifyAll();
            }
        }

        public synchronized void cosume(){
            int i = 0;
            while (i < 100){
                System.out.println(Thread.currentThread().getName() + ":库存总量：" + productCount);

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(productCount <= 0){
                    System.out.println(Thread.currentThread().getName() + ":库存已空，无法消费");
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    //System.out.println(Thread.currentThread().getName() + ":::" + i++);
                    productCount --;
                }

                //虽然每次+1都唤醒其他线程，但是因为该方法是synchronized修饰的，只有当前线程释放锁了别的线程才有机会执行
                notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        A a = new A();

        Thread thread1 = new Thread(() -> {
            a.produce();
        }, "thread-1");

        Thread thread2 = new Thread(() -> {
            a.cosume();
        }, "thread-2");

        Thread thread3 = new Thread(() -> {
            a.cosume();
        }, "thread-3");

        thread1.start();
        thread2.start();
        //thread3.start();
    }
}
