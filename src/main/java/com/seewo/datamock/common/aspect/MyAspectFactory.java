package com.seewo.datamock.common.aspect;

/**
 * @Author NianGao
 * @Date 2018/5/11.
 * @description aspect的简单工厂类
 */
public class MyAspectFactory {
    public static BaseAspect getAspect(String aspect) {
        if (aspect == null) {
            return null;
        }
        if (aspect.equalsIgnoreCase("ControllerAspect")) {
            return ControllerAspect.getInstance();
        }
        if (aspect.equalsIgnoreCase("interceptorAspect")) {
            return InterceptorAspect.getInstance();
        }
        return null;
    }

}
