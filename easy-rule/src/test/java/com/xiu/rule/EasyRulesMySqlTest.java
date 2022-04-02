package com.xiu.rule;

import com.google.common.collect.Lists;
import com.xiu.rule.config.BizRulesConfig;
import com.xiu.rule.enums.CommonStatusEnum;
import com.xiu.rule.enums.CompositeRuleTypeEnum;
import com.xiu.rule.pojo.domain.BizRuleComposePo;
import com.xiu.rule.pojo.domain.BizRulePo;
import com.xiu.rule.pojo.param.RuleParam;
import com.xiu.rule.service.AuditService;
import com.xiu.rule.service.BizRuleComposeService;
import com.xiu.rule.service.BizRuleService;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import com.xiu.rule.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: Mr.xiu
 * @Description:
 * @Date: 2021/7/30 15:46
 */
@Slf4j
public class EasyRulesMySqlTest extends BaseTest {

  @Resource
  private BizRuleService bizRuleService;

  @Resource
  private BizRuleComposeService bizRuleComposeService;

  @Resource
  private BizRulesConfig bizRulesConfig;

  final String RULE_NAME ="审核规则";

  final String RE_AUDIT_STAGE = "2";

  @Test
  public void createRuleTest(){
    BizRulePo bizRulePo = new BizRulePo()
        .setName(RULE_NAME)
        .setDescription("审核规则测试用例,组合方式为XOR")
        .setCompositeType(CompositeRuleTypeEnum.XOR.getCode())
        .setPriority(1)
        .setState(CommonStatusEnum.VALID.getCode())
        .setCreateTime(new Date());

    Integer flag = bizRuleService.insert(bizRulePo);
    log.info("新增一条规则[{}],插入结果[{}]",bizRulePo.getName(),flag);
    Assert.assertNotNull(flag);
  }

  @Test
  public void createComposeRule(){
    List<BizRuleComposePo> dataList = Lists.newArrayList();
    //加急审核
    BizRuleComposePo ruleComposePo = new BizRuleComposePo()
        .setRuleId(2L)
        .setName("加急审核")
        .setDescription("北京复审")
        .setCondition(" ('110000').equals(param.cityCode) and '2'.equals(param.businessCode)")
        .setActions("auditService.urgentAudit(param);")
        .setPriority(1)
        .setState(CommonStatusEnum.VALID.getCode())
        .setCreateTime(new Date());
    dataList.add(ruleComposePo);
    //初审
     ruleComposePo = new BizRuleComposePo()
        .setRuleId(2L)
         .setName("初审")
        .setDescription("初审")
        .setCondition("'1'.equals(param.businessCode)")
        .setActions("auditService.preAudit(param);")
        .setPriority(2)
        .setState(CommonStatusEnum.VALID.getCode())
        .setCreateTime(new Date());
    dataList.add(ruleComposePo);
    //复审
    ruleComposePo = new BizRuleComposePo()
        .setRuleId(2L)
        .setName("复审")
        .setDescription("复审")
        .setCondition("'2'.equals(param.businessCode)")
        .setActions("auditService.reAudit(param);")
        .setPriority(3)
        .setState(CommonStatusEnum.VALID.getCode())
        .setCreateTime(new Date());
    dataList.add(ruleComposePo);
    Integer flag = bizRuleComposeService.batchInsert(dataList);
    log.info("批量插入组合规则[{}],插入结果[{}]",dataList.size(),flag);
    Assert.assertNotNull(flag);
  }

  @Test
  public void ruleCheckTest(){
    //上海复审
    RuleParam dto = RuleParam
        .builder()
        .businessCode(RE_AUDIT_STAGE)
        .cityCode("110000")
        .build();
    //定义事实
    Facts facts = new Facts();
    facts.put("param", dto);
    facts.put("auditService", AuditService.class);
    Rules rules = bizRulesConfig.fetchConfigRules(RULE_NAME);
    SpringContextUtil.getBean(RulesEngine.class).fire(rules, facts);
  }
}
