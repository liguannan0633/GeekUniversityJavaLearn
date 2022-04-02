package com.xiu.rule.pojo.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Mr.xiu
 * @Description: 规则组合
 * @Date: 2021/1/8 15:55
 */
@Data
@Accessors(chain = true)
@TableName("t_r_biz_rule_compose")
public class BizRuleComposePo implements Serializable {

  private Long id;

  /**
   * 规则
   */
  private Long ruleId;

  /**
   * 规则名称
   */
  private String name;

  private String description;

  /**
   * 权重
   */
  private Integer priority;

  /**
   * 条件
   */
  private String condition;

  /**
   * 执行动作
   */
  private String actions;

  private Integer state;

  private Date createTime;

  private Date updateTime;
}
