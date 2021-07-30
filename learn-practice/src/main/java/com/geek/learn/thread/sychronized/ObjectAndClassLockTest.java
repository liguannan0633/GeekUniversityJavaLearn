package com.geek.learn.thread.sychronized;

/**
 * 类锁和对象锁测试
 * 运行结果1和2交替出现，说明类锁和对象锁互不影响，
 * 线程1拿到了类锁，执行静态同步方法，不影响其他线程获取对象锁执行非静态同步方法
 * @author LiGuanNan
 * @date 2021/7/24 8:16 下午
 */
public class ObjectAndClassLockTest {
    static class B {
        //静态方法，上类锁，函数功能为连续打印1000个value值，调用时会传1，所以会打印1000个1
        synchronized public static void mB(String value) throws InterruptedException {
            for (int i = 0; i < 1000; i++) {
                System.out.print(value);
                Thread.sleep(100);
            }
        }

        public void mC(String value) throws InterruptedException {
            //修饰this上对象锁，函数功能也是连续打印1000个value值，调用时会传2，所以会打印1000个2
            synchronized (this) {
                for (int i = 0; i < 1000; i++) {
                    System.out.print(value);
                    Thread.sleep(100);
                }
            }
        }
    }

    public static void main(String[] args) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    B.mB("1");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        B b = new B();
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    b.mC("2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread2.start();

    }

}
