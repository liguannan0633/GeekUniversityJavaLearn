package com.geek.learn.cache;

import com.geek.learn.utils.IpUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class CacheMessage implements Serializable {

    /** */
    private static final long serialVersionUID = 5987219310442078193L;

    private String cacheName;
    private Object key;
    private String fromIp;

    public CacheMessage(String cacheName, Object key) {
        super();
        this.cacheName = cacheName;
        this.key = key;
        //获取当前机器ip
        fromIp = IpUtil.getHostIp();
    }
}