package com.niangao.test2;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @Author NianGao
 * @Date 2018/5/18.
 * @description
 */
public class MyTrace extends HandlerInterceptorAdapter {
    //    public void test1(String xxx, String yyyyyy) {
//        Map<String, String> map = new HashMap<>();
//        map.put("aaaaa", xxx);
//        map.put("bbbbbb", yyyyyy);
//    }
//    public void test2() {
//        List<String> list = new ArrayList<>();
//        list.add("aaaa");
//        list.add("bbb");
//        list.add(null);
//    }

    public static String inputValueFromReq() {
        Object obj = null;
        String aaa = "aaa";
        System.out.println(obj);
        obj = aaa;
        return aaa;
    }

    @Bean
    public TestInterceptorAdapter getTokenHandler() {
        return new TestInterceptorAdapter();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.getTokenHandler()).addPathPatterns("/roomcenter/**");
    }


    public static Object inputValueFromReq2() {
        return String.valueOf(123);
    }

//    public void test3() {
//
//        String methodName = "aaaaaaaaaaa";
//        List<String> methodList = new ArrayList<>();//url
//        List<String> urlList = new ArrayList<>();//请求方式
//        HashMap map = new HashMap();//方法名
//        WeavingUtils.doMock(null, null, null, null, null, null, null);
//    }
}
