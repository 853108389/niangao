package com.seewo.datamock.test2;

import com.seewo.datamock.common.Config;
import com.seewo.datamock.common.utils.MyClassLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @Author NianGao
 * @Date 2018/5/18.
 * @description
 */
public class MyTrace extends HandlerInterceptorAdapter {
    @Bean
    public static HandlerInterceptorAdapter getWeavingHandler_() {
        MyClassLoader myClassLoader = new MyClassLoader();
        try {
            Class<?> uploadInterceptor = myClassLoader.findClass(Config.weavingInterceptorClassName);
            Object o = uploadInterceptor.newInstance();
            if (o instanceof HandlerInterceptorAdapter) {
                HandlerInterceptorAdapter weavingHandler = (HandlerInterceptorAdapter) uploadInterceptor.newInstance();
                System.out.println(o);
                return weavingHandler;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("拦截器加载异常.......");
        return null;
    }
}