package com.seewo.datamock.intrumentation;

import com.seewo.datamock.common.Config;
import com.seewo.datamock.common.aspect.BaseAspect;
import com.seewo.datamock.common.aspect.MyAspectFactory;
import com.seewo.datamock.http.utils.MyHttpUtils;

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
//        setArgs(agentArgs);
        //TODO:这里可以做外部参数配置
        inst.addTransformer(getTransformer());//加载类   参数为true报错
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

    //选择转换器及切面
    private static ClassFileTransformer getTransformer() {
        BaseAspect aspect = MyAspectFactory.getAspect("ControllerAspect");
        System.out.println("ClassFileTransformer: " + "ClassTransformer");
        return new ClassTransformer(aspect);
    }

}