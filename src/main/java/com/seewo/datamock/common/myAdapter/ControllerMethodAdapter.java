package com.seewo.datamock.common.myAdapter;

import com.seewo.datamock.common.Config;
import com.seewo.datamock.common.aspect.MyAspect;
import com.seewo.datamock.common.bean.LocalParams;
import com.seewo.datamock.common.weaving.beans.AnnEntity;
import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.commons.AdviceAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @Author NianGao
 * @Date 2018/5/8.
 * @description 方法访问器适配器
 */
public class ControllerMethodAdapter extends AdviceAdapter {
    private String owner;
    private boolean isControllerMethod;
    private MyAspect aspect;

    public ControllerMethodAdapter(int access, String name, String desc,
                                   MethodVisitor mv, String owner) {
        this(access, name, desc, null, null, mv, owner, null);
    }

    public ControllerMethodAdapter(int access, String name, String desc, String signature, String[] exceptions,
                                   MethodVisitor mv, String owner, MyAspect aspect) {
        super(Opcodes.ASM5, mv, access, name, desc);
        this.owner = owner;
        this.aspect = aspect;
        aspect.getInfo().setOwner(owner);
        aspect.getInfo().setMethodName(name);
        aspect.getInfo().setMethodDesc(desc);
        aspect.getInfo().setAccess(access);
        aspect.getInfo().setParamsList(new LinkedList<>());//参数列表
        aspect.getInfo().setParamsSize(Type.getArgumentTypes(desc).length);//使用这种方式获得,记着
        aspect.getInfo().setAnnList(new LinkedList<>());//方法注解列表
        aspect.getInfo().setParamsAnnMap(new HashMap<>());//参数注解列表
        Arrays.stream(Type.getArgumentTypes(desc)).map(a -> a.getDescriptor()).forEach(a -> {
            LocalParams localParams = new LocalParams("", a);
            aspect.getInfo().getParamsList().add(localParams);
        });
//        offset = (access & Opcodes.ACC_STATIC) != 0 ? 0 : 1;//判断是否是静态,也记着 不是静态的话
    }

    //方法参数注解 i 是位置 s是描述符
    @Override
    public AnnotationVisitor visitParameterAnnotation(int i, String s, boolean b) {
        AnnotationVisitor av = mv.visitParameterAnnotation(i, s, b);
        if (!isControllerMethod) {
            return av;
        }
        AnnEntity annEntity = new AnnEntity(s);
        aspect.getInfo().getParamsAnnMap().put(i, annEntity);
        return new ControllerParamsAnoAdapter(av, aspect, i);
    }

//    方法参数名
//    @Override
//    public void visitParameter(String name, int access) {
////        System.out.println(name + "__" + access);
//        aspect.getInfo().getParamsList().add(new LocalParams(name));
//        mv.visitParameter(name, access);
//    }

    //    方法注解
    @Override
    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        System.out.println("方法注解: " + s);
        if (Config.IMethodAnnName.stream().anyMatch(key -> s.equals(key))) {
            //如果是Controller
            isControllerMethod = true;
        } else {
            isControllerMethod = false;
        }
        AnnotationVisitor av = mv.visitAnnotation(s, b);
        AnnEntity annEntity = new AnnEntity(s);
        aspect.getInfo().getAnnList().push(annEntity);
        return new ControllerMethodAnoAdapter(av, aspect);
    }


    @Override
    protected void onMethodEnter() {
        if (aspect != null) {
            if (isControllerMethod) {
                aspect.before();
            }
        }
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (aspect != null) {
            if (isControllerMethod) {
                aspect.after(opcode);
            }
        }
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals);
    }


//      /**
//     * 访问局部变量表
//     * @param name      参数名
//     * @param desc      参数描述符
//     * @param signature 参数签名
//     * @param start
//     * @param end
//     * @param index     参数位置
//     * @return void
//     * @exception:
//     * @see:
//     */
//    @Override
//    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
//        if (offset <= index && index < paramsSize + offset) { //方法中参数的位置
//            LocalVariableNode fieldNode = new LocalVariableNode(name, desc, signature, new LabelNode(start), new LabelNode(end), index);
//            localVariableNodeList.add(fieldNode);
//        }
//        if (index == paramsSize + offset - 1) {
//            System.out.println("localVariableNodeList" + localVariableNodeList);
//        }
//        mv.visitLocalVariable(name, desc, signature, start, end, index);
//    }
}
