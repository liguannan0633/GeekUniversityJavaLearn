package com.geek.learn.thread;

import java.util.concurrent.CountDownLatch;

/**
 * 一个在阻塞状态下的线程,如果在其他线程中调用了他的interrupt方法,
 * 会自动抛出InterruptException异常同时会清除他的interrupt状态,
 * 也就是说此时在调用该线程的inInterrupted()方法,返回的结果就是false了而不是true
 *
 * 结论：
 * 阻塞状态和中断状态不能共存，如果并存会阻塞方法会抛出InterruptException，同时结束阻塞状态并且中断状态也会重置为非中断
 *
 * @author LiGuanNan
 * @date 2021/7/24 10:20 上午
 */
public class InterruptTest {
    static CountDownLatch countDownLatch = new CountDownLatch(1);

    static class MyThread2 implements Runnable {

        @Override
        public void run() {
            System.out.println("是否中断1" + Thread.currentThread().isInterrupted());

            try {
                Thread.sleep(10000);
                //countDownLatch.countDown();
            } catch (InterruptedException e) {
                System.out.println("抛出InterruptedException....");
            }

            //验证:修改抛出InterruptedExceptio后线程的中止标志
            System.out.println("是否中断2" + Thread.currentThread().isInterrupted());

            //验证：修改抛出InterruptedExceptio后线程的状态"
            System.out.println("线程状态2： " + Thread.currentThread().getState());
        }
    }

    static class MyThread1 implements Runnable {

        @Override
        public void run() {
            //验证:修改线程的中止标志
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("是否中断 " + Thread.currentThread().isInterrupted());
            }

            System.out.println("是否中断child1 " + Thread.currentThread().isInterrupted());

            //验证:让该线程在阻塞状态的时候抛出InterruptedException
            try {
                System.out.println("进入线程阻塞状态。。");
                Thread.sleep(3000);
                System.out.println(Thread.currentThread().getName() + "执行........");
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getState());
                System.out.println(Thread.currentThread().getName() + "结束线程阻塞状态");
            }

            //验证： 抛出InterruptedException之后，线程的是否中断结束
            System.out.println("是否中断child2 " + Thread.currentThread().isInterrupted());

        }
    }

    public static void main(String[] args) {
        test1();
    }

    /**
     * 运行结果：
     * 是否中断1false
     * 线程状态1： TIMED_WAITING
     * 是否中断3false
     * 抛出InterruptedException....
     * 是否中断2false
     * 线程状态2： RUNNABLE
     */
    public static void test2() {
        Thread t2 = new Thread(new MyThread2(), "Thread-2");
        t2.start();

        try {
            Thread.sleep(10);
            System.out.println("线程状态1： " + t2.getState());
            t2.interrupt();
            System.out.println("线程状态12： " + t2.getState());
            System.out.println("是否中断3" + t2.isInterrupted());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void test1() {
        Thread t1 = new Thread(new MyThread1(), "Thread-1");
        t1.start();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //线程的interrupt方法有两个作用,
        // 一是修改线程的中止标志,
        // 二是让该线程在阻塞状态的时候抛出InterruptedException,也就是说让是否中断的线程执行阻塞方法时抛异常
        t1.interrupt();
        System.out.println("main ......");
    }

}
