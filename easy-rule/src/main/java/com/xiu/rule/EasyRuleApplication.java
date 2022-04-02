package com.xiu.rule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author: Mr.xiu
 * @Description: 主函数
 * @Date: 2020/12/9 15:27
 */
@SpringBootApplication
@MapperScan(basePackages = "com.xiu.rule.mapper")
@EnableAsync
public class EasyRuleApplication {

  public static void main(String[] args) {
    SpringApplication.run(EasyRuleApplication.class, args);
  }

}
