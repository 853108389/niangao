package com.niangao.common.utils;

import com.niangao.common.Config;
import com.niangao.common.aspect.MyAspect;
import com.niangao.common.myAdapter.ControllerAdapter;
import com.niangao.common.myAdapter.InterceptorAdapter;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.util.ASMifier;
import jdk.internal.org.objectweb.asm.util.TraceClassVisitor;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/**
 * @Author NianGao
 * @Date 2018/5/8.
 * @description 核心api的入口
 */
public class ControllerScanner {
//======================================================================================================================
//asm相关
//======================================================================================================================

    /**
     * @param pack       扫描包名
     * @param myAspect   指定增强切面
     * @param basePath   生成字节码路径
     * @param isbuildAll 是否生成所有的字节码,包括没增强的(默认不输出没增强字节码)
     * @param isParallel 是否采用并行处理
     */
    public static void genEnhanceClasses(String pack, MyAspect myAspect, String basePath, boolean isbuildAll, boolean isParallel) {
        Set<Class<?>> c = ClassTools.getClasses(pack)/*.stream().filter(a -> !a.getName().contains("$")).collect(Collectors.toSet())*/;
        if (isParallel) {
//            TODO:拦截器的增强
            c.stream().parallel().forEach(clazz -> ControllerScanner.enhanceController(clazz, myAspect, basePath, isbuildAll));// 如果使用并行处理
//            c.stream().parallel().forEach(clazz -> ControllerScanner.enhanceInterceptor(clazz, myAspect, basePath, isbuildAll));// 如果使用并行处理
        } else {
            c.forEach(clazz -> ControllerScanner.enhanceController(clazz, myAspect, basePath, isbuildAll));
//            c.stream().forEach(clazz -> ControllerScanner.enhanceInterceptor(clazz, myAspect, basePath, isbuildAll));
        }
    }

    public static void genEnhanceClasses(String pack, MyAspect myAspect, String basePath) {
        genEnhanceClasses(pack, myAspect, basePath, false, false);
    }

    public static void genEnhanceClasses(String pack, MyAspect myAspect, String basePath, boolean isbuildAll) {
        genEnhanceClasses(pack, myAspect, basePath, isbuildAll, false);
    }

