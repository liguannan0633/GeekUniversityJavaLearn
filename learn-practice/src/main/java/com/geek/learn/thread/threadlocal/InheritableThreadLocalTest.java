package com.geek.learn.thread.threadlocal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

/**
 * @author LiGuanNan
 * @date 2021/12/13 3:49 下午
 */
@Slf4j
public class InheritableThreadLocalTest {
    ThreadLocal<Long> longLocal = new InheritableThreadLocal<Long>();
    ThreadLocal<String> stringLocal = new InheritableThreadLocal<String>();

    public void set() {
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }

    public long getLong() {
        return longLocal.get();
    }

    public String getString() {
        return stringLocal.get();
    }

    public void remove(){
        longLocal.remove();
        stringLocal.remove();
    }

    public static void main(String[] args) throws InterruptedException {
        final InheritableThreadLocalTest test = new InheritableThreadLocalTest();

        test.set();
        System.out.println(test.getLong());
        System.out.println(test.getString());

        ThreadPoolTaskExecutor asyncExecutor = getAsyncExecutor();
        asyncExecutor.submit(() -> {
            test.set();
            System.out.println(test.getLong());
            System.out.println(test.getString());

            TimeUnit.SECONDS.sleep(2);
            System.out.println("222222");

            return 99;
        });

        asyncExecutor.submit(() -> {
            //test.set();
            System.out.println(test.getLong());
            System.out.println(test.getString());

            TimeUnit.SECONDS.sleep(1);
            System.out.println("111111");

            return 99;
        });

        TimeUnit.SECONDS.sleep(5);

        /*Thread thread1 = new Thread(() -> {
            //test.set();
            System.out.println(test.getLong());
            System.out.println(test.getString());
        },"my-thread");
        thread1.start();
        thread1.join();*/

        System.out.println(test.getLong());
        System.out.println(test.getString());

        test.remove();
    }

    public static ThreadPoolTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 配置核心线程池数量
        taskExecutor.setCorePoolSize(1);
        // 配置最大线程池数量
        taskExecutor.setMaxPoolSize(1);
        /// 线程池所使用的缓冲队列
        taskExecutor.setQueueCapacity(500);
        // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        taskExecutor.setAwaitTerminationSeconds(60);
        // 空闲线程存活时间
        taskExecutor.setKeepAliveSeconds(60);
        // 等待任务在关机时完成--表明等待所有线程执行完
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 线程池名称前缀
        taskExecutor.setThreadNamePrefix("bscollege-thread-pool-");
        // 线程池拒绝策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程池初始化
        taskExecutor.initialize();
        log.info("线程池初始化......CorePoolSize:{}",8);
        return taskExecutor;
    }
}
