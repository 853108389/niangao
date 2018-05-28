package com.niangao.common.myAdapter;

import com.niangao.common.aspect.MyAspect;
import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/**
 * @Author NianGao
 * @Date 2018/5/18.
 * @description 类的注解类访问器适配器
 */
public class ControllerClassAnoAdapter extends AnnotationVisitor {
    private static MyAspect aspect = null;//增强切面

    public ControllerClassAnoAdapter(AnnotationVisitor annotationVisitor, MyAspect aspect) {
        super(Opcodes.ASM5, annotationVisitor);
        this.aspect = aspect;
    }


    /**
     * @param s null
     * @param o 注解值
     */
    @Override
    public void visit(String s, Object o) {
        this.aspect.getInfo().setBasepath(o.toString());
        av.visit(s, o);
    }

    /**
     * @param s  null
     * @param s1 enum名
     * @param s2 enmu值
     */
    @Override
    public void visitEnum(String s, String s1, String s2) {
        av.visitEnum(s, s1, s2);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String s, String s1) {
        return new ControllerClassAnoAdapter(av.visitAnnotation(s, s1), aspect);
    }

    @Override
    public AnnotationVisitor visitArray(String s) {
        return new ControllerClassAnoAdapter(av.visitArray(s), aspect);
    }

    @Override
    public void visitEnd() {
        av.visitEnd();
    }
}
