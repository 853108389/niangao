package com.seewo.datamock.common.myAdapter;

import com.seewo.datamock.common.Config;
import com.seewo.datamock.common.aspect.BaseAspect;
import jdk.internal.org.objectweb.asm.*;

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

    public InterceptorAdapter(ClassVisitor cv, BaseAspect baseAspect) {
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
            isInterceptorConfig = true;
            getBeanMethod();
        } else {
            isInterceptorConfig = false;
        }
        owner = name;//记录owner
        isInterface = (access & Opcodes.ACC_INTERFACE) != 0;//记录是否为接口
        System.out.println("=============================" + owner + "=============================");
    }

    /**
     * 添加一个获取自定义拦截器方法
     */
    public void getBeanMethod() {
        {
            MethodVisitor mv = this.visitMethod(ACC_PUBLIC, Config.weavingInterceptorName, "()Lorg/springframework/web/servlet/handler/HandlerInterceptorAdapter;", null, null);
         /*   {
                AnnotationVisitor av0 = mv.visitAnnotation("Lorg/springframework/context/annotation/Bean;", true);
                av0.visitEnd();
            }*/
            mv.visitCode();
            Label l0 = new Label();
            Label l1 = new Label();
            Label l2 = new Label();
            mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLineNumber(15, l3);
            mv.visitTypeInsn(NEW, "com/seewo/datamock/common/utils/MyClassLoader");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "com/seewo/datamock/common/utils/MyClassLoader", "<init>", "()V", false);
            mv.visitVarInsn(ASTORE, 0);
            mv.visitLabel(l0);
            mv.visitLineNumber(17, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitLdcInsn(Config.weavingInterceptorClassName);
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/seewo/datamock/common/utils/MyClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;", false);
            mv.visitVarInsn(ASTORE, 1);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLineNumber(18, l4);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "newInstance", "()Ljava/lang/Object;", false);
            mv.visitVarInsn(ASTORE, 2);
            Label l5 = new Label();
            mv.visitLabel(l5);
            mv.visitLineNumber(19, l5);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitTypeInsn(INSTANCEOF, "org/springframework/web/servlet/handler/HandlerInterceptorAdapter");
            Label l6 = new Label();
            mv.visitJumpInsn(IFEQ, l6);
            Label l7 = new Label();
            mv.visitLabel(l7);
            mv.visitLineNumber(20, l7);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "newInstance", "()Ljava/lang/Object;", false);
            mv.visitTypeInsn(CHECKCAST, "org/springframework/web/servlet/handler/HandlerInterceptorAdapter");
            mv.visitVarInsn(ASTORE, 3);
            Label l8 = new Label();
            mv.visitLabel(l8);
            mv.visitLineNumber(21, l8);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
            Label l9 = new Label();
            mv.visitLabel(l9);
            mv.visitLineNumber(22, l9);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitLabel(l1);
            mv.visitInsn(ARETURN);
            mv.visitLabel(l6);
            mv.visitLineNumber(26, l6);
            mv.visitFrame(Opcodes.F_NEW, 1, new Object[]{"com/seewo/datamock/common/utils/MyClassLoader"}, 0, new Object[]{});
            Label l10 = new Label();
            mv.visitJumpInsn(GOTO, l10);
            mv.visitLabel(l2);
            mv.visitLineNumber(24, l2);
            mv.visitFrame(Opcodes.F_NEW, 1, new Object[]{"com/seewo/datamock/common/utils/MyClassLoader"}, 1, new Object[]{"java/lang/Exception"});
            mv.visitVarInsn(ASTORE, 1);
            Label l11 = new Label();
            mv.visitLabel(l11);
            mv.visitLineNumber(25, l11);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "()V", false);
            mv.visitLabel(l10);
            mv.visitLineNumber(27, l10);
            mv.visitFrame(Opcodes.F_NEW, 1, new Object[]{"com/seewo/datamock/common/utils/MyClassLoader"}, 0, new Object[]{});
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("拦截器加载异常.......");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            Label l12 = new Label();
            mv.visitLabel(l12);
            mv.visitLineNumber(28, l12);
            mv.visitInsn(ACONST_NULL);
            mv.visitInsn(ARETURN);
            Label l13 = new Label();
            mv.visitLabel(l13);
            mv.visitLocalVariable("weavingHandler", "Lorg/springframework/web/servlet/handler/HandlerInterceptorAdapter;", null, l8, l6, 3);
            mv.visitLocalVariable(Config.weavingInterceptorClassName, "Ljava/lang/Class;", "Ljava/lang/Class<*>;", l4, l6, 1);
            mv.visitLocalVariable("o", "Ljava/lang/Object;", null, l5, l6, 2);
            mv.visitLocalVariable("e", "Ljava/lang/Exception;", null, l11, l10, 1);
            mv.visitLocalVariable("myClassLoader", "Lcom/seewo/datamock/common/utils/MyClassLoader;", null, l0, l13, 0);
            mv.visitMaxs(2, 4);
            mv.visitEnd();
        }
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
