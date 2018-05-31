package com.seewo.datamock.common;

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
    public static String weavingInterceptorClassName;//植入的拦截器类名
    public static String weavingInterceptorPathPatterns;//植入的拦截器拦截路径 默认为/**
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
    public static String enhanceType = "controller";//增强的类型
    //==========================平台配置
    public static String username; //用户名
    public static String password;//密码
    public static String projectName;//项目名
    public static String catName;//分类名
    public static String colName;//测试用例分类名
    public static String groupName;//分组名
    public static HashMap<String, String> defaultHeaders = new HashMap<>();//默认携带的请求头
    //=========================开发时配置
    public static String traceTxtPos;//类的asm指令生成位置
    public static String agentGenClassPos;//代理模式启动下,生成的增强后的类根目录
    public static String mainBaseScanPack;//main方法启动测试asm生成类时,扫描的包路径
    public static String mainGenClassPos;//main方法启动测试asm生成类时,生成的类目录
    public static ResourceBundle resourceBundle = ResourceBundle.getBundle("config.config");

    public static void init() {
        scanConf();//扫描配置
        weavingConf();  //植入配置
        plantformConf();   //平台配置
        devConf();  //开发时配置
    }

    //扫描配置
    public static void scanConf() {
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
        String type = resourceBundle.getString("enhanceType").trim();
        if (!(type.equals(";") || type.equals(""))) {
            enhanceType = type.split(";")[0].trim();
        }
    }

    //植入配置

    public static void weavingConf() {
        weavingPackageName = resourceBundle.getString("weavingPackageName").replace(".", "/").split(";")[0].trim() + "/";
        weavingInterceptorName = resourceBundle.getString("weavingInterceptorName").split(";")[0].trim();
        weavingInterceptorClassName = resourceBundle.getString("weavingInterceptorClassName").split(";")[0].trim();
        String path = resourceBundle.getString("weavingInterceptorPathPatterns").trim();
        if (!(path.equals(";") || path.equals(""))) {
            weavingInterceptorPathPatterns = path.split(";")[0].trim();
        } else {
            weavingInterceptorPathPatterns = "/**";
        }
    }

    //平台配置
    public static void plantformConf() {
        username = resourceBundle.getString("username");
        password = resourceBundle.getString("password");
        projectName = resourceBundle.getString("projectName");
        catName = resourceBundle.getString("catName");
        colName = resourceBundle.getString("colName");
        groupName = resourceBundle.getString("groupName");
        String headers = resourceBundle.getString("headers").trim();
        if (!(headers.equals(";") || headers.equals(""))) {
            Arrays.stream(resourceBundle.getString("headers").split("&")).forEach(entry -> {
                String[] split = entry.split("=");
                defaultHeaders.put(split[0], split[1]);
            });
        }
    }

    //开发时配置
    public static void devConf() {
        traceTxtPos = resourceBundle.getString("traceTxtPos");
        agentGenClassPos = resourceBundle.getString("agentGenClassPos").trim();
        mainBaseScanPack = resourceBundle.getString("mainBaseScanPack").trim();
        mainGenClassPos = resourceBundle.getString("mainGenClassPos").trim();
    }

    //是否扫描该类
    public static boolean isScan(String className) {
        if (className == null) {
            return false;
        }
        if (className.contains("$")) {
            //包含内部类和大量spring的代理类
            return false;
        }
        if (Config.EPackageBaseName.stream().anyMatch(a -> className.startsWith(a))) {
            return false;
        }
        return isAllScan || Config.IPackageName.stream().anyMatch(a -> className.startsWith(a));
    }

}
