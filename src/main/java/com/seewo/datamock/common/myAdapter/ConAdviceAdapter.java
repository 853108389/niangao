package com.seewo.datamock.common.myAdapter;

import com.seewo.datamock.common.aspect.BaseAspect;
import com.seewo.datamock.common.bean.ClassInfo;
import jdk.internal.org.objectweb.asm.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author NianGao
 * @Date 2018/5/30.
 * @description 对@ControllerAdvice修饰的类进行增强 TODO:不知道是否可以有多个@ControllerAdvice
 */
public class ConAdviceAdapter extends ClassVisitor implements Opcodes {
    private boolean isInterface; //判断改类是否为接口,如果是就不进行增强
    private boolean isControllerAdvice = false;//判断是否是ControllerAdvice
    private boolean isImplemented = false;//是否继承过接口
    ClassInfo classInfo = null;

    public ConAdviceAdapter(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    public ConAdviceAdapter(ClassVisitor cv, BaseAspect baseAspect) {
        super(Opcodes.ASM5, cv);
    }


    public boolean isControllerAdvice() {
        return isControllerAdvice;
    }

    /**
     * 该类添加一个接口,并添加两个接口方法
     */

    @Override
    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        System.out.println("visitAnnotation " + s);
        if ("Lorg/springframework/web/bind/annotation/ControllerAdvice;".equals(s)) {
            //注解为ControllerAdvice的类
            isControllerAdvice = true;
            String desc = "org/springframework/web/servlet/mvc/method/annotation/ResponseBodyAdvice";
            if (!Arrays.stream(classInfo.getInterfaces()).anyMatch(a -> {
                return a.equals(desc);
            })) {
                //没实现过接口,TODO: =添加= 接口和方法
                isImplemented = false;
                String[] interfaces = classInfo.getInterfaces();
                List<String> collect = Arrays.stream(interfaces).collect(Collectors.toList());
                collect.add(desc);
                this.visit(classInfo.getVersion(), classInfo.getAccess(), classInfo.getName(), classInfo.getSignature(),
                        classInfo.getSuperName(), collect.toArray(new String[]{}));//添加接口
                addMethod();//添加方法
            } else {
                //实现过
                isImplemented = true;
            }
        }
        AnnotationVisitor av = cv.visitAnnotation(s, b);
        return av;
    }


