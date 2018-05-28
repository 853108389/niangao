package com.niangao.intrumentation;

import com.niangao.common.Config;
import com.niangao.common.aspect.MyAspect;
import com.niangao.common.aspect.MyAspectFactory;
import com.niangao.http.utils.MyHttpUtils;

import java.lang.instrument.ClassFileTransformer;
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
     * @param agentArgs 外部传的参数字符串
     * @param inst      一个Instrumentation对象
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        Config.init();//初始化参数
        MyHttpUtils.getHttpClient();//初始化数据
        System.out.println("userInfo: " + Config.username + "__" + Config.password);
        System.out.println("project: " + Config.projectName + "  cat: " + Config.catName + "  col:  " + Config.colName);
        String level = "0";//默认打印等级
//        setArgs(agentArgs);
        //TODO:这里可以做外部参数配置
        MyAspect aspect = getAspect(level);
        inst.addTransformer(getControllerTransformer(aspect));//加载类   参数为true报错
    }

    //外部参数配置
    public static void setArgs(String agentArgs) {
        System.out.println(agentArgs);//打印传入的参数
        Arrays.stream(agentArgs.split("=")).forEach(a -> {
            String[] split = a.split("&");
            String key = split[0];
            String val = split[1];
        });
    }

    //选择切面
    private static MyAspect getAspect(String level) {
        MyAspect aspect = null;
        switch (level) {
            case "0":
                System.out.println("aspect: " + "ControllerAspect");
                aspect = MyAspectFactory.getAspect("ControllerAspect");
                break;
        }
        return aspect;
    }

    //选择转换器
    private static ClassFileTransformer getControllerTransformer(MyAspect aspect) {
        System.out.println("ClassFileTransformer: " + "ControllerTransformer");
        return new ControllerTransformer(aspect);
    }

}