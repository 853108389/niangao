package com.seewo.datamock.common.weaving.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * @Author NianGao
 * @Date 2018/5/24.
 * @description 将此类编译后的Class文件放在/resources下 再启用拦截器增强即可
 */
public class UploadInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("========================================我的interceptor========================================");
        System.out.println("===========request===========");
        System.out.println("request.getContentType() " + request.getContentType());
        System.out.println("request.getRequestURL() " + request.getRequestURL());
        System.out.println("request.getMethod() " + request.getMethod());
        System.out.println("request.getQueryString() " + request.getQueryString());
        Enumeration<String> e = request.getHeaderNames();
        while (e.hasMoreElements()) {
            String headerName = (String) e.nextElement();
            String headValue = request.getHeader(headerName);
            System.out.println("header " + headerName + " __ " + headValue);
        }
        System.out.println("request.getParameterMap() " + request.getParameterMap());
        System.out.println("===========request===========");

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("===========response===========");
        System.out.println();
        System.out.println("========================================我的interceptor========================================");
        super.afterCompletion(request, response, handler, ex);
    }

    public void test() {
        System.out.println("我是test");
    }
}
