package com.xiu.rule.config;

import com.xiu.rule.service.BizRuleService;
import lombok.AllArgsConstructor;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.support.composite.CompositeRule;
import org.springframework.stereotype.Component;

/**
 * @Author: Mr.xiu
 * @Description: 获取规则rules集合
 * @Date: 2021/7/28 16:24
 */

@Component
@AllArgsConstructor
public class BizRulesConfig {

  private BizRuleService bizRuleService;

  /**
   * 构建rules配置
   */
  public Rules fetchConfigRules(String ruleName) {

    CompositeRule compositeRule = bizRuleService.ruleDefinitions(ruleName);
    Rules rules = new Rules();
    rules.register(compositeRule);
    return rules;
  }
}
