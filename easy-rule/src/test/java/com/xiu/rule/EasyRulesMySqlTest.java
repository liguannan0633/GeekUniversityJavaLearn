package com.xiu.rule;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
import java.util.HashMap;
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

  @Test
  public void ruleCheckTest2(){
    //上海复审
    RuleParam dto = RuleParam
            .builder()
            .businessCode(RE_AUDIT_STAGE)
            .cityCode("110000")
            .build();
    //定义事实
    Facts facts = new Facts();
    facts.put("param", dto);
    Rules rules = bizRulesConfig.fetchConfigRules("test");
    SpringContextUtil.getBean(RulesEngine.class).fire(rules, facts);
  }


  @Test
  public void testCreateHuaxiangRuleTest(){
    BizRulePo bizRulePo1 = new BizRulePo()
            .setName("画像")
            .setDescription("画像用户orderCheckRule审查")
            .setCompositeType(CompositeRuleTypeEnum.XOR.getCode())
            .setPriority(10)
            .setState(CommonStatusEnum.VALID.getCode())
            .setCreateTime(new Date());

    BizRulePo bizRulePo2 = new BizRulePo()
            .setName("画像")
            .setDescription("画像用户courseTypeCheckRule审查")
            .setCompositeType(CompositeRuleTypeEnum.XOR.getCode())
            .setPriority(20)
            .setState(CommonStatusEnum.VALID.getCode())
            .setCreateTime(new Date());

    BizRulePo bizRulePo3 = new BizRulePo()
            .setName("画像")
            .setDescription("画像用户productLineCheckRule审查")
            .setCompositeType(CompositeRuleTypeEnum.XOR.getCode())
            .setPriority(30)
            .setState(CommonStatusEnum.VALID.getCode())
            .setCreateTime(new Date());

    Integer flag1 = bizRuleService.insert(bizRulePo1);
    log.info("新增一条规则[{}],插入结果[{}]",bizRulePo1.getName(),flag1);
    Integer flag2 = bizRuleService.insert(bizRulePo2);
    log.info("新增一条规则[{}],插入结果[{}]",bizRulePo2.getName(),flag2);
    Integer flag3 = bizRuleService.insert(bizRulePo3);
    log.info("新增一条规则[{}],插入结果[{}]",bizRulePo3.getName(),flag3);

    List<BizRulePo> rulePos = bizRuleService.selectByName("画像");

    //查询子规则
    List<BizRuleComposePo> dataList = Lists.newArrayList();
    BizRuleComposePo ruleComposePo1 = new BizRuleComposePo()
            .setRuleId(rulePos.get(0).getId())
            .setName("订单检查")
            .setDescription("订单检查")
            .setCondition("com.xiu.rule.util.SpringContextUtil.getBean('orderCheckRuleService').when(param)")
            .setActions("com.xiu.rule.util.SpringContextUtil.getBean('orderCheckRuleService').then(param);")
            .setPriority(0)
            .setState(CommonStatusEnum.VALID.getCode())
            .setCreateTime(new Date());
    dataList.add(ruleComposePo1);

    BizRuleComposePo ruleComposePo2 = new BizRuleComposePo()
            .setRuleId(rulePos.get(1).getId())
            .setName("课程类型检查")
            .setDescription("课程类型检查")
            .setCondition("com.xiu.rule.util.SpringContextUtil.getBean('courseTypeCheckRuleService').when(param)")
            .setActions("com.xiu.rule.util.SpringContextUtil.getBean('courseTypeCheckRuleService').then(param);")
            .setPriority(0)
            .setState(CommonStatusEnum.VALID.getCode())
            .setCreateTime(new Date());
    dataList.add(ruleComposePo2);

    BizRuleComposePo ruleComposePo3 = new BizRuleComposePo()
            .setRuleId(rulePos.get(2).getId())
            .setName("产品线检查")
            .setDescription("产品线检查")
            .setCondition("com.xiu.rule.util.SpringContextUtil.getBean('productLineRuleService').when(param)")
            .setActions("com.xiu.rule.util.SpringContextUtil.getBean('productLineRuleService').then(param);")
            .setPriority(0)
            .setState(CommonStatusEnum.VALID.getCode())
            .setCreateTime(new Date());
    dataList.add(ruleComposePo3);

    bizRuleComposeService.batchInsert(dataList);
  }


  @Test
  public void testHuaxiangRuleCheckTest(){
    //上海复审
    RuleParam dto = RuleParam
            .builder()
            .build();
    dto.getAtomicMap().set(new HashMap<>());
    dto.getAtomicMap().get().put("koolearnUserId",999);

    //定义事实
    Facts facts = new Facts();
    facts.put("param", dto);
    facts.put("koolearnUserId",999);
    facts.put("userTagType",1);
    facts.put("userTagSet",Sets.newHashSet());
    facts.put("productLineCodes", Sets.newHashSet());

    Rules rules = bizRulesConfig.fetchConfigRules("画像");

    SpringContextUtil.getBean(RulesEngine.class).fire(rules, facts);
  }
}
