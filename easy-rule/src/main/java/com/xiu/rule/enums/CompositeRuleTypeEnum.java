package com.xiu.rule.enums;

import lombok.Getter;

/**
 * @Author: MR.xiu
 * @Description: 规则组合类型
 * @Date: 2020/3/11
 */
@Getter
public enum CompositeRuleTypeEnum {
  /**
   * 要么应用所有规则，要么不应用任何规则
   * 只要一个不符合，就都不执行了，就是要么都执行，要么都不执行
   */
  AND(1, "UnitRuleGroup"),

  /**
   * 组合规则 ActivationRuleGroup 只执行优先级最高的那个,如果满足就不在执行后面的了,如果不满足继续执行后面的,直到找到一个满足的规则就停止执行
   * 也就是说,只要找到一个符合的就执行找个规则的then,后面的就不看了
   */
  XOR(2, "ActivationRuleGroup"),

  /**
   * 找到优先级最高的，如果符合，然后找到其他的符合的rule并执行,如果不符合,直接结束,等所有条件都检查完毕之后再执行then操作
   */
  ALL(3, "ConditionalRuleGroup"),
  ;
  private Integer code;
  private String name;

  CompositeRuleTypeEnum(Integer code, String name) {
    this.code = code;
    this.name = name;
  }

  public static String getNameByCode(Integer code) {
    if (code != null) {
      for (CompositeRuleTypeEnum item : CompositeRuleTypeEnum.values()) {
        if (item.code.intValue() == code) {
          return item.name;
        }
      }
    }
    return null;
  }

  public  static CompositeRuleTypeEnum of(Integer code){
    if (code != null) {
      for (CompositeRuleTypeEnum item : CompositeRuleTypeEnum.values()) {
        if (item.code.intValue() == code) {
          return item;
        }
      }
    }
    return CompositeRuleTypeEnum.XOR;
  }
}
