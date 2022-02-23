package com.geek.learn.delay;



/**
 * @author cube.li
 * @date 2021/9/22 15:32
 * @description 延时消息处理器接口
 */
public interface DelayMessageHandler {

    /**
     * 处理消息
     *
     * @param message 消息
     */
    void handle(DelayMessage message);
}