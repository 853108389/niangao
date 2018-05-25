package com.niangao.common.aspect;

import com.niangao.common.bean.Info;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.commons.LocalVariablesSorter;

/**
 * @Author NianGao
 * @Date 2018/5/8.
 * @description 方法增强的切面类
 */
public abstract class MyAspect implements Opcodes {
    protected int index;//返回值的局部变量位置

    protected LocalVariablesSorter mv = null;

    protected Info info = new Info();

    protected MyAspect() {
    }


    public abstract void before();

    public abstract void after(int opcode);

    public void setMv(LocalVariablesSorter mv) {
        this.mv = mv;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public MethodVisitor getMv() {
        return mv;
    }

    public Info getInfo() {
        return info;
    }
}
