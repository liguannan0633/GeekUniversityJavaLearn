package com.xiu.rule.config;

import com.xiu.rule.listener.DefaultRulesListener;
import org.jeasy.rules.api.RuleListener;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.api.RulesEngineParameters;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Author: Mr.xiu
 * @Description:
 * @Date: 2021/7/1 18:18
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

  /**
   * 拦截器配置
   *
   * @param registry 拦截器
   */
 /* @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new ResponseResultInterceptor());
  }*/

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler("doc.html", "swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
      registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

  @Bean
  @ConditionalOnMissingBean
  public RuleListener defaultRulesListener(){
    return  new DefaultRulesListener();
  }


  @Bean
  @ConditionalOnMissingBean(RulesEngine.class)
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public RulesEngine rulesEngine(RuleListener defaultRulesListener) {
    RulesEngineParameters parameters = new RulesEngineParameters();
    DefaultRulesEngine rulesEngine = new DefaultRulesEngine(parameters);
    rulesEngine.registerRuleListener(defaultRulesListener);
    return rulesEngine;
  }
}
