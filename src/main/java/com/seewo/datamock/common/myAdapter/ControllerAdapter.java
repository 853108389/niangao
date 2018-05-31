package com.seewo.datamock.common.myAdapter;

import com.seewo.datamock.common.Config;
import com.seewo.datamock.common.aspect.BaseAspect;
import jdk.internal.org.objectweb.asm.*;

/**
 * @Author NianGao
 * @Date 2018/5/8.
 * @description 类访问器适配器, 基于委托的形式 使用ASM5
 */
public class ControllerAdapter extends ClassVisitor implements Opcodes {
    private String owner; //记住类名
    private boolean isInterface; //判断改类是否为接口,如果是就不进行增强
    private boolean isController = false;//判断是否是Controller
    private BaseAspect aspect = null;

    public ControllerAdapter(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    public ControllerAdapter(ClassVisitor cv, BaseAspect baseAspect) {
        super(Opcodes.ASM5, cv);
        this.aspect = baseAspect;
    }


    public boolean isController() {
        return isController;
    }

    @Override
    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        cv.visit(version, access, name, signature, superName, interfaces);
        owner = name;//记录owner
        isInterface = (access & Opcodes.ACC_INTERFACE) != 0;//记录是否为接口
        System.out.println("=============================" + owner + "=============================");
    }

    @Override
    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        if (isController || Config.IClassAnnName.stream().anyMatch(s::equals)) {
            //如果是Controller
            System.out.println("Controller: " + owner);
            isController = true;
        } else {
//            System.out.println("others: " + owner);
            isController = false;
            cv.visitEnd();
        }
        AnnotationVisitor av = cv.visitAnnotation(s, b);
        if (s.equals("Lorg/springframework/web/bind/annotation/RequestMapping;")) {
            av = new ControllerClassAnoAdapter(av, aspect);
        }
        return av;
    }

    /**
     * @param access     方法修饰符
     * @param name       方法名称
     * @param desc       方法返回值
     * @param signature  方法泛型
     * @param exceptions 方法异常
     * @return jdk.internal.org.objectweb.asm.MethodVisitor
     * @exception:
     * @see:
     */
    @Override
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature,
                exceptions);
//        System.out.println("_" + name);
        if (!isInterface && mv != null) {
            if (isController) {
                //如果是Con,则进行方法增强
                System.out.println("methodname " + name);
                ControllerMethodAdapter controllerMethodAdapter = new ControllerMethodAdapter(access, name, desc, signature, exceptions, mv, owner, aspect);
                mv = controllerMethodAdapter;
                aspect.setMv(controllerMethodAdapter);
            }
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        System.out.println("=============================" + owner + "=============================");
        cv.visitEnd();
    }

}