    /**
     * 将某个controller的字节码进行转换并生成到磁盘
     *
     * @param clazz      需要增强类
     * @param myAspect   增强切面类
     * @param basePath   增强后的类字节码输出位置
     * @param isbuildAll 是否生成所有的字节码
     */
    private static void enhanceController(Class<?> clazz, MyAspect myAspect, String basePath, boolean isbuildAll) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        try {
            ClassReader classReader = new ClassReader(Type.getType(clazz).getInternalName());//分析类
            ControllerAdapter cv = new ControllerAdapter(classWriter, myAspect);//cv将所有事件转发给cw
            classReader.accept(cv, ClassReader.EXPAND_FRAMES);
            if (cv.isController() || isbuildAll) {
                writeToClass(classWriter, basePath + clazz.getName() + ".class");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void enhanceInterceptor(Class<?> clazz, MyAspect myAspect, String basePath, boolean isbuildAll) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        try {
            ClassReader classReader = new ClassReader(Type.getType(clazz).getInternalName());//分析类
            InterceptorAdapter cv = new InterceptorAdapter(classWriter, myAspect);//cv将所有事件转发给cw
            classReader.accept(cv, ClassReader.EXPAND_FRAMES);
            if (cv.isInterceptorConfig() || isbuildAll) {
                writeToClass(classWriter, basePath + clazz.getName() + ".class");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将某个类的字节码进行转换并返回字节码数组(此方法用于代理模式启动)
     *
     * @param className  需要增强类名
     * @param myAspect   增强切面类
     * @param basePath   (测试)增强后的类字节码输出位置(默认不输出)
     * @param isbuildAll (测试)是否生成所有的字节码,包括没增强的(默认不输出没增强字节码)
     * @return 增强后的字节码数组
     */
    public static byte[] classScanner(String className, MyAspect myAspect, String basePath, boolean isbuildAll) throws IOException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        System.out.println("ClassReader " + className);
        ClassReader classReader = new ClassReader(className);//分析类
        ControllerAdapter cv = new ControllerAdapter(classWriter, myAspect);//cv将所有事件转发给cw
        classReader.accept(cv, ClassReader.EXPAND_FRAMES);
        boolean isWrite = cv.isController();
        byte car[] = classWriter.toByteArray();
        if (isWrite || isbuildAll) {
            if (!("".equals(basePath) || basePath == null)) {
                writeToClass(classWriter, basePath + className + ".class");
            }
        }
        return car;
    }

    /**
     * 适配   通过类名加载不到类
     *
     * @param in         需要增强的类的流
     * @param myAspect   切面
     * @param basePath   基础路径
     * @param isbuildAll 是否生成所有类 为false
     * @param className  需要增强的类名
     * @return 增强后的字节数组
     * @throws IOException
     */
    public static byte[] classScanner(InputStream in, MyAspect myAspect, String basePath, boolean isbuildAll, String className) throws IOException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassReader classReader = new ClassReader(in);//分析类
//TODO:拦截器的增强
        ControllerAdapter cv = new ControllerAdapter(classWriter, myAspect);//cv将所有事件转发给cw
//        InterceptorAdapter cv = new InterceptorAdapter(classWriter);
        classReader.accept(cv, ClassReader.EXPAND_FRAMES);
//        isWrite = cv.isInterceptorConfig();
        boolean isWrite = cv.isController();
        byte car[] = classWriter.toByteArray();
        if (isWrite || isbuildAll) {
            if (!("".equals(basePath) || basePath == null)) {
                writeToClass(classWriter, basePath + className + ".class");
            }
        }
        return car;
    }

    public static byte[] classScanner(InputStream in, MyAspect myAspect, String basePath, String className) throws IOException {
        return classScanner(in, myAspect, basePath, false, className);
    }

    public static byte[] classScanner(String className, MyAspect myAspect, String basePath) throws IOException {
        return classScanner(className, myAspect, basePath, false);
    }

    public static byte[] classScanner(String className, MyAspect myAspect) throws IOException {
        return classScanner(className, myAspect, null, false);
    }

    /**
     * 将类写到磁盘上(工具方法)
     */
    public static void writeToClass(ClassWriter cw, String filePath) {
        //可以考虑nio
        byte[] data = cw.toByteArray();
        try {
            new File(filePath.substring(0, filePath.lastIndexOf("/"))).mkdirs();
            File file = new File(filePath);
            System.out.println(filePath);
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(data);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印类的asm结构组成,(指令形式)
     */
    public static void getTraceInfo(Class<?> clazz, String logPath) {
        ClassWriter classWriter = new ClassWriter(0);
        try {
            ClassReader classReader = new ClassReader(Type.getType(clazz).getInternalName());
            ASMifier asMifier = new ASMifier();
            TraceClassVisitor cv = new TraceClassVisitor(classWriter, asMifier, new PrintWriter(new FileOutputStream(logPath)));
            //生成asm代码
            classReader.accept(cv, ClassReader.EXPAND_FRAMES);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getTraceInfo(Class<?> clazz) {
        getTraceInfo(clazz, Config.traceTxtPos);//默认使用配置文件里的位置
    }
//======================================================================================================================
//反射
//======================================================================================================================

    /**
     * 打印目标包所有类信息
     *
     * @param c          目标类集合
     * @param isParallel 是否采用并行处理
     */
    @Deprecated
    public static void getInfo(Collection<Class<?>> c, boolean isParallel) {
        if (isParallel) {
            c.stream().parallel().forEach(ControllerScanner::getInfo);  //如果使用并行处理
        } else {
            c.forEach(ControllerScanner::getInfo);
        }
    }

    /**
     * 打印目标类的信息
     */
    @Deprecated
    public static void getInfo(Class<?> clazz) {

        System.out.println("===================================================================================");
        Annotation[] annotations = clazz.getAnnotations();//获得所有的注解
        Arrays.stream(annotations)
                .map(a -> a.annotationType().getClass().getFields())
                .flatMap(Arrays::stream)
                .forEach(a -> System.out.println("注解参数名" + a.getName()));

        Field[] declaredFields = clazz.getDeclaredFields();//获得所有的属性
        Arrays.stream(declaredFields).forEach(a -> System.out.println("属性" + a.toString()));
        Method[] declaredMethods = clazz.getDeclaredMethods();//获得所有的方法
        Arrays.stream(declaredMethods)
                .forEach(m -> {
                    System.out.println("----------------------------------------");
                    System.out.println("method: " + m.getName());
                    System.out.println("return: " + m.getReturnType().getName());
                    for (Parameter p : m.getParameters()) {
                        System.out.println("parameter: " + p.getType().getName() + ", " + p.getName());
                    }
                    System.out.println("----------------------------------------");
                });
        System.out.println("===================================================================================");
    }


}
