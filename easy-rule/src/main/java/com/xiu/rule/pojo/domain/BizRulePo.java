package com.xiu.rule.pojo.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Mr.xiu
 * @Description: 业务规则
 * @Date: 2021/1/8 15:51
 */
@Data
@Accessors(chain = true)
@TableName("t_r_biz_rule")
public class BizRulePo implements Serializable {

  /**
   * 主键ID
   */
  private Long id;

  /**
   * 规则名称
   */
  private String name;

  /**
   * 描述
   */
  private String description;

  /**
   * 权重
   */
  private Integer priority;

  /**
   * 组合类型
   */
  private Integer compositeType;

  private Integer state;

  private Date createTime;

  private Date updateTime;
}
