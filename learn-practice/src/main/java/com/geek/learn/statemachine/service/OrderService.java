package com.geek.learn.statemachine.service;

import com.geek.learn.statemachine.Order;

import java.util.Map;
/**
 * @author LiGuanNan
 * @date 2022/3/31 2:06 下午
 */
public interface OrderService {
    //创建订单
    Order create();
    //发起支付
    Order pay(int id);
    //订单发货
    Order deliver(int id);
    //订单收货
    Order receive(int id);
    //获取所有订单信息
    Map<Integer, Order> getOrders();
}
