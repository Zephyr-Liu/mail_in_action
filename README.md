# 总结

> 作者文档地址：http://www.macrozheng.com/#/README

## 1.Mall实现generator自动生成model以及mapper.xml

> 总结: (2021.7.30)
>
> 1. 添加依赖
> 2. 修改application.yml配置
> 3. 添加Mybatis generator 配置文件 (注意路径配置和数据库配置)
> 4. 启动生成main函数



## 2.[mall整合Swagger-UI实现在线API文档](http://www.macrozheng.com/#/architect/mall_arch_02)

> 总结:(2021.8.1)
>
> 1. 数据库连接超时可能是未设置useSSL=flase 
>
> ```properties
> jdbc:mysql://localhost:3306/mall?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
> ```

- @Api：用于修饰Controller类，生成Controller相关文档信息
- @ApiOperation：用于修饰Controller类中的方法，生成接口方法相关文档信息
- @ApiParam：用于修饰接口中的参数，生成接口参数相关文档信息
- @ApiModelProperty：用于修饰实体类的属性，当实体类是请求参数或返回结果时，直接生成相关文档信息





## 3.mall实现redis缓存功能

> 总结:(2021.08.02)
>
> 1. 安装windows的redis并且添加项目依赖
> 2. 2修改SpringBoot配置文件
> 3. 添加RedisService接口用于定义一些常用Redis操作
> 4. 注入StringRedisTemplate，实现RedisService接口
> 5. 添加UmsMemberController
> 6. 添加UmsMemberService接口以及实现接口

