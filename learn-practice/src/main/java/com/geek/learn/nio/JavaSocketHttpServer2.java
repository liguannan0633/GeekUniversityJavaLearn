package com.geek.learn.nio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author LiGuanNan
 * @date 2021/7/5 12:08 下午
 * 单线程
 * wrk压测情况:
 * 
 */
@Slf4j
public class JavaSocketHttpServer2 {

    public static void main(String[] args) throws IOException {

        Executor asyncExecutor = getAsyncExecutor();

        ServerSocket serverSocket = new ServerSocket(8802);
        while (true){
            log.info("自旋等待接受客户端请求");
            Socket accept = null;
            try {
                accept = serverSocket.accept();
                Socket finalAccept = accept;
                asyncExecutor.execute(()->{
                    doSomething(finalAccept);
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void doSomething(Socket socket) {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body = "hello nio";
            printWriter.println("Content-Length:" + body.getBytes().length);

            printWriter.println();

            printWriter.write(body);
            printWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 配置核心线程池数量
        taskExecutor.setCorePoolSize(4 * 2);
        // 配置最大线程池数量
        taskExecutor.setMaxPoolSize(4 * 4);
        /// 线程池所使用的缓冲队列
        taskExecutor.setQueueCapacity(2);
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
        log.info("线程池初始化......CorePoolSize:{}",4 * 2);
        return taskExecutor;
    }
}
