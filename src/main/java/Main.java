import com.seewo.datamock.common.Config;
import com.seewo.datamock.common.utils.ClassScanner;
import com.seewo.datamock.http.utils.MyHttpUtils;
import com.seewo.datamock.test.MyTrace;
import jdk.internal.org.objectweb.asm.Opcodes;

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
        MyHttpUtils.getHttpClient();//初始化数据
//        System.out.println(Config.weavingInterceptorClassName);
//        System.out.println(Config.weavingInterceptorName);
//        System.out.println(Config.weavingInterceptorPathPatterns);
//        testHttpUtils();
//        testAgent();//测试代理 TODO:重复添加问题,可能需要一个标志标量
        testAsm();//测试asm
        testTrace();//测试trace
        System.out.println("aaaa".split(";")[0]);
//        Int2 int2 = new Int2();
//        int2.addInterceptors(null);
//        dump1();
//        System.out.println(Type.getType(MyTrace.MyTraceInnerClass.class).getInternalName());
//        dump();
//        testUtils();
//        testGetInfo();
    }

    //匿名

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
        ClassScanner.getTraceInfo(MyTrace.class);
        System.out.println("Trace完成");
    }

    //测试字节码增强
    public static void testAsm() {
        ClassScanner.genEnhanceClasses(Config.mainBaseScanPack, Config.mainGenClassPos);
    }


}
