package com.geek.learn.delay;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author cube.li
 * @date 2021/9/22 15:43
 * @description 延时消息
 */
@Getter
@Setter
@ToString
public class DelayMessage implements Serializable {
    private static final long serialVersionUID = 9006297630420423520L;

    /**
     * 内容
     */
    @NonNull
    private String body;

    /**
     * 具体任务
     */
    //private Task task;

    /**
     * 消息类型
     */
    @NonNull
    private DelayMessageType type;

    /**
     * 消息属性
     */
    @JsonIgnore
    @NonNull
    private DelayMessageProperties properties;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DelayMessage that = (DelayMessage) o;
        return Objects.equals(body, that.body) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(body, type);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DelayMessageProperties implements Serializable {

        private static final long serialVersionUID = 1240631950524432277L;
        /**
         * 过期时间单位
         */
        private TimeUnit timeUnit;

        /**
         * 时长,实际的过期时间为 timeUnit * expire
         */
        private long expire;

    }

    public void check() {
        Assert.notNull(this.body, "delay message must not be null");
        Assert.notNull(this.type, "delay message type must not be null");
        Assert.notNull(this.properties, "delay message properties must not be null");
    }
}