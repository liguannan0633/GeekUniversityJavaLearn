package com.xiu.rule.pojo.rules;

import com.xiu.rule.pojo.param.RuleParam;
import com.xiu.rule.service.AuditService;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

/**
 * @Author: Mr.xiu
 * @Description: 初审规则
 * @Date: 2021/1/13 19:54
 */
@Rule(name = "pre-audit", description = "初审", priority = 1)
@Slf4j
public class PreAuditRule {

  @Condition
  public boolean when(@Fact("param") RuleParam dto) {
    return Objects.equals(dto.getBusinessCode(), "1");
  }

  @Action(order = 1)
  public void action(@Fact("param") RuleParam dto) {
    log.info("这是初审单");
  }

  @Action(order = 2)
  public void action2(@Fact("param") RuleParam dto) {
    AuditService.preAudit(dto);
  }
}
