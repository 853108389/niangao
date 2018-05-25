package com.niangao.common.aspect;

import com.niangao.common.Config;
import com.niangao.common.bean.LocalParams;
import com.niangao.common.weaving.beans.AnnEntity;
import com.niangao.common.weaving.beans.AnnEntry;
import com.niangao.common.weaving.beans.EnumEntry;
import com.niangao.common.weaving.utils.WeavingUtils;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;

import java.util.*;

/**
 * @Author NianGao
 * @Date 2018/5/8. 打印方法参数及注解信息
 * @description
 */
@SuppressWarnings("all")
public class PrintUtilAspect extends MyAspect {

    private PrintUtilAspect() {

    }

    private static class InnerClass {
        private static MyAspect m = new PrintUtilAspect();
    }

    public static MyAspect getInstance() {
        return InnerClass.m;
    }


    /**
     * 判断是否是静态方法
     *
     * @return
     */
    private boolean isStatic() {
        return (info.getAccess() & Opcodes.ACC_STATIC) != 0;
    }

    /**
     * 返回数字所对应的操作数
     *
     * @param num
     * @return
     */
    private Integer getConstOpcode(int num) {
        switch (num) {
            case 0:
                return ICONST_0;
            case 1:
                return ICONST_1;
            case 2:
                return ICONST_2;
            case 3:
                return ICONST_3;
            case 4:
                return ICONST_4;
            case 5:
                return ICONST_5;
            default:
                return null;
        }
    }

    /**
     * 返回布尔值对应的操作数
     *
     * @param flag
     * @return
     */
    private Integer getBooleanOpcode(boolean flag) {
        return flag ? ICONST_1 : ICONST_0;
    }

    /**
     * 返回偏移量
     *
     * @return
     */
    private int getOffset() {
        return isStatic() ? 0 : 1;
    }

    //打印方法中全部参数
    @Deprecated
    private void printInsn(int offset) {
        System.out.println("===============printInsn===============");
        int paramsSize = info.getParamsSize();
        if (paramsSize > 5 && paramsSize <= 127) {
            //如果参数个数大于5 小于128
            mv.visitIntInsn(BIPUSH, paramsSize);//5个参数以上要用BIPUSH
        } else if (paramsSize >= 128) {
            mv.visitIntInsn(SIPUSH, paramsSize);//128个参数以上要用SIPUSH
        } else {
            mv.visitInsn(getConstOpcode(paramsSize)); //5个参数以下要用ICONST
        }
        mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");//创建大小为paramsSize的obj数组
        mv.visitInsn(DUP);//复制一份数组,并将复制值压入栈顶
        for (int i = 0; i < paramsSize; i++) {
            mv.visitInsn(getConstOpcode(i));//将0放入栈顶(数组下标)
            mv.visitVarInsn(ALOAD, i + offset);//放置参数i
            mv.visitInsn(AASTORE);//将参数1放入到数组中,将一个操作数栈的值存储到数组元素中的指令
            if (i != paramsSize - 1) {
                mv.visitInsn(DUP);//复制一份数组,并将复制值压入栈顶
            }
        }
        mv.visitMethodInsn(INVOKESTATIC, Config.weavingPackageName + "utils/WeavingUtils", "printParams", "([Ljava/lang/Object;)V", false);//调用方法
    }

