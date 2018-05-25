package com.niangao.common.myAdapter;

import com.niangao.common.aspect.MyAspect;
import com.niangao.common.weaving.beans.AnnEntity;
import com.niangao.common.weaving.beans.AnnEntry;
import com.niangao.common.weaving.beans.EnumEntry;
import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/**
 * @Author NianGao
 * @Date 2018/5/10.
 * @description 注意:时序只有一次, 不存在重复,参数注解访问和方法注解访问不一样
 */
public class ControllerParamsAnoAdapter extends AnnotationVisitor {
    private static MyAspect aspect = null;//增强切面
    private AnnEntity annEntity = null;//注解实体

    /**
     * 方法访问器调用此构造
     *
     * @param annotationVisitor
     * @param aspect
     */
    public ControllerParamsAnoAdapter(AnnotationVisitor annotationVisitor, MyAspect aspect, int index) {
        super(Opcodes.ASM5, annotationVisitor);
        this.aspect = aspect;
        this.annEntity = aspect.getInfo().getParamsAnnMap().get(index);
    }

    /**
     * @param s null
     * @param o 注解值
     */
    @Override
    public void visit(String s, Object o) {
//        System.out.println("visit " + s + "__" + o);
        AnnEntry annEntry = new AnnEntry();
        annEntry.setKey(s);//这里是有值的,和方法注解不同
        annEntry.getEnumEntrylist().add(new EnumEntry("", o));//设置key对应的value
        annEntry.setEnumType(false);
        annEntity.getEntryList().add(annEntry);
        av.visit(s, o);
    }

    /**
     * @param s  null
     * @param s1 enum名
     * @param s2 enmu值
     */
    @Override
    public void visitEnum(String s, String s1, String s2) {
        System.out.println("visitEnum");
        AnnEntry annEntry = new AnnEntry();
        annEntry.setKey(s);
        annEntry.getEnumEntrylist().add(new EnumEntry(s1, s2));//设置key对应的value
        annEntry.setEnumType(true);
        annEntity.getEntryList().add(annEntry);
        av.visitEnum(s, s1, s2);
    }


    @Override
    public void visitEnd() {
//        System.out.println(annEntity);
        av.visitEnd();
    }
}
