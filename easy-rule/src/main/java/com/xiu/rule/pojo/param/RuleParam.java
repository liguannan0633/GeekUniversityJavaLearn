package com.xiu.rule.pojo.param;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: Mr.xiu
 * @Description: 规则参数
 * @Date: 2021/1/13 16:46
 */
@Data
@Builder
public class RuleParam {

  public final AtomicReference<Map<Object, Object>> atomicMap = new AtomicReference<>();

  /**
   * 审核类型 1-初审 2-复审
   */
  private String businessCode;

  /**
   * 城市code
   */
  private String cityCode;

}
