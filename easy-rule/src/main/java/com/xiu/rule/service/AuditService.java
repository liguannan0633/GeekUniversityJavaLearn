package com.xiu.rule.service;

import com.alibaba.fastjson.JSON;
import com.xiu.rule.pojo.param.RuleParam;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.springframework.stereotype.Service;

/**
 * @Author: Mr.xiu
 * @Description: 审核操作
 * @Date: 2021/1/5 15:46
 */
@Service
@Slf4j
public class AuditService {

  public void test(){
    System.out.println("====>执行微服务RPC接口调用");
  }

  /**
   * 初审操作
   *
   * @param param 参数
   */
  public static void preAudit(RuleParam param) {
    log.info("begin to deal Pre-audit...param:{}", JSON.toJSONString(param));
  }

  /**
   * 复审操作
   *
   * @param param 参数
   */
  public static void reAudit(RuleParam param) {
    log.info("begin to deal Re-audit...param:{}", JSON.toJSONString(param));
  }

  /**
   * 加急审核操作
   *
   * @param param 参数
   */
  public static void urgentAudit(RuleParam param) {
    log.info("begin to deal Urgent-audit...param:{}", JSON.toJSONString(param));
  }

  /**
   * 规则执行失败操作
   *
   * @param facts 参数
   */
  public void failDeal(Rule rule,Facts facts) {
    log.info("规则[{}]评估失败,将会执行补偿机制 ...",rule.getName());
  }
}