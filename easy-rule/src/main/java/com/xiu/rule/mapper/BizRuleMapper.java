package com.xiu.rule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiu.rule.pojo.domain.BizRulePo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: Mr.xiu
 * @Description:
 * @Date: 2021/1/8 15:59
 */
@Mapper
public interface BizRuleMapper extends BaseMapper<BizRulePo> {

  /**
   * 通过名称查询所有的
   *
   * @param name 名称
   * @return 结果
   */
  List<BizRulePo> selectByName(
      @Param("name") String name
  );

  int insert(BizRulePo bizRulePo);
}
