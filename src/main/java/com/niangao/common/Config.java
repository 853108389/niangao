package com.niangao.common;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author NianGao
 * @Date 2018/5/9.
 * @description 配置类
 */
public class Config {
    public static List<String> IClassAnnName = new ArrayList<>();//需要扫描的类的注解的描述符
    public static List<String> IMethodAnnName = new ArrayList<>();//需要扫描的方法的注解的描述符
    public static List<String> IPackageName = new ArrayList<>();//需要扫描的包名
    public static String weavingPackageName = "";//要植入的包名
    public static String weavingInterceptorName;//植入的拦截器方法名
    private static Map<String, String> descMap = new HashMap<String, String>() {
        {
            put("", "");
            put("Controller", "Lorg/springframework/stereotype/Controller;");
            put("RestController", "Lorg/springframework/web/bind/annotation/RestController;");
            put("RequestMapping", "Lorg/springframework/web/bind/annotation/RequestMapping;");
            put("PostMapping", "Lorg/springframework/web/bind/annotation/PostMapping;");
            put("PutMapping", "Lorg/springframework/web/bind/annotation/PutMapping;");
            put("DeleteMapping", "Lorg/springframework/web/bind/annotation/DeleteMapping;");
            put("GetMapping", "Lorg/springframework/web/bind/annotation/GetMapping;");
        }
    };//配置对应的注解对应的类描述符
    private static boolean isAllScan = true;
    public static List<String> EPackageBaseName = new ArrayList<>();//不扫描的包名
    //==========================平台配置
    public static String username; //用户名
    public static String password;//密码
    public static String projectName;//项目名
    public static String catName;//分类名
    public static String colName;//测试用例分类名


    public static void init() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config.config");
        //===========扫描配置
        IClassAnnName = Arrays.stream(resourceBundle.getString("IClassAnnName").split(";"))
                .map(IClassAnnDes -> descMap.get(IClassAnnDes.trim()))
                .collect(Collectors.toList());
        IMethodAnnName = Arrays.stream(resourceBundle.getString("IMethodAnnName").split(";"))
                .map(IClassAnnDes -> descMap.get(IClassAnnDes.trim()))
                .collect(Collectors.toList());
        if (resourceBundle.containsKey("IPackageBaseName") && !resourceBundle.getString("IPackageBaseName").trim().equals(";")) {
            isAllScan = false;
            String iPackageBaseName = resourceBundle.getString("IPackageBaseName").replace(".", "/").split(";")[0] + "/";
            IPackageName.add(iPackageBaseName);
            if (resourceBundle.containsKey("IPackageName") && !resourceBundle.getString("IPackageName").trim().equals(";")) {
                IPackageName = Arrays.stream(resourceBundle.getString("IPackageName").split(";"))
                        .map(IClassAnnDes -> iPackageBaseName + IClassAnnDes.trim() + "/")
                        .collect(Collectors.toList());
            }
        }
        EPackageBaseName = Arrays.stream(resourceBundle.getString("EPackageBaseName").replace(".", "/").split(";"))
                .collect(Collectors.toList());
        //==========植入配置
        weavingPackageName = resourceBundle.getString("weavingPackageName").replace(".", "/").split(";")[0] + "/";
        weavingInterceptorName = resourceBundle.getString("weavingInterceptorName").split(";")[0];
        //==========平台配置
        username = resourceBundle.getString("username");
        password = resourceBundle.getString("password");
        projectName = resourceBundle.getString("projectName");
        catName = resourceBundle.getString("catName");
        colName = resourceBundle.getString("colName");
    }

    public static boolean isScan(String className) {
//        System.out.println("------------------" + className);
        if (className == null) {
//            System.out.println("------" + "类名为null");
            return false;
        }
        if (className.contains("$")) {
//            System.out.println("------" + "spring代理: " + className);
            return false;
        }
        if (Config.EPackageBaseName.stream().anyMatch(a -> className.startsWith(a))) {
            return false;
        }
        return isAllScan || Config.IPackageName.stream().anyMatch(a -> className.startsWith(a));
    }

}
