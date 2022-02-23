package com.geek.learn.delay;


import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author cube.li
 * @date 2021/9/22 16:00
 * @description 延时消息管理器
 */
public interface DelayMessageManager extends InitializingBean, DisposableBean {

    /**
     * 添加延时消息
     *
     * @param message 延时消息
     */
    void add(DelayMessage message);

    /**
     * 移除延时消息
     *
     * @param message 待移除的消息
     * @return 移除成功返回true, 移除失败返回false
     */
    boolean remove(DelayMessage message);
}
