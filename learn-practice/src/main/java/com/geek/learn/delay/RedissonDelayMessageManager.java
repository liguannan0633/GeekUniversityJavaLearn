package com.geek.learn.delay;


import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author cube.li
 * @date 2021/10/11 20:59
 * @description 基于redisson实现的延时消息管理器
 */
@DependsOn("mySpringContextUtil")
@Component
@Slf4j
public class RedissonDelayMessageManager implements DelayMessageManager {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    private RBlockingQueue<DelayMessage> rBlockingQueue;

    private RDelayedQueue<DelayMessage> rDelayedQueue;

    private final Map<DelayMessageType, DelayMessageHandler> handlerMap = new ConcurrentHashMap<>(16);

    @PostConstruct
    private void init(){
        rBlockingQueue = redissonClient.getBlockingDeque("redisson-delay-message-queue");
        rDelayedQueue = redissonClient.getDelayedQueue(rBlockingQueue);
    }

    @Override
    public void add(DelayMessage message) {
        if (rDelayedQueue.contains(message)) {
            return;
        }
        log.info("redisson-delay-message-queue,add message = {}", message);
        rDelayedQueue.offer(message, message.getProperties().getExpire(), message.getProperties().getTimeUnit());
    }

    @Override
    public boolean remove(DelayMessage message) {
        return rDelayedQueue.remove(message);
    }

    @Override
    public void destroy() {
        rDelayedQueue.destroy();
    }

    @Override
    public void afterPropertiesSet() {
        Arrays.stream(DelayMessageType.values()).forEach(delayMessageType -> handlerMap.put(delayMessageType, MySpringContextUtil.getBean(delayMessageType.getHandler())));
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    DelayMessage delayMessage = rBlockingQueue.take();
                    log.info("redisson-delay-message-queue,consume message = {}", delayMessage);
                    taskExecutor.execute(()-> handlerMap.get(delayMessage.getType()).handle(delayMessage));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    ThreadUtil.safeSleep(1000);
                    afterPropertiesSet();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

}
