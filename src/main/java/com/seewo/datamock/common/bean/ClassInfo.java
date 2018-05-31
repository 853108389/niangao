package com.seewo.datamock.common.bean;

import lombok.Builder;
import lombok.Data;

/**
 * @Author NianGao
 * @Date 2018/5/30.
 * @description 用于保存类的信息
 */
@Data
@Builder
public class ClassInfo {
    public int version;
    public int access;
    public String name;
    public String signature;
    public String superName;
    public String[] interfaces;
}
