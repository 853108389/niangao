package com.seewo.datamock.test;

import com.seewo.datamock.test2.TokenHandlerInterceptorAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

  /*  @Bean
    public HandlerInterceptorAdapter getUploadInterceptor() {
        HandlerInterceptorAdapter h = new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                System.out.println("r==========================我的interceptor==========================");
                System.out.println("request " + request);
                System.out.println("response " + response);
                System.out.println("r==========================我的interceptor==========================");
                return true;
            }
        };
        return h;
    }*/

//    public void addInterceptors(InterceptorRegistry registry) {
//        InterceptorRegistration registration = registry.addInterceptor(this.getTokenHandler());
////        registry.addInterceptor(this.getTokenHandler());
//        registration.addPathPatterns(new String[]{"/roomcenter/**"});
//        super.addInterceptors(registry);
//    }
}
