package com.xiu.rule.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * mybatis-plus分页
 * @author chenzhongyong
 * @since 2018-08-10
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }

    /**
     * 默认自动填充配置属性值（如create_time，update_time,is_delete,ts等）
     *
     * 如果各个项目没有单独配置MetaObjectHandler，就用该默认handler
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                //insert时自动插入属性
                this.strictInsertFill(metaObject, "createTime", Date.class, Calendar.getInstance().getTime());
                this.strictInsertFill(metaObject, "updateTime", Date.class, Calendar.getInstance().getTime());
                this.strictInsertFill(metaObject, "ts", Date.class, Calendar.getInstance().getTime());
                this.strictInsertFill(metaObject, "isDelete", Integer.class, 0);
            }
            @Override
            public void updateFill(MetaObject metaObject) {
                this.strictUpdateFill(metaObject, "updateTime", Date.class, Calendar.getInstance().getTime());
                this.strictUpdateFill(metaObject, "ts", Date.class, Calendar.getInstance().getTime());
                // 默认提供的strictUpdateFill为有值不覆盖， gmtModified需要覆盖，利用通用塞值的方法填充
                this.setFieldValByName("updateTime", Calendar.getInstance().getTime(), metaObject);
                this.setFieldValByName("ts", Calendar.getInstance().getTime(), metaObject);
            }
        };
    }


}
