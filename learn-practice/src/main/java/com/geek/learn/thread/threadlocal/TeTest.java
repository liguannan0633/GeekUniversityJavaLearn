package com.geek.learn.thread.threadlocal;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author LiGuanNan
 * @date 2021/12/13 5:08 下午
 */
@Data
public class TeTest {

    ThreadPoolTaskExecutor threadPool = null;

    public static void main(String[] args) {
        ContextHolder.set("hello");

        TeTest teTest = new TeTest();
        teTest.setThreadPool(getAsyncExecutor());

        List<Integer> list = Lists.newArrayList(1,2,3,4);
        teTest.test(list);
        System.out.println(String.format("current thread : %s; local value: %s", Thread.currentThread().getName(), ContextHolder.get()));
        teTest.getThreadPool().shutdown();
    }

    @SneakyThrows
    public void test(List<Integer> valueList){
        List<Future<?>> results = new ArrayList<>();
        for(Integer value : valueList){
            //提交任务，并设置拷贝Context到子线程
            Future<?> taskResult = threadPool.submit(new BizTask(ContextHolder.get()));
            results.add(taskResult);
        }
        for(Future<?> result : results){
            result.get();//阻塞等待任务执行完成
        }
    }

    class BizTask<T> implements Callable<T> {
        private String session = null;

        public BizTask(String session) {
            this.session = session;
        }

        @Override
        public T call(){
            try {
                //ContextHolder.set(this.session);
                System.out.println(String.format("current thread : %s; local value: %s", Thread.currentThread().getName(), ContextHolder.get()));
                // 执行业务逻辑
                TimeUnit.SECONDS.sleep(2);
            } catch(Exception e){
                //log error
            } finally {
                if(Thread.currentThread().getName().startsWith("test-thread-pool")){
                    ContextHolder.remove(); // 清理 ThreadLocal 的上下文，避免线程复用时context互串
                }
            }
            return null;
        }
    }

    public static ThreadPoolTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 配置核心线程池数量
        taskExecutor.setCorePoolSize(1);
        // 配置最大线程池数量
        taskExecutor.setMaxPoolSize(2);
        /// 线程池所使用的缓冲队列
        taskExecutor.setQueueCapacity(0);
        // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        taskExecutor.setAwaitTerminationSeconds(60);
        // 空闲线程存活时间
        taskExecutor.setKeepAliveSeconds(60);
        // 等待任务在关机时完成--表明等待所有线程执行完
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 线程池名称前缀
        taskExecutor.setThreadNamePrefix("test-thread-pool-");
        // 线程池拒绝策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程池初始化
        taskExecutor.initialize();
        return taskExecutor;
    }
}
