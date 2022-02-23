package com.geek.learn.delay;


import lombok.Getter;

/**
 * @author cube.li
 * @date 2021/9/22 15:44
 * @description 延迟消息类型
 */
@Getter
public enum DelayMessageType {

    DELETE_QR_CODE("删除二维码", QrCodeDelayMessageHandler.class),
    EXECUTE_TASK("执行任务", ExecuteTaskDelayMessageHandler.class);

    private final String desc;

    /**
     * 此延时消息的处理器
     */
    private final Class<? extends DelayMessageHandler> handler;

    DelayMessageType(String desc, Class<? extends DelayMessageHandler> handler) {
        this.desc = desc;
        this.handler = handler;
    }
}