package com.geek.learn.thread;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 线程一二三交替执行
 * @author LiGuanNan
 * @date 2021/7/24 8:43 下午
 */
public class WaitAndNotifyTest2 {
   static class B{
       public volatile int count = 0;
       private List<Object> list = new CopyOnWriteArrayList<>();

       public synchronized void consume(int mo){
           while (true){
               /*try {
                   Thread.sleep(10);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }*/

               if (count % 3 == mo) {
                   switch (mo){
                       case 0:
                           int i1 = 0;
                           System.out.println(Thread.currentThread().getName() + ":::" + i1++);
                           break;
                       case 1:
                           int i2 = 0;
                           System.out.println(Thread.currentThread().getName() + ":::" + i2++);
                           break;
                       case 2:
                           int i3 = 0;
                           System.out.println(Thread.currentThread().getName() + ":::" + i3++);
                           break;
                   }
                   count ++;
                   list.add(new Object());
               }else {
                   try {
                       this.wait();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
               this.notifyAll();
           }
       }
   }

    public static void main(String[] args) {
        B b = new B();

        Thread thread1 = new Thread(() -> {
            b.consume(0);
        }, "thread-1");

        Thread thread2 = new Thread(() -> {
            b.consume(1);
        }, "thread-2");

        Thread thread3 = new Thread(() -> {
            b.consume(2);
        }, "thread-3");

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
