package com.geek.learn.delay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author cube.li
 * @date 2021/9/22 18:02
 * @description 任务消息处理器
 */
@Component
@Slf4j
public class ExecuteTaskDelayMessageHandler implements DelayMessageHandler {

    @Override
    public void handle(DelayMessage message) {
        log.info("任务延时消息处理中,message={}", message);
        //message.getTask().getTask().run();
    }
}
