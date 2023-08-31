package com.example.users.config;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
@MapperScan("com.example.users.dao")
public class MyBatisConfig {

//    @Bean
//    public Configuration mybatisConfiguration() {
//        Configuration configuration = new Configuration();
//        configuration.setMapUnderscoreToCamelCase(true); // 设置命名转换策略
//        return configuration;
//    }
}

