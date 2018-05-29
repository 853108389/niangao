package com.seewo.datamock.common.myAdapter;

import com.seewo.datamock.common.aspect.MyAspect;
import com.seewo.datamock.common.weaving.beans.AnnEntry;
import com.seewo.datamock.common.weaving.beans.EnumEntry;
import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/**
 * @Author NianGao
 * @Date 2018/5/10.
 * @description 注意注意:1.适配注解访问器时, 要返回new的, 否则就会使用原有的访问器,
 * 2.因为注解访问器会嵌套返回,所以全局变量要小心,不能保证每个注解访问器都一样
 */
public class ControllerMethodAnoAdapter extends AnnotationVisitor {
    private static MyAspect aspect = null;//增强切面
    private AnnEntry annEntry = null;//注解实体
    private boolean flag = false;//因为ControllerAnoAdapter会进行嵌套扫描,false代表一个键值对扫描完毕

    /**
     * 方法访问器调用此构造
     *
     * @param annotationVisitor
     * @param aspect
     */
    public ControllerMethodAnoAdapter(AnnotationVisitor annotationVisitor, MyAspect aspect) {
        super(Opcodes.ASM5, annotationVisitor);
        this.aspect = aspect;
        this.flag = true;//所以在最外层的时候设置flag,代表本次注解全部扫描完毕
    }

    /**
     * 嵌套调用调用此构造
     *
     * @param annotationVisitor
     * @param annEntry
     */
    public ControllerMethodAnoAdapter(AnnotationVisitor annotationVisitor, AnnEntry annEntry) {
        super(Opcodes.ASM5, annotationVisitor);
        this.annEntry = annEntry;
    }

    /**
     * @param s null
     * @param o 注解值
     */
    @Override
    public void visit(String s, Object o) {
//        System.out.println("visit- " + s + "__" + o);
        this.annEntry.getEnumEntrylist().add(new EnumEntry("", o));//设置key对应的value
        this.annEntry.setEnumType(false);
        av.visit(s, o);
    }

    /**
     * @param s  null
     * @param s1 enum名
     * @param s2 enmu值
     */
    @Override
    public void visitEnum(String s, String s1, String s2) {
        this.annEntry.getEnumEntrylist().add(new EnumEntry(s1, s2));//设置key对应的value
        this.annEntry.setEnumType(true);
//        System.out.println("enum " + s + "__" + s1 + "__" + s2);
        av.visitEnum(s, s1, s2);
    }

    /**
     * 注解key
     *
     * @param s
     * @return
     */
    @Override
    public AnnotationVisitor visitArray(String s) {
        AnnEntry annEntry = new AnnEntry(s);//设置key;
        return new ControllerMethodAnoAdapter(av.visitArray(s), annEntry);//这行很关键,一定要用new的,保证每次返回的都是我们适配后的annotationVisitor
    }

    @Override
    public AnnotationVisitor visitAnnotation(String s, String s1) {
        return new ControllerMethodAnoAdapter(av.visitAnnotation(s, s1), annEntry);
    }

    @Override
    public void visitEnd() {
        if (!flag) {
            //一个键值对扫描完毕,实体添加键值对
//            System.out.println(annEntry);
            aspect.getInfo().getAnnList().peek().getEntryList().add(annEntry);
        } else {
            //一个注解扫描完毕
//            System.out.println(aspect.getInfo().getAnnList().peek());
        }
        av.visitEnd();
    }
}
