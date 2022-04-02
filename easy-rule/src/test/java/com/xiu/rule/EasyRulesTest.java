package com.xiu.rule;

import com.xiu.rule.config.SimpleBeanResolver;
import com.xiu.rule.pojo.param.RuleParam;
import com.xiu.rule.pojo.rules.PreAuditRule;
import com.xiu.rule.pojo.rules.ReAuditRule;
import com.xiu.rule.pojo.rules.UrgentAuditRule;
import com.xiu.rule.service.AuditService;
import java.io.FileReader;
import java.util.Objects;

import com.xiu.rule.util.SpringContextUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.api.RulesEngineParameters;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.composite.ActivationRuleGroup;
import org.jeasy.rules.support.reader.JsonRuleDefinitionReader;
import org.jeasy.rules.support.reader.YamlRuleDefinitionReader;
import org.junit.Test;
import org.mvel2.ParserContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.expression.BeanResolver;

/**
 * @Author: Mr.xiu
 * @Description:
 * 1、审核规则
 *      1.1、businessCode =1 初审
 *      1.2、businessCode =2 复审
 *      1.3、cityCode = 110000 北京
 *      1.4、北京的复审是加急单
 * @Date: 2021/1/13 16:03
 */
@Slf4j
public class EasyRulesTest extends BaseTest {


  final String PRE_AUDIT_STAGE = "1";

  final String RE_AUDIT_STAGE = "2";

  /**
   * 表达式
   */
  @Test
  public void expression() {
    //初审
    Facts facts = new Facts();
    facts.put("businessCode", PRE_AUDIT_STAGE);

    //构建规则
    Rule preAuditRule = new RuleBuilder()
        .name("pre-audit")
        .description("初审规则")
        .when(vo -> vo.get("businessCode").equals(PRE_AUDIT_STAGE))
        .then(vo -> System.out.println("这是一个初审单"))
        .priority(1)
        .build();

    //规则注册
    Rules rules = new Rules();
    rules.register(preAuditRule);

    //规则启动
    RulesEngine rulesEngine = new DefaultRulesEngine();
    rulesEngine.fire(rules, facts);
  }

  /**
   * 复合规则，注解实现
   */
  @Test
  public void ruleCompose() {
    //复审北京
    RuleParam dto = RuleParam
        .builder()
        .businessCode(RE_AUDIT_STAGE)
        .cityCode("110000")
        .build();

    //事实定义
    Facts facts = new Facts();
    facts.put("param", dto);

    //组合规则 ActivationRuleGroup 逻辑异或 XOR
    ActivationRuleGroup ruleGroup = new ActivationRuleGroup("audit-rule", "审核规则");

    //组合规则 ActivationRuleGroup 按权重，执行所有规则， ALL
//    ConditionalRuleGroup ruleGroup = new ConditionalRuleGroup("audit-rule", "审核规则");

    //组合规则 ActivationRuleGroup 全部执行或者失败，AND
//    UnitRuleGroup ruleGroup = new UnitRuleGroup("audit-rule", "审核规则");

    ruleGroup.addRule(new PreAuditRule());
    ruleGroup.addRule(new ReAuditRule());
    ruleGroup.addRule(new UrgentAuditRule());

    //规则注册
    Rules rules = new Rules();
    rules.register(ruleGroup);
    //启动
    RulesEngine rulesEngine = new DefaultRulesEngine();
    rulesEngine.fire(rules, facts);
  }

  /**
   * yml SpEl表达式
   */
  @SneakyThrows
  @Test
  public void ymlSpElTest() {
    /*//上海复审
    RuleParam dto = RuleParam
        .builder()
        .businessCode(RE_AUDIT_STAGE)
        .cityCode("310000")
        .build();
    //事实参数
    Facts facts = new Facts();
    facts.put("param", dto);
    facts.put("auditService", AuditService.class);
    BeanResolver beanResolver = new SimpleBeanResolver(SpringContextUtil.getApplicationContext());
    //SpEL表达式加载
    SpELRuleFactory ruleFactory = new SpELRuleFactory(new YamlRuleDefinitionReader(), beanResolver);
    Rules yamlRules = ruleFactory
        .createRules(new FileReader(
            Objects.requireNonNull(SpringBootApplication.class.getClassLoader().getResource("audit-rules-spel.yml")).getFile())
        );

    //引擎
    DefaultRulesEngine rulesEngine = new DefaultRulesEngine();
    rulesEngine.fire(yamlRules, facts);*/
  }

  /**
   * yml MvEL表达式
   */
  @SneakyThrows
  @Test
  public void ymlMvElTest() {
    //北京复审-加急
    RuleParam dto = RuleParam
        .builder()
        .businessCode(RE_AUDIT_STAGE)
        .cityCode("110000")
        .build();
    //事实参数
    Facts facts = new Facts();
    facts.put("param", dto);
    //上下文加载
    ParserContext context = new ParserContext();
    context.addImport("auditService", AuditService.class);
    //MVEL表达式加载
    MVELRuleFactory ruleFactory = new MVELRuleFactory(new YamlRuleDefinitionReader(), context);
    Rules yamlRules = ruleFactory
              .createRules(
                  new FileReader(Objects.requireNonNull(SpringBootApplication.class.getClassLoader().getResource("audit-rules-mvel.yml")).getFile())
              );

    DefaultRulesEngine rulesEngine = new DefaultRulesEngine();
    rulesEngine.fire(yamlRules, facts);
  }

  //************************ easy rules 扩展特性 ***********************//

  /**
   * 引擎参数
   */
  @SneakyThrows
  @Test
  public void engineParamTest() {
    //北京复审-加急
    RuleParam dto = RuleParam
        .builder()
        .businessCode(RE_AUDIT_STAGE)
        .cityCode("110000")
        .build();
    //事实参数
    Facts facts = new Facts();
    facts.put("param", dto);
    //上下文加载
    ParserContext context = new ParserContext();
    context.addImport("auditService", AuditService.class);
    //MVEL表达式加载
    MVELRuleFactory ruleFactory = new MVELRuleFactory(new YamlRuleDefinitionReader(), context);
    Rules yamlRules = ruleFactory
        .createRules(
            new FileReader(Objects.requireNonNull(SpringBootApplication.class.getClassLoader().getResource("audit-rules-mvel.yml")).getFile())
        );
    //引擎
    RulesEngineParameters parameters = new RulesEngineParameters();
    //遇到成功规则，跳过后续规则
    parameters.skipOnFirstAppliedRule(true);
    //遇到失败规则，继续后续规则
//    parameters.skipOnFirstFailedRule(true);
    //遇到失败规则，跳过后续规则
//    parameters.skipOnFirstNonTriggeredRule(true);
    DefaultRulesEngine rulesEngine = new DefaultRulesEngine(parameters);
    rulesEngine.fire(yamlRules, facts);
  }


  /**
   * 规则引擎监听
   */
  @Test
  public void ruleListenerTest() throws Exception {
    //上海复审
    RuleParam dto = RuleParam
        .builder()
        .businessCode(RE_AUDIT_STAGE)
        .cityCode("310000")
        .build();
    //定义事实
    Facts facts = new Facts();
    facts.put("param", dto);
    facts.put("auditService", AuditService.class);

    MVELRuleFactory ruleFactory = new MVELRuleFactory(new JsonRuleDefinitionReader());

    Rules yamlRules = ruleFactory
        .createRules(new FileReader(
            Objects.requireNonNull(SpringBootApplication.class.getClassLoader().getResource("audit-rules-mvel.json")).getFile())
        );
    SpringContextUtil.getBean(RulesEngine.class).fire(yamlRules,facts);
  }
}
