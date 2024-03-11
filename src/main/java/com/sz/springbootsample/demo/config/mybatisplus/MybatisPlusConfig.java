package com.sz.springbootsample.demo.config.mybatisplus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * MybatisPlus配置类
 *
 * @author Yanghj
 * @date 1/1/2020
 */
@Configuration
public class MybatisPlusConfig {

    /** 添加分页插件 */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 如果配置多个插件,切记分页最后添加interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // 如果有多数据源可以不配具体类型,否则都建议配上具体的DbType
        return interceptor;
    }

    /** 注入主键生成器 */
    @Bean
    public IKeyGenerator keyGenerator() {
        return new H2KeyGenerator();
    }
}
