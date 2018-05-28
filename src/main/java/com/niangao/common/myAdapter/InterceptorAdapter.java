package com.niangao.common.myAdapter;

import com.niangao.common.Config;
import com.niangao.common.aspect.MyAspect;
import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/**
 * @Author NianGao
 * @Date 2018/5/23.
 * @description
 */
public class InterceptorAdapter extends ClassVisitor implements Opcodes {
    private String owner; //记住类名
    private boolean isInterface; //判断改类是否为接口,如果是就不进行增强
    private boolean isInterceptorConfig = false;//判断是否是interceptorConfig
    private boolean isAddInterceptorsExit = false;//判断添加过滤器的方法是否存在

    public InterceptorAdapter(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    public InterceptorAdapter(ClassVisitor cv, MyAspect myAspect) {
        super(Opcodes.ASM5, cv);
    }

    public boolean isInterceptorConfig() {
        return isInterceptorConfig;
    }

    @Override
    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        cv.visit(version, access, name, signature, superName, interfaces);
        System.out.println("visit " + name + "___" + superName);
        if ("org/springframework/web/servlet/config/annotation/WebMvcConfigurerAdapter".equals(superName)) {
            //如果继承了WebMvcConfigurerAdapter ,就一定是过滤器配置类
            System.out.println("visit==================");
            isInterceptorConfig = true;
//            genBeanMethod();
        } else {
            isInterceptorConfig = false;
        }
        isInterceptorConfig = true;
        owner = name;//记录owner
        isInterface = (access & Opcodes.ACC_INTERFACE) != 0;//记录是否为接口
        System.out.println("=============================" + owner + "=============================");
    }

    //生成一个bean方法,将我们自己的拦截器声明为一个bean
    public void genBeanMethod() {
        MethodVisitor mv = this.visitMethod(ACC_PUBLIC, Config.weavingInterceptorName, "()L" + Config.weavingPackageName + "interceptor/UploadInterceptor;", null, null);
        {
            AnnotationVisitor av0 = mv.visitAnnotation("Lorg/springframework/context/annotation/Bean;", true);
            av0.visitEnd();
        }
        mv.visitCode();
        mv.visitTypeInsn(NEW, Config.weavingPackageName + "interceptor/UploadInterceptor");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, Config.weavingPackageName + "interceptor/UploadInterceptor", "<init>", "()V", false);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();
        System.out.println("已经生成bean方法................................................");
    }

    @Override
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature,
                exceptions);
        if (isInterceptorConfig && name.equals("addInterceptors") && desc.equals("(Lorg/springframework/web/servlet/config/annotation/InterceptorRegistry;)V")) {
            System.out.println("visitMethod " + access + " " + name + " " + desc);
            //是注册方法会进入注册方法
            isAddInterceptorsExit = true;
            System.out.println("尝试修改添加方法................................................");
            mv = new InterceptorMethodAdapter(access, name, desc, signature, exceptions, mv, owner);
        }
        return mv;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        System.out.println("visitAnnotation " + s);
        AnnotationVisitor av = cv.visitAnnotation(s, b);
        return av;
    }

    @Override
    public void visitEnd() {
        if (isInterceptorConfig && !isAddInterceptorsExit) {
            //如果是配置类,但是却没有写这个方法 生成此方法
//            TODO:
        }
        System.out.println("=============================" + owner + "=============================");

    }
}
