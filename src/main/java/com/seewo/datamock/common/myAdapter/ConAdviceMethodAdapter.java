package com.seewo.datamock.common.myAdapter;

import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;

/**
 * @Author NianGao
 * @Date 2018/5/30.
 * @description 方法增强
 */
public class ConAdviceMethodAdapter extends AdviceAdapter {
    private String owner;//所属类
    private String methodName;//方法名

    public ConAdviceMethodAdapter(int access, String name, String desc, String signature, String[] exceptions,
                                  MethodVisitor mv, String owner, String methodName) {
        super(Opcodes.ASM5, mv, access, name, desc);
        this.owner = owner;
        this.methodName = methodName;
    }

}
