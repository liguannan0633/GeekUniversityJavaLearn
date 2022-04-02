package com.xiu.rule.config;

import com.xiu.rule.service.BizRuleService;
import lombok.AllArgsConstructor;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.support.composite.CompositeRule;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

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
    List<CompositeRule> compositeRules = bizRuleService.ruleDefinitions(ruleName);
    if(CollectionUtils.isEmpty(compositeRules)){
      return null;
    }

    Rules rules = new Rules();
    compositeRules.forEach(rules::register);
    return rules;
  }
}
