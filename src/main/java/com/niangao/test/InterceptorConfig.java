package com.niangao.test;

import com.niangao.test2.TokenHandlerInterceptorAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author NianGao
 * @Date 2018/5/23.
 * @description
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    public InterceptorConfig() {
    }

    @Bean
    public TokenHandlerInterceptorAdapter getTokenHandler() {
        return new TokenHandlerInterceptorAdapter();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(this.getTokenHandler());
//        registry.addInterceptor(this.getTokenHandler());
        registration.addPathPatterns(new String[]{"/roomcenter/**"});
        super.addInterceptors(registry);
    }
}