    //打印注解参数
    @Deprecated
    private void creatAnnEntityList() {
        //创建一个LinkedList
        int pos1 = mv.newLocal(Type.getType(LinkedList.class));
        mv.visitTypeInsn(NEW, "java/util/LinkedList");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/util/LinkedList", "<init>", "()V", false);
        mv.visitVarInsn(ASTORE, pos1);
        for (AnnEntity annEntity : info.getAnnList()) {
            //创建一个LinkedList
            int pos2 = mv.newLocal(Type.getType(LinkedList.class));
            mv.visitTypeInsn(NEW, "java/util/LinkedList");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/util/LinkedList", "<init>", "()V", false);
            mv.visitVarInsn(ASTORE, pos2);
            for (AnnEntry annEntry : annEntity.getEntryList()) {
                //创建一个LinkedList
                int pos3 = mv.newLocal(Type.getType(LinkedList.class));
                mv.visitTypeInsn(NEW, "java/util/LinkedList");
                mv.visitInsn(DUP);
                mv.visitMethodInsn(INVOKESPECIAL, "java/util/LinkedList", "<init>", "()V", false);
                mv.visitVarInsn(ASTORE, pos3);
                for (EnumEntry enumEntry : annEntry.getEnumEntrylist()) {
                    //创建一个enumEntry TODO:Object用String替换了
                    int pos4 = mv.newLocal(Type.getType(EnumEntry.class));
                    mv.visitTypeInsn(NEW, Config.weavingPackageName + "beans/EnumEntry");
                    mv.visitInsn(DUP);
                    mv.visitLdcInsn(enumEntry.getEnumDesc());
                    mv.visitLdcInsn(enumEntry.getEnumValue());
                    mv.visitMethodInsn(INVOKESPECIAL, Config.weavingPackageName + "beans/EnumEntry", "<init>", "(Ljava/lang/String;Ljava/lang/Object;)V", false);
                    mv.visitVarInsn(ASTORE, pos4);
                    //add操作 添加一个enumEntry
                    mv.visitVarInsn(ALOAD, pos3);//list
                    mv.visitVarInsn(ALOAD, pos4);//obj
                    mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
                    mv.visitInsn(POP);
                }
                //创建一个AnnEntry
                int pos5 = mv.newLocal(Type.getType(AnnEntry.class));
//                mv.visitFrame(Opcodes.F_CHOP,1, null, 0, null);
                mv.visitTypeInsn(NEW, Config.weavingPackageName + "beans/AnnEntry");
                mv.visitInsn(DUP);
                mv.visitLdcInsn(annEntry.getKey());
                mv.visitInsn(getBooleanOpcode(annEntry.isEnumType()));
                mv.visitVarInsn(ALOAD, pos3);
                mv.visitMethodInsn(INVOKESPECIAL, Config.weavingPackageName + "beans/AnnEntry", "<init>", "(Ljava/lang/String;ZLjava/util/List;)V", false);
                mv.visitVarInsn(ASTORE, pos5);
                //add操作 添加一个AnnEntry
                mv.visitVarInsn(ALOAD, pos2);//list
                mv.visitVarInsn(ALOAD, pos5);//obj
                mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
                mv.visitInsn(POP);
            }
            //创建一个AnnEntity
//            mv.visitFrame(Opcodes.F_CHOP, 3, null, 0, null);
            int pos6 = mv.newLocal(Type.getType(AnnEntry.class));
            mv.visitTypeInsn(NEW, Config.weavingPackageName + "beans/AnnEntity");
            mv.visitInsn(DUP);
            mv.visitLdcInsn(annEntity.getAnnDesc());
            mv.visitVarInsn(ALOAD, pos2);
            mv.visitMethodInsn(INVOKESPECIAL, Config.weavingPackageName + "beans/AnnEntity", "<init>", "(Ljava/lang/String;Ljava/util/List;)V", false);
            mv.visitVarInsn(ASTORE, pos6);
            //add操作 添加一个AnnEntity
            mv.visitVarInsn(ALOAD, pos1);//list
            mv.visitVarInsn(ALOAD, pos6);//obj
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
            mv.visitInsn(POP);
            mv.visitVarInsn(ALOAD, pos1);
            mv.visitMethodInsn(INVOKESTATIC, Config.weavingPackageName + "utils/WeavingUtils", "printAnnInfo", "(Ljava/util/LinkedList;)V", false);
        }
    }


    //==================================================================================================================

