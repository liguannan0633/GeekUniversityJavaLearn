package com.xiu.rule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiu.rule.pojo.domain.BizRuleComposePo;
import java.util.List;

import com.xiu.rule.pojo.domain.BizRulePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: Mr.xiu
 * @Description:
 * @Date: 2021/1/8 16:00
 */
@Mapper
public interface BizRuleComposeMapper extends BaseMapper<BizRuleComposePo> {

  /**
   * 通过规则查
   *
   * @param ruleId 规则ID
   * @return 结果
   */
  List<BizRuleComposePo> selectByRule(
      @Param("ruleId") Long ruleId
  );

  int batchInsert(@Param("list") List<BizRuleComposePo> list);

}
