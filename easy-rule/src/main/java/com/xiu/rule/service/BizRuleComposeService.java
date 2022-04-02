package com.xiu.rule.service;


import com.xiu.rule.mapper.BizRuleComposeMapper;
import com.xiu.rule.pojo.domain.BizRuleComposePo;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author: Mr.xiu
 * @Description: 规则组合
 * @Date: 2021/1/8 16:00
 */
@Service
@AllArgsConstructor
public class BizRuleComposeService {

  private BizRuleComposeMapper bizRuleComposeMapper;

  /**
   * 批量插入
   *
   * @param list 参数
   * @return 结果
   */
  public int batchInsert(List<BizRuleComposePo> list) {
    return bizRuleComposeMapper.batchInsert(list);
  }

  /**
   * 通过规则查
   *
   * @param ruleId 规则
   * @return 结果
   */
  public List<BizRuleComposePo> selectByRule(Long ruleId) {
    return bizRuleComposeMapper.selectByRule(ruleId);
  }

  /**
   * 更新
   *
   * @param bo 规则
   * @return 结果
   */
  public int update(BizRuleComposePo bo) {
    return bizRuleComposeMapper.updateById(bo);
  }
}