    /**
     * 获取完整url
     *
     * @return 返回完整url参数的List集合在局部变量表中的位置
     */
    private Integer getUrl(int offset) {
        int pos = mv.newLocal(Type.getType(ArrayList.class));
        mv.visitTypeInsn(NEW, "java/util/ArrayList");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
        mv.visitVarInsn(ASTORE, pos);
        String basePath = getBaseUrl();
        Map<String, Integer> pathValueMap = getPathValueMap(offset);
        for (String url : getUrlList()) {
            mv.visitVarInsn(ALOAD, pos);//压入list
            //创建StringBuffer
            int pos1 = mv.newLocal(Type.getType(StringBuffer.class));
            mv.visitTypeInsn(NEW, "java/lang/StringBuffer");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuffer", "<init>", "()V", false);
            mv.visitVarInsn(ASTORE, pos1);
            //添加类映射
            mv.visitVarInsn(ALOAD, pos1);
            mv.visitLdcInsn(basePath);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;", false);
//            mv.visitInsn(POP);
            //添加方法映射,将方法中占位符修改为真实的值
            //如果存在占位符
            int start = 0;
            if (url.indexOf("{") != -1) {
                for (int i = start; i < url.length(); i++) {
//                    System.out.println((url.charAt(i) + "").equals("{"));
                    if ((url.charAt(i) + "").equals("{")) {
                        String str = url.substring(start, i);//获得占位符前面的字符串
                        if (!str.equals("")) {
//                            mv.visitVarInsn(ALOAD, pos1);
                            mv.visitLdcInsn(str);
                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;", false);
//                            mv.visitInsn(POP);
                        }
                        for (int j = i; j < url.length(); j++) {
                            if ((url.charAt(j) + "").equals("}")) {
                                String key = url.substring(i + 1, j);//获得占位符{xxxx}
                                System.out.println(key + "___" + pathValueMap.get(key));
                                mv.visitVarInsn(ALOAD, pathValueMap.get(key));//获得第i个参数
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;", false);
//                                mv.visitInsn(POP);
                                start = j + 1;
                                i = j + 1;
                                break;
                            }
                        }
                    }
                }
                if (start != url.length()) {
                    String end = url.substring(start);
                    mv.visitLdcInsn(end);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;", false);
                }
            } else {
                mv.visitLdcInsn(url);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;", false);
            }
            //对应toString方法
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuffer", "toString", "()Ljava/lang/String;", false);
            //对应add方法,list在最开始就被压入栈
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
            mv.visitInsn(POP);
        }
        return pos;
    }

    /**
     * 获取请求方式
     *
     * @return 请求方式的List集合所在局部变量表里的位置
     */
    private Integer getMethod() {
        int pos = mv.newLocal(Type.getType(ArrayList.class));
        mv.visitTypeInsn(NEW, "java/util/ArrayList");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
        mv.visitVarInsn(ASTORE, pos);
        List<String> method = WeavingUtils.getMethod(info.getAnnList());
        method.forEach(a -> {
            mv.visitVarInsn(ALOAD, pos);
            mv.visitLdcInsn(a);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
            mv.visitInsn(POP);
        });
        return pos;
    }

    /**
     * 获取HttpRequest对象
     *
     * @param offset
     * @return 增加偏移量后对应的位置
     */
    public Integer getHttpServletRequest(int offset) {
        int res = -1;
        List<LocalParams> paramsList = info.getParamsList();
        for (int i = 0; i < paramsList.size(); i++) {
//            System.out.println("---------->" + paramsList.get(i).getDesc());
            if ("Ljavax/servlet/http/HttpServletRequest;".equals(paramsList.get(i).getDesc())) {
//                System.out.println("---------->" + res);
                res = offset + i;
            }
        }
        return res;
    }

    /**
     * 获取@RequestBody修饰的对象
     *
     * @param offset
     * @return
     */
    public Integer getRequestBody(int offset) {
        int res = -1;
        for (Map.Entry<Integer, AnnEntity> entry : info.getParamsAnnMap().entrySet()) {
            if (entry.getValue().getAnnDesc().equals("Lorg/springframework/web/bind/annotation/RequestBody;")) {
                res = offset + entry.getKey();
            }
        }
//        if (res == -1) {
//            int pos1 = mv.newLocal(Type.getType(null));
//            mv.visitInsn(ACONST_NULL);
//        }
        return res;
    }

