package com.seewo.datamock.test;

import com.seewo.datamock.common.weaving.utils.ConAdviceWeavingUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author NianGao
 * @Date 2018/5/18.
 * @description
 */
public class MyTrace extends HandlerInterceptorAdapter implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
//        System.out.println(Arrays.toString(methodParameter.getParameterAnnotations()));
//        System.out.println(methodParameter.getParameterName());
        System.out.println("-----------------------我是拦截-------------------------");
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
//        System.out.println(methodParameter.getAnnotatedElement());//public com.seewo.roomcenter.web.response.Response com.seewo.roomcenter.web.controller.RoomController.createRoom(com.seewo.roomcenter.web.request.vo.RoomCreateRequestVo,javax.servlet.http.HttpServletRequest)
//        System.out.println(methodParameter.getConstructor());//null
//        System.out.println(methodParameter.getContainingClass());//class com.seewo.roomcenter.web.controller.RoomController
//        System.out.println(methodParameter.getDeclaringClass());//class com.seewo.roomcenter.web.controller.RoomController
//        System.out.println(methodParameter.getGenericParameterType());//com.seewo.roomcenter.web.response.Response<com.seewo.roomcenter.web.response.vo.RoomCreateResponseVo>
//        System.out.println("====================================");
//        System.out.println(methodParameter.getMember());//public com.seewo.roomcenter.web.response.Response com.seewo.roomcenter.web.controller.RoomController.createRoom(com.seewo.roomcenter.web.request.vo.RoomCreateRequestVo,javax.servlet.http.HttpServletRequest)
//        System.out.println(Arrays.toString(methodParameter.getMethodAnnotations()));//[Ljava.lang.annotation.Annotation;@1adcf939
//        System.out.println(methodParameter.getParameterIndex());//-1
//        System.out.println(Arrays.toString(methodParameter.getParameterAnnotations()));//[]
//        System.out.println(methodParameter.getParameterName());//null
//        System.out.println("====================================");
//        System.out.println(methodParameter.getNestedGenericParameterType());//com.seewo.roomcenter.web.response.Response<com.seewo.roomcenter.web.response.vo.RoomCreateResponseVo>
//        System.out.println(methodParameter.getParameterType());//class com.seewo.roomcenter.web.response.Response
//        System.out.println(methodParameter.hasParameterAnnotation(ResponseBody.class));
//        System.out.println(methodParameter);//method 'createRoom' parameter -1
//        System.out.println("====================================");
//        System.out.println(methodParameter.getAnnotatedElement().);
//        System.out.println(Arrays.toString(methodParameter.getAnnotatedElement().getDeclaredAnnotations()));
//        System.out.println(methodParameter.getMember().getName());
//        System.out.println(methodParameter.getMember().getDeclaringClass());
        ServletServerHttpRequest sshr = (ServletServerHttpRequest) serverHttpRequest;
        HttpServletRequest request = sshr.getServletRequest(); //request对象
        ConAdviceWeavingUtils.doMock(request, o);
        return o;
    }
  /*
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
    */
}