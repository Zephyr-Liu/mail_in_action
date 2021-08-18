# 总结

> 作者文档地址：http://www.macrozheng.com/#/README

## 1.Mall实现generator自动生成model以及mapper.xml

> 总结: (2021.7.30)
>
> 1. 添加依赖
> 2. 修改application.yml配置
> 3. 添加Mybatis generator 配置文件 (注意路径配置和数据库配置)
> 4. 启动生成main函数

1. 当遇到***.mapper出现问题的时候 应该是在生成新的时候没把已经生成的表配置没有注释 导致覆盖后报错

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



## 3. mall实现SpringSecurity和JWT实现认证和授权

> 总结:(2021.08.06)
>
> Spring Security 结合 Jwt：
> 1.创建JwtUtils工具类，用来生成jwt的token和token的验证刷新相关
> 2.创建SecurityConfig类，并配置注入security配置文件中(还有自定义 没有权限的时候返回结果)
> 3.创建DAO，Service，Controller实现接口
> 4.创建AuthenticationFilter类，实现token校验功能
>
> 5.修改Swagger的配置文件
>
> 6.给相关Controller增加权限

1. 无法解析占位符“jwt.secret”的字符串值“$ {} jwt.secret” 

   > ```java
   > @PropertySource(value = {"classpath:application.properties"})`，但我仍然得到错误说`Could not resolve placeholder 'jwt.secret' in string value
   > ```

解答：第一可能在application没有配置属性 第二可能配置写错了   还有一种可能性就是[Value注解](https://blog.csdn.net/hunan961/article/details/79206291)的使用错误 

```yaml
jwt: 
    header: Authorization 
    secret: my-very-secret-key 
```



2. 关于UmsAdminRoleRelationDao的自动注入失败 Could not autowire. No beans of 'UmsAdminRoleRelationDao' type found. 然后启动报null错误

> 提示:https://www.cnblogs.com/zzb-yp/p/11899880.html
>
> 是因为MappperScan注解没有添加Dao下面的Mapper接口
>
> 或者是在Appcalition.yml的mybatis的配置没有添加多个classpath
>
> 以上两个都是配置多个 所以检查这两个配置是某一个配置只配置了一个路径

3. 关于Spring Security的config的授权管理

> 贴文章:[Spring Security 访问控制的方式 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/365515573)
>
> 关于显示Knife4j文档请求异常 一般是因为没有放行资源

```java
// SecurityConfig类  
.antMatchers(HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs/**",
                        "/doc.html")
                .permitAll()
```

4.userDetails.getUsername() 输出的却是password？两者对换了

> 拿到token后放到jwt上面校验发现，sub不正确
>

最后检查到是

```java
//AdminUserDetails类
    @Override
    public String getPassword() {
        return umsAdmin.getPassword();
    }

    @Override
    public String getUsername() {
        return umsAdmin.getUsername();
    }
```

5. 已经登录后还是显示暂未登录或者token已经过期

> 第一：可能Authorize设置有问题 
>
> 在Swagger-ui文档中设置了Authorize 但是可能没有加空格？也可能加多了空格
>
> 玄学
>
> 正确格式: tokenHead<kbd> </kbd>token  
>
> 第二：设置完后还是没有成功 debug进入后发现进入到  if (jwtTokenUtil.validateToken(authToken, userDetails))方法中就执行失效了
>
> 改完以后运行成功

```java
//JwtAuthenticationTokenFilter类中
  @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(this.tokenHeader);
        LOGGER.info(authHeader);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            String authToken = authHeader.substring(this.tokenHead.length());
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            LOGGER.info("解析token获取到用户名:{}", username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                LOGGER.debug(String.valueOf(userDetails));
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    LOGGER.info("给用户授权:{}", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(request, response);
    }
```