    /**
     * 获取@RequestHeader饰的对象
     *
     * @param offset
     * @return 返回对应hashMap所在局部变量表里的位置
     */
    public Integer getRequestHeaders(int offset) {
        int res = -1;
        Map<String, Integer> requestParamsMap = getRequestHeadersMap(offset);
        if (requestParamsMap.size() > 0) {
            int pos1 = mv.newLocal(Type.getType(HashMap.class));
            res = pos1;
            mv.visitTypeInsn(NEW, "java/util/HashMap");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false);
            mv.visitVarInsn(ASTORE, pos1);
            for (Map.Entry<String, Integer> map : requestParamsMap.entrySet()) {
                mv.visitVarInsn(ALOAD, pos1);
                mv.visitLdcInsn(map.getKey());
                mv.visitVarInsn(ALOAD, map.getValue());
                mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
                mv.visitInsn(POP);
            }
        }
        return res;
    }

    /**
     * 获取@RequestParam饰的对象
     *
     * @param offset
     * @return
     */
    public Integer getRequestParams(int offset) {
        int res = -1;
        Map<String, Integer> requestParamsMap = getRequestParamsMap(offset);
        if (requestParamsMap.size() > 0) {
            int pos1 = mv.newLocal(Type.getType(HashMap.class));
            res = pos1;
            mv.visitTypeInsn(NEW, "java/util/HashMap");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false);
            mv.visitVarInsn(ASTORE, pos1);
            for (Map.Entry<String, Integer> map : requestParamsMap.entrySet()) {
                mv.visitVarInsn(ALOAD, pos1);
                mv.visitLdcInsn(map.getKey());
                mv.visitVarInsn(ALOAD, map.getValue());
                mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
                mv.visitInsn(POP);
            }
        }
        return res;
    }

    /**
     * 获取方法名字
     *
     * @param offset
     * @return 返回对应方法名字所在局部变量表里的位置
     */
    public Integer getMethodName() {
        String owner = info.getOwner();
        mv.visitLdcInsn(owner.substring(owner.lastIndexOf("/") + 1) + "." + info.getMethodName());
        int pos = mv.newLocal(Type.getType(String.class));
        mv.visitVarInsn(ASTORE, pos);
        return pos;
    }

    /**
     * 插入前准备方法1
     * 进行插入前的汇总
     * 如果需要新增别的注解,或者有别的逻辑,需要添加的地方有这里,和PrintUtil.doMock()中的参数
     * 注意和PrintUtil.doMock()中参数顺序相同
     *
     * @return 参数顺序
     */
    public List<Integer> getInsertPosList() {
        int offset = getOffset();
        int methodNamePos = getMethodName();//方法名 req
        int methodPos = getMethod();//请求方式 req
        int urlPos = getUrl(offset);//url req
        int requestParamsPos = getRequestParams(offset);//获取RequestParams
        int requestBodyPos = getRequestBody(offset);//获取RequestBody
        int httpServletRequestPos = getHttpServletRequest(offset);//获取HttpServletRequest
        int requestHeaders = getRequestHeaders(offset);
        List<Integer> list = new ArrayList<>();
        list.add(methodNamePos);
        list.add(methodPos);
        list.add(urlPos);
        list.add(requestParamsPos);
        list.add(requestBodyPos);
        list.add(httpServletRequestPos);
        list.add(requestHeaders);
        return list;
    }

    /**
     * 插入前准备方法2
     * 获取方法描述符
     */
    public String getMethodDesc() {
        String arr[] = {""};
        Arrays.stream(WeavingUtils.class.getMethods()).forEach(a -> {
            if (a.getName().equals("doMock")) {
                arr[0] = Type.getMethodDescriptor(a);
            }
        });
        return arr[0];
    }

    /**
     * 植入的入口方法
     */
    public void weaving() {
        System.out.println(getMethodDesc());
        Type.getType(WeavingUtils.class).getMethodType("doMock");
        //将所有参数压入栈中
        getInsertPosList().stream().forEach(index -> {
            if (index == -1) {
                mv.visitInsn(ACONST_NULL);
            } else {
                mv.visitVarInsn(ALOAD, index);
            }
        });
        mv.visitMethodInsn(INVOKESTATIC, Config.weavingPackageName + "utils/WeavingUtils", "doMock", getMethodDesc(), false);
    }
    //==================================================================================================================

    //获取类上面的注解值
    public String getBaseUrl() {
        return Arrays.stream(info.getBasepath().split("/")).filter(a -> !a.equals("")).reduce("", (a, b) -> a + "/" + b);
    }

    //每个方法对应的url(可能不止一个)
    public List<String> getUrlList() {
        LinkedList<AnnEntity> annList = info.getAnnList();
        List<String> urlList = new ArrayList<>();//可能一个注解里面写了多个url
        annList.stream().forEach(annEntity -> {
            //获得key为value的所有值 及所有的url
            if (annEntity.getEntryList() == null || annEntity.getEntryList().size() == 0) {
                urlList.add("");
                //代表使用的是类上的url,没有方法上的url
            } else {
                annEntity.getEntryList().stream().filter(a -> a.getKey().equals("value")).forEach(annEntry -> {
                    System.out.println("annEntry " + annEntry);
                    annEntry.getEnumEntrylist().forEach(enumEntry -> {
                        System.out.println("enumEntry " + enumEntry);
                        String url = Arrays.stream(enumEntry.getEnumValue().toString().split("/")).filter(a -> !a.equals("")).reduce("", (a, b) -> a + "/" + b);
                        System.out.println("url " + url);
                        urlList.add(url);
                    });
                });
            }
        });
        return urlList;
    }

    //每个占位符对应的参数位置
    public Map<String, Integer> getPathValueMap(int offset) {
        Map<String, Integer> map = new HashMap<>();//关键字,第几个参数
        for (Map.Entry<Integer, AnnEntity> entry : info.getParamsAnnMap().entrySet()) {
            if (entry.getValue().getAnnDesc().equals("Lorg/springframework/web/bind/annotation/PathVariable;")) {
                if (entry.getValue().getEntryList().size() == 0 || entry.getValue().getEntryList().get(0).getEnumEntrylist().size() == 0) {
                    //PathValue注解里不写参数的情况
                } else {
                    map.put(entry.getValue().getEntryList().get(0).getEnumEntrylist().get(0).getEnumValue().toString(), entry.getKey() + offset);
                }
            }
        }
        return map;
    }

    //获取@RequestParam修饰的对象对应的参数位置
    public Map<String, Integer> getRequestParamsMap(int offset) {
        Map<String, Integer> map = new HashMap<>();//关键字,第几个参数
        for (Map.Entry<Integer, AnnEntity> entry : info.getParamsAnnMap().entrySet()) {
            if (entry.getValue().getAnnDesc().equals("Lorg/springframework/web/bind/annotation/RequestParam;")) {
                if (entry.getValue().getEntryList().size() == 0 || entry.getValue().getEntryList().get(0).getEnumEntrylist().size() == 0) {
                    System.out.println("error @RequestParam里没有值");
                    //@RequestParam里没有值
                } else {
                    List<AnnEntry> entryList = entry.getValue().getEntryList();
                    String val = entryList.stream().peek(System.out::println)
                            .filter(a -> (a.getKey().equals("value") || a.getKey().equals("name")))//保留name或val
                            .map(annEntry -> annEntry.getEnumEntrylist().get(0).getEnumValue().toString())//保留他们的值
                            .filter(s -> !s.equals("")).limit(1).reduce("", (a, b) -> a + b);//获取值
                    map.put(val, entry.getKey() + offset);
                }
            }
        }
        return map;
    }

    //获取@RequestHeader修饰的对象对应的参数位置
    public Map<String, Integer> getRequestHeadersMap(int offset) {
        Map<String, Integer> map = new HashMap<>();//关键字,第几个参数
        for (Map.Entry<Integer, AnnEntity> entry : info.getParamsAnnMap().entrySet()) {
            if (entry.getValue().getAnnDesc().equals("Lorg/springframework/web/bind/annotation/RequestHeader;")) {
                if (entry.getValue().getEntryList().size() == 0 || entry.getValue().getEntryList().get(0).getEnumEntrylist().size() == 0) {
                    System.out.println("error @RequestHeader");
                    //@RequestParam里没有值
                } else {
                    List<AnnEntry> entryList = entry.getValue().getEntryList();
                    String val = entryList.stream().peek(System.out::println)
                            .filter(a -> (a.getKey().equals("value") || a.getKey().equals("name")))//保留name或val
                            .map(annEntry -> annEntry.getEnumEntrylist().get(0).getEnumValue().toString())//保留他们的值
                            .filter(s -> !s.equals("")).limit(1).reduce("", (a, b) -> a + b);//获取值
                    map.put(val, entry.getKey() + offset);
                }
            }
        }
        return map;
    }

    //==================================================================================================================
    //Type.getType(desc).getClassName(); 根据类描述符获得类的类名
    @Override
    public void before() {
        if (mv != null) {
        }
    }

    @Override
    public void after(int opcode) {
        if (mv != null) {
            if (opcode == ATHROW) {
                //最后是throw,  不做处理
            } else {
                if (opcode == RETURN) {
                    mv.visitInsn(ACONST_NULL);
                } else {
                    mv.visitInsn(DUP);
                }
                weaving();
            }
        }
    }
}
