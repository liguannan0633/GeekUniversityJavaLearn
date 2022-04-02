package com.xiu.rule.enums;


/**
 * @author LiGuanNan
 * @date 2022/4/1 4:34 下午
 */
public enum CommonStatusEnum {
    VALID(1)
    ;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    CommonStatusEnum(int code) {
        this.code = code;
    }

}
