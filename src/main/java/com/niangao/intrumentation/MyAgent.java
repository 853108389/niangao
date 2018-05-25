package com.niangao.intrumentation;

import com.niangao.common.Config;
import com.niangao.common.aspect.MyAspect;
import com.niangao.common.aspect.MyAspectFactory;
import com.niangao.http.utils.MyHttpUtils;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;

/**
 * @Author NianGao
 * @Date 2018/5/8.
 * @description 代理启动时的入口, 类比Main
 */
public class MyAgent {

    /**
     * 该方法是一个类作为agent类必备的
     *
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        Arrays.stream(inst.getAllLoadedClasses()).forEach(aClass -> {
            if (aClass.getName().startsWith("com.niangao")) {
                System.out.println(aClass.getName());
            }
        });
        Config.init();//初始化参数
        MyHttpUtils.getHttpClient();//初始化数据 TODO:账号或者名称配置错误的话 要提示
        System.out.println(Config.username + "__" + Config.password);
        System.out.println("project: " + Config.projectName + "  cat: " + Config.catName + "  col:  " + Config.colName);
        String level = "0";//默认打印等级
//        setArgs(agentArgs);
        //TODO:这里可以做外部参数配置
        MyAspect aspect = getAspect(level);
        inst.addTransformer(new ControllerTransformer(aspect));//加载类   参数为true报错
    }

    //TODO;
    public static void setArgs(String agentArgs) {
        System.out.println(agentArgs);//打印传入的参数
        Arrays.stream(agentArgs.split(";")).map(a -> {
            String[] split = a.split("=");
            return split;
        });
    }

    private static MyAspect getAspect(String level) {
        MyAspect aspect = null;
        switch (level) {
            case "0":
                aspect = MyAspectFactory.getAspect("PrintUtilAspect");
                break;
        }
        return aspect;
    }

}