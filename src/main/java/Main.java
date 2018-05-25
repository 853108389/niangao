import com.alibaba.fastjson.JSONArray;
import com.niangao.common.Config;
import com.niangao.common.aspect.PrintUtilAspect;
import com.niangao.common.utils.ControllerScanner;
import com.niangao.common.weaving.utils.WeavingUtils;
import com.niangao.http.bean.TestCase;
import com.niangao.http.utils.MyHttpUtils;
import com.niangao.test2.MyController;
import com.niangao.test2.MyTrace;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author NianGao
 * @Date 2018/5/7.
 * @description
 */
public class Main {

    public static void main(String[] args) {
        Config.init();
        System.out.println(Config.weavingPackageName);
//        System.out.println(Config.IPackageName);
//        System.out.println(Config.IClassAnnName);
//        System.out.println(Config.IMethodAnnName);
//        System.out.println(Config.EPackageBaseName);
//        testHttpUtils();
//        testdoMock();
//        testAgent();//测试代理 TODO:重复添加问题,可能需要一个标志标量
//        testAsm();//测试asm
        testTrace();//测试trace
//        testUtils();
//        testGetInfo();
//        String str = "/{roomUuid}/endpoints";
    }

//    public static void testdoMock() {
//        HeaderParams headerParams = new HeaderParams("aaaa", "bbbbb");
//        WeavingUtils.doMock("methodName", Arrays.asList("POST"),
//                Arrays.asList("/roomcenter/ceshi"), null,
//                headerParams, null, new HashMap<String, Object>() {{
//                    put("testHeader", "testValue");
//                }});
//    }

    public static void testHttpUtils() {
        MyHttpUtils.getHttpClient();
        JSONArray array = MyHttpUtils.findAllTestCase(MyHttpUtils.getCol_id()).getJSONArray("data");//获取所有用例
        List<TestCase> list = new ArrayList<>();
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                list.add(array.getObject(i, TestCase.class));
            }
        }
        System.out.println(list);
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

    public static void testUtils() {
        WeavingUtils.printParams("aaa", "bbb");
    }

    public static void testResult() {
        System.out.println("");
        System.out.println("====================result====================");
        MyController myController = new MyController();
        myController.testMethod("-------- com.niangao.test---------");
    }

    public static void testTrace() {
        ControllerScanner.getTraceInfo(MyTrace.class);
        System.out.println("Trace完成");
    }

    public static void testAgent() {
        System.out.println(111111);
        MyController myController = new MyController();
        myController.testMethod("-------- com.niangao.test---------");
    }

    public static void testGetInfo() {
//        Set<Class<?>> a = ClassTools.getClasses("com/seewo/roomcenter");
//        ControllerScanner.getInfo(a, false);
//        GetInfo.getInfo(com.niangao.common.utils);
//        GetInfo.getInfo(Info.class);
    }

    public static void testAsm() {
        ControllerScanner.genEnhanceClasses("com/niangao/test", PrintUtilAspect.getInstance(), "D://test/");
    }


}
