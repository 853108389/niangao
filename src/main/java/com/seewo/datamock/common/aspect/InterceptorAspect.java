package com.seewo.datamock.common.aspect;

import com.seewo.datamock.common.Config;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/**
 * @Author NianGao
 * @Date 2018/5/31.
 * @description
 */
public class InterceptorAspect extends BaseAspect {
    private static class InnerClass2 {
        private static BaseAspect m = new InterceptorAspect();
    }

    public static BaseAspect getInstance() {
        return InterceptorAspect.InnerClass2.m;
    }

    //将我们的bean注册到拦截器栈中
    public void addInterceptorToStack() {
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, info.getOwner(), Config.weavingInterceptorName, "()Lorg/springframework/web/servlet/handler/HandlerInterceptorAdapter;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "org/springframework/web/servlet/config/annotation/InterceptorRegistry", "addInterceptor", "(Lorg/springframework/web/servlet/HandlerInterceptor;)Lorg/springframework/web/servlet/config/annotation/InterceptorRegistration;", false);
        mv.visitInsn(ICONST_1);
        mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_0);
        mv.visitLdcInsn(Config.weavingInterceptorPathPatterns);
        mv.visitInsn(AASTORE);
        mv.visitMethodInsn(INVOKEVIRTUAL, "org/springframework/web/servlet/config/annotation/InterceptorRegistration", "addPathPatterns", "([Ljava/lang/String;)Lorg/springframework/web/servlet/config/annotation/InterceptorRegistration;", false);
        mv.visitInsn(POP);
        System.out.println("修改成功................................................");
    }


    //添加一个获取自定义拦截器方法
    public void getBeanMethod(MethodVisitor mv) {
        {
//            MethodVisitor mv = this.visitMethod(ACC_PUBLIC, Config.weavingInterceptorName, "()Lorg/springframework/web/servlet/handler/HandlerInterceptorAdapter;", null, null);
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
    public void before() {
        if (mv != null) {
            addInterceptorToStack();
            System.out.println("修改成功................................................");
        }
    }

    @Override
    public void after(int opcode) {

    }
}
