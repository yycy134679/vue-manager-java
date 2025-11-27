package com.example.config;

import com.example.security.LoginFailureHandler;
import com.example.security.LoginSuccessHandler;
import com.example.security.CaptchaFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Autowired
    CaptchaFilter captchaFilter;

    //白名单
    private static final String[] URL_WHITELIST={
            "/login",
            "/logout",
            "/captcha",
            "/favicon.ico"
    };

    protected void configure(HttpSecurity http) throws Exception{
        // 现状：原有配置顺序和缩进混乱，可读性较差
        // 目标：整理配置顺序、缩进，提升可读性和维护性

        http.cors()
            .and()
            .csrf().disable()
            // 表单登录配置
            .formLogin()
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler)
            .and()
            // 禁用 session
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // 配置拦截规则
            .authorizeRequests()
            .antMatchers(URL_WHITELIST).permitAll()
            .anyRequest().authenticated()

            //异常处理器

            // 配置自定义验证码过滤器
            .and()
            .addFilterBefore(captchaFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

    }
}
