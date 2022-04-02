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
   * 它会触发第一个适用规则，而忽略该组中的其他规则（XOR逻辑）
   * 选择第一个，其他的就不执行了
   */
  XOR(2, "ActivationRuleGroup"),

  /**
   * 条件规则组是一个组合规则，其中具有最高优先级的规则作为条件：如果具有最高优先级的规则求值为true，则将触发其余规则。
   * 找到优先级最高的，如果符合，然后找到其他的符合的rule并执行
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
