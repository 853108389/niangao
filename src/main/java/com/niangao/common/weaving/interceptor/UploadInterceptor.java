package com.niangao.common.weaving.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author NianGao
 * @Date 2018/5/24.
 * @description
 */
public class UploadInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("r==========================我的interceptor==========================");
        System.out.println("request " + request);
        System.out.println("response " + response);
        System.out.println("r==========================我的interceptor==========================");
        return true;
    }

    public void test() {
        System.out.println("我是test");
    }
}
