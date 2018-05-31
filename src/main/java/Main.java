import com.seewo.datamock.common.Config;
import com.seewo.datamock.common.aspect.ControllerAspect;
import com.seewo.datamock.common.utils.ClassScanner;
import com.seewo.datamock.http.utils.MyHttpUtils;
import com.seewo.datamock.test.MyTrace;
import jdk.internal.org.objectweb.asm.*;

/**
 * @Author NianGao
 * @Date 2018/5/7.
 * @description
 */
public class Main implements Opcodes {

    public static void main(String[] args) {
      /*  try {
            Method getTest = InterceptorConfig.class.getDeclaredMethod("getTest", null);
            System.out.println(Type.getMethodDescriptor(getTest));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        Config.init();
//        System.out.println(Config.weavingInterceptorClassName);
//        System.out.println(Config.weavingInterceptorName);
//        System.out.println(Config.weavingInterceptorPathPatterns);
//        testHttpUtils();
//        testAgent();//测试代理 TODO:重复添加问题,可能需要一个标志标量
        testAsm();//测试asm
        testTrace();//测试trace
//        Int2 int2 = new Int2();
//        int2.addInterceptors(null);
//        dump1();
//        System.out.println(Type.getType(MyTrace.MyTraceInnerClass.class).getInternalName());
//        dump();
//        testUtils();
//        testGetInfo();
    }

    //匿名
    public static byte[] dump1() {
        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "com/seewo/datamock/test2/MyTrace", null, "org/springframework/web/servlet/handler/HandlerInterceptorAdapter", null);

        cw.visitSource("MyTrace.java", null);

        cw.visitInnerClass("com/seewo/datamock/test2/MyTrace$MyTraceInnerClass", "com/seewo/datamock/test2/MyTrace", "MyTraceInnerClass", ACC_PUBLIC + ACC_STATIC);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(10, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "org/springframework/web/servlet/handler/HandlerInterceptorAdapter", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lcom/seewo/datamock/test2/MyTrace;", null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "test2", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(22, l0);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        cw.visitEnd();
        ClassScanner.writeToClass(cw, "D://test/testoooooo.class");
        return cw.toByteArray();
    }

    public static byte[] dump() {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "com/seewo/datamock/test2/MyTrace$MyTraceInnerClass", null, "java/lang/Object", null);

        cw.visitSource("MyTrace.java", null);

        cw.visitInnerClass("com/seewo/datamock/test2/MyTrace$MyTraceInnerClass", "com/seewo/datamock/test2/MyTrace", "MyTraceInnerClass", ACC_PUBLIC + ACC_STATIC);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(12, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(13, l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "Lcom/seewo/datamock/test2/MyTrace$MyTraceInnerClass;", null, l0, l2, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "test1", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(17, l0);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        cw.visitEnd();
        ClassScanner.writeToClass(cw, "D://test/tesiiiiinner.class");
        return cw.toByteArray();
    }

    //测试http请求
    public static void testHttpUtils() {
        MyHttpUtils.getHttpClient();
//        JSONObject allInterFace = MyHttpUtils.findAllInterFace("79");
//        System.out.println(allInterFace);
        //==
//        JSONObject jsonObject = MyHttpUtils.editTestCase("90");
//        System.out.println(jsonObject.getJSONObject("data"));
        //===
//        EditTestCase editTestCase = new EditTestCase();
//        editTestCase.setId("442");
//        editTestCase.setCasename("我是测试442");

        //===
//        AddTestCase addTestCase = new AddTestCase(MyHttpUtils.getCol_id(), Arrays.asList("163"), MyHttpUtils.getProject_id());
//        JSONObject jsonObject = MyHttpUtils.addTestCase(addTestCase);
//        System.out.println("A" + jsonObject);
        //====
//        Edit_Post_Json editInterFacePost = new Edit_Post_Json();
//        editInterFacePost.setCatid(MyHttpUtils.getCatid());
//        editInterFacePost.setId(MyHttpUtils.getProject_id());
//        editInterFacePost.setTitle("(POST)/roomcenter/rooms/1");
//        editInterFacePost.setPath("/roomcenter/rooms/1");
//        QueryParams queryParams = new QueryParams();
//        queryParams.setName("测试query");
//        queryParams.setValue("测试query的值");
//        List<QueryParams> req_query = new ArrayList<>();
//        req_query.add(queryParams);
//        editInterFacePost.setReq_query(req_query);
//        HeaderParams headerParams = new HeaderParams();
//        headerParams.setName("测试header");
//        headerParams.setValue("测试header的值");
//        ArrayList<HeaderParams> objects = new ArrayList<>();
//        objects.add(headerParams);
//        editInterFacePost.setReq_headers(objects);
//        editInterFacePost.setReq_body_other(JSON.toJSONString(headerParams));
//        JSONObject jsonObject = MyHttpUtils.uploadCase(editInterFacePost);
//        System.out.println("resu " + jsonObject);

    }

    //生成traceInfo
    public static void testTrace() {
//        ClassScanner.getTraceInfo(MyTrace.MyTraceInnerClass.class);
        ClassScanner.getTraceInfo(MyTrace.class);
        System.out.println("Trace完成");
    }

    //测试字节码增强
    public static void testAsm() {
        ClassScanner.genEnhanceClasses(Config.mainBaseScanPack, ControllerAspect.getInstance(), Config.mainGenClassPos);
    }

    @SuppressWarnings("all")
    public static void testInnerClass() {
//        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
//        try {
//            ClassReader classReader = new ClassReader(Type.getType(clazz).getInternalName());//分析类
//            InterceptorAdapter cv = new ControllerAdapter(classWriter, myAspect);//cv将所有事件转发给cw
//            classReader.accept(cv, ClassReader.EXPAND_FRAMES);
//            if (cv.isController() || isbuildAll) {
//                writeToClass(classWriter, basePath + clazz.getName() + ".class");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
