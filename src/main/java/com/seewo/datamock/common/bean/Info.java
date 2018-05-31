package com.seewo.datamock.common.bean;


import com.seewo.datamock.common.weaving.beans.AnnEntity;
import lombok.Data;

import java.util.*;

/**
 * @Author NianGao
 * @Date 2018/5/7.
 * @description 用于保存ControllerAdapter适配后的信息
 */
@Data
public class Info {
    private String owner;//所属类   //com.niangao.test2/MyController
    private String methodName;//方法名称  //testMethod
    private String methodDesc;//方法描述符 //(Ljava/lang/String;)Ljava/lang/String;
    private int access; //方法修饰符 //1
    private List<LocalParams> paramsList = new ArrayList<>();//参数变量表
    private LinkedList<AnnEntity> annList = new LinkedList<>();//所有的注解信息
    private Map<Integer, AnnEntity> paramsAnnMap = new HashMap<>();// {注解位置,注解信息}
    private int paramsSize = 0;//paramsList在代理模式启动后失效(未知原因).先使用此属性
    private String basepath = "";//类注解上的path

    public Info(String owner, String methodName, String methodDesc, int access, List<LocalParams> paramsList) {
        this.owner = owner;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        this.paramsList = paramsList;
        this.access = access;
    }

    public Info() {
    }
}
