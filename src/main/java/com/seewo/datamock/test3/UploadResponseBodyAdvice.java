package com.seewo.datamock.test3;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author NianGao
 * @Date 2018/5/29.
 * @description 获取返回值, Spring4.x以上特性 这里应该也可以获取请求值, 最终的植入目标
 */
@Order(1)
public class UploadResponseBodyAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        //获取当前处理请求的controller的方法
//        String methodName=methodParameter.getMethod().getName();
        // 不拦截/不需要处理返回值 的方法
//        String method= "loginCheck"; //如登录
        //不拦截
//        return !method.equals(methodName);
        System.out.println("植入成功......");
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
//        通过 ServerHttpRequest的实现类ServletServerHttpRequest 获得HttpServletRequest
        ServletServerHttpRequest sshr = (ServletServerHttpRequest) serverHttpRequest;
//        此处获取到request 是为了取到在拦截器里面设置的一个对象 是我项目需要,可以忽略
        HttpServletRequest request = sshr.getServletRequest(); //request对象
        System.out.println("返回值" + o);//返回值
        return null;
    }

}
