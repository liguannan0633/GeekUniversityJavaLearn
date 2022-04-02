package com.xiu.rule.service;

import com.xiu.rule.enums.CompositeRuleTypeEnum;
import com.xiu.rule.helper.BizRuleHelper;
import com.xiu.rule.mapper.BizRuleMapper;
import com.xiu.rule.pojo.domain.BizRuleComposePo;
import com.xiu.rule.pojo.domain.BizRulePo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.support.RuleDefinition;
import org.jeasy.rules.support.composite.ActivationRuleGroup;
import org.jeasy.rules.support.composite.CompositeRule;
import org.jeasy.rules.support.composite.ConditionalRuleGroup;
import org.jeasy.rules.support.composite.UnitRuleGroup;
import org.mvel2.ParserContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @Author: Mr.xiu
 * @Description: 规则Service
 * @Date: 2021/1/8 16:00
 */
@Service
@AllArgsConstructor
@Slf4j
public class BizRuleService {

  private BizRuleMapper bizRuleMapper;

  private BizRuleComposeService composeService;


  /**
   * 新增数据
   *
   * @param po 参数
   * @return 结果
   */
  public int insert(BizRulePo po) {
    return bizRuleMapper.insert(po);
  }

  /**
   * 通过名称查询
   *
   * @param name 名称
   * @return 结果
   */
  public List<BizRulePo> selectByName(String name) {
    return bizRuleMapper.selectByName(name);
  }

  /**
   * 找出该组单个的规则
   *
   * @param groupCode 规则组
   * @return 结果
   */
  public CompositeRule ruleDefinition(String groupCode) {
    //规则
    List<BizRulePo> bizRuleList = this.selectByName(groupCode);
    if (CollectionUtils.isEmpty(bizRuleList)) {
      throw new RuntimeException("规则定义不存在");
    }
    CompositeRule compositeRule;
    BizRulePo bizRule = bizRuleList.get(0);
    switch (CompositeRuleTypeEnum.of(bizRule.getCompositeType())) {
      case AND:
        compositeRule = new UnitRuleGroup(bizRule.getName());
        break;
      case ALL:
        compositeRule = new ConditionalRuleGroup(bizRule.getName());
        break;
      default:
        compositeRule = new ActivationRuleGroup(bizRule.getName());
    }
    compositeRule.setDescription(bizRule.getDescription());
    compositeRule.setPriority(bizRule.getPriority());
    //规则组合数据
    List<BizRuleComposePo> bizRuleComposeList = composeService.selectByRule(bizRule.getId());
    CompositeRule finalCompositeRule = compositeRule;
    BizRuleHelper bizRuleHelper = new BizRuleHelper(new ParserContext());

    //规则定义
    bizRuleComposeList.forEach(bizRuleCompose -> {
      RuleDefinition ruleComposeDefinition = new RuleDefinition();
      ruleComposeDefinition.setName(bizRuleCompose.getName());
      BeanUtils.copyProperties(bizRuleCompose, ruleComposeDefinition);
      if (!StringUtils.isEmpty(bizRuleCompose.getActions())) {
        List<String> actions = Arrays.asList(bizRuleCompose.getActions().split(";"));
        ruleComposeDefinition.setActions(actions);
      }
      Rule rule = bizRuleHelper.createSimpleRule(ruleComposeDefinition);
      finalCompositeRule.addRule(rule);
    });
    return finalCompositeRule;
  }

  /**
   * 找出该组所有的规则
   *
   * @param groupCode 规则组
   * @return 结果
   */
  public List<CompositeRule> ruleDefinitions(String groupCode) {
    //规则
    List<BizRulePo> bizRuleList = this.selectByName(groupCode);
    if (CollectionUtils.isEmpty(bizRuleList)) {
      throw new RuntimeException("规则定义不存在");
    }

    List<CompositeRule> list = new ArrayList<>();
    bizRuleList.forEach(bizRule -> {
      CompositeRule compositeRule;
      switch (CompositeRuleTypeEnum.of(bizRule.getCompositeType())) {
        case AND:
          compositeRule = new UnitRuleGroup(bizRule.getName());
          break;
        case ALL:
          compositeRule = new ConditionalRuleGroup(bizRule.getName());
          break;
        default:
          compositeRule = new ActivationRuleGroup(bizRule.getName());
      }
      compositeRule.setDescription(bizRule.getDescription());
      compositeRule.setPriority(bizRule.getPriority());
      //规则组合数据
      List<BizRuleComposePo> bizRuleComposeList = composeService.selectByRule(bizRule.getId());
      CompositeRule finalCompositeRule = compositeRule;
      BizRuleHelper bizRuleHelper = new BizRuleHelper(new ParserContext());

      //规则定义
      bizRuleComposeList.forEach(bizRuleCompose -> {
        RuleDefinition ruleComposeDefinition = new RuleDefinition();
        ruleComposeDefinition.setName(bizRuleCompose.getName());
        BeanUtils.copyProperties(bizRuleCompose, ruleComposeDefinition);
        if (!StringUtils.isEmpty(bizRuleCompose.getActions())) {
          List<String> actions = Arrays.asList(bizRuleCompose.getActions().split(";"));
          ruleComposeDefinition.setActions(actions);
        }
        Rule rule = bizRuleHelper.createSimpleRule(ruleComposeDefinition);
        finalCompositeRule.addRule(rule);
      });
      list.add(finalCompositeRule);
    });
    return list;
  }
}
