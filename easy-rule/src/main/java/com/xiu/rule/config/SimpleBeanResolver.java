package com.xiu.rule.config;

import org.springframework.context.ApplicationContext;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;

/**
 * @Author: Mr.xiu
 * @Description:
 * @Date: 2021/1/13 18:00
 */
public class SimpleBeanResolver implements BeanResolver {

  private ApplicationContext applicationContext;

  public SimpleBeanResolver(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public Object resolve(EvaluationContext context, String beanName) {
    return applicationContext.getBean(beanName);
  }

}
