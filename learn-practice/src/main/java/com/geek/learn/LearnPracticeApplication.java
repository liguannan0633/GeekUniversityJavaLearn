package com.geek.learn;

import com.geek.learn.db.readwrite.DynamicDataSourceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@MapperScan("com.geek.learn.db.readwrite.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})//因为数据源是自己生成的，所以要去掉原先springboot启动时候自动装配的数据源配置。
@Import({DynamicDataSourceConfig.class})
public class LearnPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnPracticeApplication.class, args);
    }

}
