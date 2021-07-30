package com.geek.learn;

/**
 * @author LiGuanNan
 * @date 2021/7/29 10:34 上午
 */
public class TestRetry {

    public static void main(String[] args) {
        test(3);
    }

    public static void test(int i) {
        retry:
        for (;;) {
            System.out.println("1");
            for (;;) {
                System.out.println("2");
                if (i == 1) {
                    return;//结束方法
                } else if (i == 2) {
                    break retry;//结束外层大循环
                } else if (i == 3) {
                    continue retry;//结束外层大循环的本次循环,进入下次循环
                }
            }
        }
        System.out.println("end");
    }

}
