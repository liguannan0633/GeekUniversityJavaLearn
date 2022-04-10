package com.geek.learn.cache;

import lombok.Data;

import java.util.Date;

/**
 * @author LiGuanNan
 * @date 2022/4/9 下午2:24
 */
@Data
public class User {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String appId;
    private String uid;
    private String unionId;
    private String openId;
    private String wxName;
    private String wxPhone;
    private String avatar;
    private Date createTime;
    private Date ts;
    private Short isDelete;
    private Integer isNew;
    private Integer channelId;
    private Integer appUserId;
}
