package com.zenas.mall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.zenas.mall.mbg.mapper")
public class MybatisConfig {

}