    //添加拦截方法
    public void addMethod2() {
        {
            MethodVisitor mv = this.visitMethod(ACC_PUBLIC, "supports", "(Lorg/springframework/core/MethodParameter;Ljava/lang/Class;)Z", null, null);
            mv.visitParameter("methodParameter", 0);
            mv.visitParameter("aClass", 0);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("-----------------------我是拦截-------------------------");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitInsn(ICONST_1);
            mv.visitInsn(IRETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "Lcom/seewo/datamock/test2/MyTrace;", null, l0, l2, 0);
            mv.visitLocalVariable("methodParameter", "Lorg/springframework/core/MethodParameter;", null, l0, l2, 1);
            mv.visitLocalVariable("aClass", "Ljava/lang/Class;", null, l0, l2, 2);
            mv.visitMaxs(2, 3);
            mv.visitEnd();
        }
        {
            MethodVisitor mv = this.visitMethod(ACC_PUBLIC, "beforeBodyWrite", "(Ljava/lang/Object;Lorg/springframework/core/MethodParameter;Lorg/springframework/http/MediaType;Ljava/lang/Class;Lorg/springframework/http/server/ServerHttpRequest;Lorg/springframework/http/server/ServerHttpResponse;)Ljava/lang/Object;", null, null);
            mv.visitParameter("o", 0);
            mv.visitParameter("methodParameter", 0);
            mv.visitParameter("mediaType", 0);
            mv.visitParameter("aClass", 0);
            mv.visitParameter("serverHttpRequest", 0);
            mv.visitParameter("serverHttpResponse", 0);
            mv.visitCode();
            Label l0 = new Label();
            Label l1 = new Label();
            Label l2 = new Label();
            mv.visitTryCatchBlock(l0, l1, l2, "java/io/IOException");
            mv.visitLabel(l0);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitTypeInsn(CHECKCAST, "org/springframework/http/server/ServletServerHttpRequest");
            mv.visitVarInsn(ASTORE, 7);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitVarInsn(ALOAD, 7);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/springframework/http/server/ServletServerHttpRequest", "getServletRequest", "()Ljavax/servlet/http/HttpServletRequest;", false);
            mv.visitVarInsn(ASTORE, 8);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitVarInsn(ALOAD, 8);
            mv.visitMethodInsn(INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getContentLength", "()I", true);
            mv.visitVarInsn(ISTORE, 9);
            Label l5 = new Label();
            mv.visitLabel(l5);
            mv.visitVarInsn(ALOAD, 8);
            mv.visitMethodInsn(INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getContentLength", "()I", true);
            mv.visitIntInsn(NEWARRAY, T_BYTE);
            mv.visitVarInsn(ASTORE, 10);
            Label l6 = new Label();
            mv.visitLabel(l6);
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(ISTORE, 11);
            Label l7 = new Label();
            mv.visitLabel(l7);
            mv.visitFrame(Opcodes.F_NEW, 12, new Object[]{"com/seewo/datamock/test2/MyTrace", "java/lang/Object", "org/springframework/core/MethodParameter", "org/springframework/http/MediaType", "java/lang/Class", "org/springframework/http/server/ServerHttpRequest", "org/springframework/http/server/ServerHttpResponse", "org/springframework/http/server/ServletServerHttpRequest", "javax/servlet/http/HttpServletRequest", Opcodes.INTEGER, "[B", Opcodes.INTEGER}, 0, new Object[]{});
            mv.visitVarInsn(ILOAD, 11);
            mv.visitVarInsn(ALOAD, 8);
            mv.visitMethodInsn(INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getContentLength", "()I", true);
            Label l8 = new Label();
            mv.visitJumpInsn(IF_ICMPGE, l8);
            Label l9 = new Label();
            mv.visitLabel(l9);
            mv.visitVarInsn(ALOAD, 7);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/springframework/http/server/ServletServerHttpRequest", "getBody", "()Ljava/io/InputStream;", false);
            mv.visitVarInsn(ALOAD, 10);
            mv.visitVarInsn(ILOAD, 11);
            mv.visitVarInsn(ALOAD, 8);
            Label l10 = new Label();
            mv.visitLabel(l10);
            mv.visitMethodInsn(INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getContentLength", "()I", true);
            mv.visitVarInsn(ILOAD, 11);
            mv.visitInsn(ISUB);
            Label l11 = new Label();
            mv.visitLabel(l11);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/InputStream", "read", "([BII)I", false);
            mv.visitVarInsn(ISTORE, 12);
            Label l12 = new Label();
            mv.visitLabel(l12);
            mv.visitVarInsn(ILOAD, 12);
            mv.visitInsn(ICONST_M1);
            Label l13 = new Label();
            mv.visitJumpInsn(IF_ICMPNE, l13);
            Label l14 = new Label();
            mv.visitLabel(l14);
            mv.visitJumpInsn(GOTO, l8);
            mv.visitLabel(l13);
            mv.visitFrame(Opcodes.F_NEW, 13, new Object[]{"com/seewo/datamock/test2/MyTrace", "java/lang/Object", "org/springframework/core/MethodParameter", "org/springframework/http/MediaType", "java/lang/Class", "org/springframework/http/server/ServerHttpRequest", "org/springframework/http/server/ServerHttpResponse", "org/springframework/http/server/ServletServerHttpRequest", "javax/servlet/http/HttpServletRequest", Opcodes.INTEGER, "[B", Opcodes.INTEGER, Opcodes.INTEGER}, 0, new Object[]{});
            mv.visitVarInsn(ILOAD, 11);
            mv.visitVarInsn(ILOAD, 12);
            mv.visitInsn(IADD);
            mv.visitVarInsn(ISTORE, 11);
            Label l15 = new Label();
            mv.visitLabel(l15);
            mv.visitJumpInsn(GOTO, l7);
            mv.visitLabel(l8);
            mv.visitFrame(Opcodes.F_NEW, 11, new Object[]{"com/seewo/datamock/test2/MyTrace", "java/lang/Object", "org/springframework/core/MethodParameter", "org/springframework/http/MediaType", "java/lang/Class", "org/springframework/http/server/ServerHttpRequest", "org/springframework/http/server/ServerHttpResponse", "org/springframework/http/server/ServletServerHttpRequest", "javax/servlet/http/HttpServletRequest", Opcodes.INTEGER, "[B"}, 0, new Object[]{});
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitTypeInsn(NEW, "java/lang/String");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 10);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/String", "<init>", "([B)V", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            Label l16 = new Label();
            mv.visitLabel(l16);
            mv.visitVarInsn(ALOAD, 8);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKESTATIC, "com/seewo/datamock/common/weaving/utils/ConAdviceWeavingUtils", "doMock", "(Ljavax/servlet/http/HttpServletRequest;Ljava/lang/Object;Lorg/springframework/core/MethodParameter;Lorg/springframework/http/MediaType;)V", false);
            mv.visitLabel(l1);
            Label l17 = new Label();
            mv.visitJumpInsn(GOTO, l17);
            mv.visitLabel(l2);
            mv.visitFrame(Opcodes.F_NEW, 7, new Object[]{"com/seewo/datamock/test2/MyTrace", "java/lang/Object", "org/springframework/core/MethodParameter", "org/springframework/http/MediaType", "java/lang/Class", "org/springframework/http/server/ServerHttpRequest", "org/springframework/http/server/ServerHttpResponse"}, 1, new Object[]{"java/io/IOException"});
            mv.visitVarInsn(ASTORE, 7);
            Label l18 = new Label();
            mv.visitLabel(l18);
            mv.visitVarInsn(ALOAD, 7);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/IOException", "printStackTrace", "()V", false);
            mv.visitLabel(l17);
            mv.visitFrame(Opcodes.F_NEW, 7, new Object[]{"com/seewo/datamock/test2/MyTrace", "java/lang/Object", "org/springframework/core/MethodParameter", "org/springframework/http/MediaType", "java/lang/Class", "org/springframework/http/server/ServerHttpRequest", "org/springframework/http/server/ServerHttpResponse"}, 0, new Object[]{});
            mv.visitVarInsn(ALOAD, 1);
            mv.visitInsn(ARETURN);
            Label l19 = new Label();
            mv.visitLabel(l19);
            mv.visitLocalVariable("readlen", "I", null, l12, l15, 12);
            mv.visitLocalVariable("i", "I", null, l7, l8, 11);
            mv.visitLocalVariable("sshr", "Lorg/springframework/http/server/ServletServerHttpRequest;", null, l3, l1, 7);
            mv.visitLocalVariable("request", "Ljavax/servlet/http/HttpServletRequest;", null, l4, l1, 8);
            mv.visitLocalVariable("contentLength", "I", null, l5, l1, 9);
            mv.visitLocalVariable("buffer", "[B", null, l6, l1, 10);
            mv.visitLocalVariable("e", "Ljava/io/IOException;", null, l18, l17, 7);
            mv.visitLocalVariable("this", "Lcom/seewo/datamock/test2/MyTrace;", null, l0, l19, 0);
            mv.visitLocalVariable("o", "Ljava/lang/Object;", null, l0, l19, 1);
            mv.visitLocalVariable("methodParameter", "Lorg/springframework/core/MethodParameter;", null, l0, l19, 2);
            mv.visitLocalVariable("mediaType", "Lorg/springframework/http/MediaType;", null, l0, l19, 3);
            mv.visitLocalVariable("aClass", "Ljava/lang/Class;", null, l0, l19, 4);
            mv.visitLocalVariable("serverHttpRequest", "Lorg/springframework/http/server/ServerHttpRequest;", null, l0, l19, 5);
            mv.visitLocalVariable("serverHttpResponse", "Lorg/springframework/http/server/ServerHttpResponse;", null, l0, l19, 6);
//            mv.visitMaxs(5, 13);
            mv.visitEnd();
        }
    }

    //添加拦截方法
    public void addMethod() {
        {
            MethodVisitor mv = this.visitMethod(ACC_PUBLIC, "supports", "(Lorg/springframework/core/MethodParameter;Ljava/lang/Class;)Z", null, null);
            mv.visitParameter("methodParameter", 0);
            mv.visitParameter("aClass", 0);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/springframework/core/MethodParameter", "getParameterAnnotations", "()[Ljava/lang/annotation/Annotation;", false);
            mv.visitMethodInsn(INVOKESTATIC, "java/util/Arrays", "toString", "([Ljava/lang/Object;)Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/springframework/core/MethodParameter", "getParameterName", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("-----------------------\u6211\u662f\u62e6\u622a-------------------------");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitInsn(ICONST_1);
            mv.visitInsn(IRETURN);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLocalVariable("this", "Lcom/seewo/datamock/test/MyTrace;", null, l0, l4, 0);
            mv.visitLocalVariable("methodParameter", "Lorg/springframework/core/MethodParameter;", null, l0, l4, 1);
            mv.visitLocalVariable("aClass", "Ljava/lang/Class;", null, l0, l4, 2);
            mv.visitMaxs(2, 3);
            mv.visitEnd();
        }
        {
            MethodVisitor mv = this.visitMethod(ACC_PUBLIC, "beforeBodyWrite", "(Ljava/lang/Object;Lorg/springframework/core/MethodParameter;Lorg/springframework/http/MediaType;Ljava/lang/Class;Lorg/springframework/http/server/ServerHttpRequest;Lorg/springframework/http/server/ServerHttpResponse;)Ljava/lang/Object;", null, null);
            mv.visitParameter("o", 0);
            mv.visitParameter("methodParameter", 0);
            mv.visitParameter("mediaType", 0);
            mv.visitParameter("aClass", 0);
            mv.visitParameter("serverHttpRequest", 0);
            mv.visitParameter("serverHttpResponse", 0);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitTypeInsn(CHECKCAST, "org/springframework/http/server/ServletServerHttpRequest");
            mv.visitVarInsn(ASTORE, 7);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitVarInsn(ALOAD, 7);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/springframework/http/server/ServletServerHttpRequest", "getServletRequest", "()Ljavax/servlet/http/HttpServletRequest;", false);
            mv.visitVarInsn(ASTORE, 8);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitVarInsn(ALOAD, 8);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, "com/seewo/datamock/common/weaving/utils/ConAdviceWeavingUtils", "doMock", "(Ljavax/servlet/http/HttpServletRequest;Ljava/lang/Object;)V", false);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitInsn(ARETURN);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLocalVariable("this", "Lcom/seewo/datamock/test/MyTrace;", null, l0, l4, 0);
            mv.visitLocalVariable("o", "Ljava/lang/Object;", null, l0, l4, 1);
            mv.visitLocalVariable("methodParameter", "Lorg/springframework/core/MethodParameter;", null, l0, l4, 2);
            mv.visitLocalVariable("mediaType", "Lorg/springframework/http/MediaType;", null, l0, l4, 3);
            mv.visitLocalVariable("aClass", "Ljava/lang/Class;", null, l0, l4, 4);
            mv.visitLocalVariable("serverHttpRequest", "Lorg/springframework/http/server/ServerHttpRequest;", null, l0, l4, 5);
            mv.visitLocalVariable("serverHttpResponse", "Lorg/springframework/http/server/ServerHttpResponse;", null, l0, l4, 6);
            mv.visitLocalVariable("sshr", "Lorg/springframework/http/server/ServletServerHttpRequest;", null, l1, l4, 7);
            mv.visitLocalVariable("request", "Ljavax/servlet/http/HttpServletRequest;", null, l2, l4, 8);
            mv.visitMaxs(2, 9);
            mv.visitEnd();
        }
    }

    @Override
    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        classInfo = ClassInfo.builder().access(access).version(version).name(name).signature(signature)
                .superName(superName).interfaces(interfaces).build();
        cv.visit(version, access, name, signature, superName, interfaces);
        System.out.println("visit " + name + "___" + superName);
        isInterface = (access & Opcodes.ACC_INTERFACE) != 0;//记录是否为接口
        System.out.println("=============================" + classInfo.getName() + "=============================");
    }


    @Override
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature,
                exceptions);
        if (isControllerAdvice && isImplemented) {
            //实现过接口,才需要对方法进行修改
            String name1 = "supports";
            String desc1 = "(Lorg/springframework/core/MethodParameter;Ljava/lang/Class;)Z";
            String name2 = "beforeBodyWrite";
            String desc2 = "(Ljava/lang/Object;Lorg/springframework/core/MethodParameter;Lorg/springframework/http/MediaType;Ljava/lang/Class;Lorg/springframework/http/server/ServerHttpRequest;Lorg/springframework/http/server/ServerHttpResponse;)Ljava/lang/Object;";
            if (name.equals("name1") && desc.equals("desc1")) {
                ConAdviceMethodAdapter conAdviceMethodAdapter = new ConAdviceMethodAdapter(access, name, desc, signature, exceptions, mv, classInfo.getName(), name1);
                mv = conAdviceMethodAdapter;
            } else if (name.equals("name2") && desc.equals("desc2")) {
                ConAdviceMethodAdapter conAdviceMethodAdapter = new ConAdviceMethodAdapter(access, name, desc, signature, exceptions, mv, classInfo.getName(), name2);
                mv = conAdviceMethodAdapter;
            }
        }
        return mv;
    }


    @Override
    public void visitEnd() {
        System.out.println("=============================" + classInfo.getName() + "=============================");
    }
}
