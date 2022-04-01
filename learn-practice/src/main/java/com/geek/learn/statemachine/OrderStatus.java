package com.geek.learn.statemachine;

/**
 * @author LiGuanNan
 * @date 2022/3/31 2:02 下午
 */
public enum OrderStatus {
    // 待支付，待发货，待收货，已完成
    WAIT_PAYMENT, WAIT_DELIVER, WAIT_RECEIVE, FINISH;
}
