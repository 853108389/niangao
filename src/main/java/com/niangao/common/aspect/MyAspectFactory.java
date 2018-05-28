package com.niangao.common.aspect;

/**
 * @Author NianGao
 * @Date 2018/5/11.
 * @description TODO:根据配置文件来获得对应的aspect对象,如打印级别,然后工厂获得aspect
 */
public class MyAspectFactory {
    public static MyAspect getAspect(String aspect) {
        if (aspect == null) {
            return null;
        }
        if (aspect.equalsIgnoreCase("ControllerAspect")) {
            return ControllerAspect.getInstance();
        }
        return null;
    }

}
