package com.zenas.mall.config;

import com.zenas.mall.component.JwtAuthenticationTokenFilter;
import com.zenas.mall.component.RestAuthenticationEntryPoint;
import com.zenas.mall.component.RestfulAccessDeniedHandler;
import com.zenas.mall.dto.AdminUserDetails;
import com.zenas.mall.mbg.model.UmsAdmin;
import com.zenas.mall.mbg.model.UmsPermission;
import com.zenas.mall.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * @program: mall
 * @author: Zephyr
 * @create: 2021-08-02 20:10
 * SpringSecurity 的配置类
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UmsAdminService umsAdminService;
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    public void setUmsAdminService(UmsAdminService umsAdminService) {
        this.umsAdminService = umsAdminService;
    }

    @Autowired
    public void setRestfulAccessDeniedHandler(RestfulAccessDeniedHandler restfulAccessDeniedHandler) {
        this.restfulAccessDeniedHandler = restfulAccessDeniedHandler;
    }

    @Autowired
    public void setRestAuthenticationEntryPoint(RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //由于使用的是JWT,我们这里不需要csrf
        httpSecurity.csrf()
                .disable()
                .sessionManagement() //基于token，所以这里不需要session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //单独配置对swagger-ui的一切资源进行无授权访问
                .antMatchers("/v2/api-docs", "/swagger-resources/configuration/ui",
                        "/swagger-resources", "/swagger-resources/configuration/security",
                        "/swagger-ui.html", "/webjars/**")
                .permitAll()
                //允许对于网站静态资源的无授权访问
                .antMatchers(HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                )
                .permitAll()
                //对登录注册要允许匿名访问
                .antMatchers("/admin/login", "/admin/register")
                .permitAll()
                //跨域请求会先进行一次options请求
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                //.antMatchers("/**")  //测试时全部访问
                //.permitAll()
                //除上面外的所有请求全部需要鉴权认证
                .anyRequest()
                .authenticated();

        //禁用缓存
        httpSecurity.headers().cacheControl();
        // 添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);
    }


    /**
     * 配置UserDetailsService 和 PasswordEncoder
     *
     * @param auth
     * @throws Exception 异常
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    /**
     * SpringSecurity编码比对密码
     *
     * @return 对比
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录信息
        return username -> {
            UmsAdmin umsAdmin = umsAdminService.getAdminByUsername(username);
            if (umsAdmin != null) {
                List<UmsPermission> permissionsList = umsAdminService.getPermissionsList(umsAdmin.getId());
                return new AdminUserDetails(umsAdmin, permissionsList);
            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }

    /**
     * 在用户名和密码校验前添加的过滤器，如果有token信息，会自行根据token信息进行登录
     *
     * @return
     * @throws Exception
     */
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
