package com.niangao.common.myAdapter;

import com.niangao.common.Config;
import com.niangao.common.aspect.MyAspect;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;

/**
 * @Author NianGao
 * @Date 2018/5/24.
 * @description
 */
public class InterceptorMethodAdapter extends AdviceAdapter {
    private String owner;
    private MyAspect aspect;

    public InterceptorMethodAdapter(int access, String name, String desc,
                                    MethodVisitor mv, String owner) {
        this(access, name, desc, null, null, mv, owner, null);
    }

    public InterceptorMethodAdapter(int access, String name, String desc, String signature, String[] exceptions,
                                    MethodVisitor mv, String owner) {
        super(Opcodes.ASM5, mv, access, name, desc);
        this.owner = owner;
    }

    public InterceptorMethodAdapter(int access, String name, String desc, String signature, String[] exceptions,
                                    MethodVisitor mv, String owner, MyAspect aspect) {
        super(Opcodes.ASM5, mv, access, name, desc);
        this.owner = owner;
        this.aspect = aspect;
    }


    @Override
    public void visitMethodInsn(int i, String s, String name, String desc, boolean b) {
        System.out.println(i + "__" + s + "__" + name + "__" + desc + "__" + b);
        mv.visitMethodInsn(i, s, name, desc, b);
    }


    @Override
    protected void onMethodEnter() {
        //将我们的bean注册到拦截器栈中
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, owner, Config.weavingInterceptorName, "()L" + Config.weavingPackageName + "UploadInterceptor;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "org/springframework/web/servlet/config/annotation/InterceptorRegistry", "addInterceptor", "(Lorg/springframework/web/servlet/HandlerInterceptor;)Lorg/springframework/web/servlet/config/annotation/InterceptorRegistration;", false);
        mv.visitInsn(ICONST_1);
        mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_0);
        mv.visitLdcInsn("/**");
        mv.visitInsn(AASTORE);
        mv.visitMethodInsn(INVOKEVIRTUAL, "org/springframework/web/servlet/config/annotation/InterceptorRegistration", "addPathPatterns", "([Ljava/lang/String;)Lorg/springframework/web/servlet/config/annotation/InterceptorRegistration;", false);
        mv.visitInsn(POP);
    }

    @Override
    protected void onMethodExit(int opcode) {

    }


    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals);
    }
}
