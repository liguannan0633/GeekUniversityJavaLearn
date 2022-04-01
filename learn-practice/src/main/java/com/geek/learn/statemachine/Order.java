package com.geek.learn.statemachine;

import lombok.Data;

/**
 * @author LiGuanNan
 * @date 2022/3/31 2:02 下午
 */
@Data
public class Order {
    private int id;
    private OrderStatus status;

}
