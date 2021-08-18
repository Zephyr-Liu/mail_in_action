package com.zenas.mall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"com.zenas.mall.mbg.mapper", "com.zenas.mall.dao"})
public class MybatisConfig {

}
