package com.seewo.datamock.http.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seewo.datamock.common.Config;
import com.seewo.datamock.http.Api;
import com.seewo.datamock.http.bean.TestCase;
import com.seewo.datamock.http.vo.*;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

/**
 * @Author NianGao
 * @Date 2018/5/16.
 * @description http请求封装 对应的url封装
 */
@SuppressWarnings("all")
public class MyHttpUtils {
    private static String group_id;//分组id
    private static String project_id;//当前的项目id
    private static String catid;//当前的分类目录
    private static String col_id;//测试用例里面的id
    private static String charset = "utf-8";
    private static RequestConfig config = RequestConfig.custom().setConnectTimeout(6000).setSocketTimeout(6000)
            .setCookieSpec(CookieSpecs.STANDARD).build(); // 设置超时及cookie策略 注:不设置就无法获得到cookie
    //================各种缓存
    private static Map<String, String> groups = new HashMap<>();//分组目录
    private static Map<String, String> projects = new HashMap<>();//项目
    private static Map<String, String> cats = new HashMap<>();//接口目录
    private static Map<String, String> titles = new HashMap<>();//接口
    private static Map<String, String> cols = new HashMap<>();//测试用例目录
    private static List<TestCase> testCases = new ArrayList<>();//测试用例
    //cookies信息
    private static HttpClientContext context = HttpClientContext.create();

    //===============================================================通用方法
    public static JSONObject doPost(String url, String jsonObj) {
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(config);
        try {
            StringEntity entity = new StringEntity(jsonObj, charset);
            entity.setContentType("application/json");//注:不设置参数就无法以json格式提交
            httpPost.setEntity(entity);
            //执行方法
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                return JSON.parseObject(EntityUtils.toString(response.getEntity()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static JSONObject doPost(String url, Object obj) {
        return doPost(url, JSON.toJSONString(obj));
    }

    public static JSONObject doPost(String url, Map<String, String> map) {
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        //创建Post
        httpPost.setConfig(config);
        //设置参数
        List<NameValuePair> list = getNameValuePair(map);
        try {
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                httpPost.setEntity(entity);
                //执行方法
                CloseableHttpResponse response = httpClient.execute(httpPost);
                //======================================
                if (response.getStatusLine().getStatusCode() == 200) {
                    return JSON.parseObject(EntityUtils.toString(response.getEntity()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject doGet(String url, Map<String, String> map) {
        try {
            CloseableHttpClient httpClient = getHttpClient();
            List<NameValuePair> list = getNameValuePair(map);
            //执行方法
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(list), charset);
            System.out.println(url);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(config);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                return JSON.parseObject(EntityUtils.toString(response.getEntity()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject doGet(String url, String... args) {
        HashMap<String, String> map = new HashMap<>();
        if (args.length % 2 != 0) {
            System.out.println("参数个数不正确");
        }
        for (int i = 0; i < args.length; i = i + 2) {
            map.put(args[i], args[i + 1]);
        }
        return doGet(url, map);
    }

    //获取HttpClient 如果没有Cookies 默认执行登入方法储存cookies
    public static CloseableHttpClient getHttpClient() {
        CookieStore cookieStore = context.getCookieStore();
        if (cookieStore == null) {
            cookieStore = new BasicCookieStore();
            context.setCookieStore(cookieStore);
            login();
        }
        return HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    }

    //登录 目的是获得cookies
    public static boolean login() {
        Login login = Login.getLogin();
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        boolean isLoginSuccess = false;
        try {
            httpClient = getHttpClient();
            httpPost = new HttpPost(Api.login);
            httpPost.setConfig(config);
            StringEntity entity = new StringEntity(JSON.toJSONString(login), charset);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {//如果不是代表失败
                List<Cookie> cookies = context.getCookieStore().getCookies();
                HttpEntity responseEntity = response.getEntity();
                JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(responseEntity));
                if (jsonObject.getInteger("errcode") == 0) {
                    isLoginSuccess = true;
                    //登入成功
                    afterLogin();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isLoginSuccess;
    }

    //用于处理登录后的一系列缓存工作
    public static void afterLogin() {
        updateGroups();
        updateProject();
        updateCols();
        updateCat();
        updateInterfaces();
        updateTestCases();
    }

    //获取所有的分组目录
    public static void updateGroups() {
        JSONObject allGroups = findAllGroups();
        JSONArray array = allGroups.getJSONArray("data");
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                JSONObject obj = array.getJSONObject(i);
                String title = obj.get("group_name").toString();
                if (!groups.containsKey(title)) {
                    groups.put(title, obj.getString("_id").toString());//名字,id
                }
            }
        }
        groups.forEach((key, value) -> {
            if (key.equals(Config.groupName)) {
                MyHttpUtils.group_id = value;
            }
        });
    }

    //获取所有的接口名称,目的是判断是否接口已经添加过,如果添加过,跳过接口添加,直接进入测试用例提交
    public static void updateInterfaces() {
        JSONArray array = findAllInterFace(getCatid()).getJSONObject("data").getJSONArray("list");
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                JSONObject obj = array.getJSONObject(i);
                String title = obj.get("title").toString();
                if (!titles.containsKey(title)) {
                    titles.put(title, obj.getString("_id").toString());//名字,id
                }
            }
        }
    }

    //获取所有的测试用例,目的是判断该用例是否已经添加过,如果添加过,跳过该用例的添加,本次提交结束
    public static void updateTestCases() {
        MyHttpUtils.getHttpClient();
        JSONArray array = MyHttpUtils.findAllTestCase(MyHttpUtils.getCol_id()).getJSONArray("data");//获取所有用例
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                testCases.add(array.getObject(i, TestCase.class));
            }
        }
    }

    //获取所有的目录信息
    public static void updateCat() {
        JSONArray array = findAllCat(MyHttpUtils.getProject_id()).getJSONArray("data");
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                JSONObject obj = array.getJSONObject(i);
                String name = obj.get("name").toString();
                String id = obj.get("_id").toString();
                if (!cats.containsKey("name")) {
                    cats.put(name, id);
                }
            }
        }
        cats.forEach((key, value) -> {
            if (key.equals(Config.catName)) {
                MyHttpUtils.catid = value;
            }
        });
    }

    //所有的项目目录信息
    public static void updateProject() {
        JSONArray array = findAllProject(MyHttpUtils.getGroup_id()).getJSONObject("data").getJSONArray("list");
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                JSONObject obj = array.getJSONObject(i);
                String name = obj.get("name").toString();
                if (!projects.containsKey("name")) {
                    String id = obj.get("_id").toString();
                    projects.put(name, id);
                }
            }
        }
        projects.forEach((key, value) -> {
            if (key.equals(Config.projectName)) {
                MyHttpUtils.project_id = value;
            }
        });
    }

    //所有的测试用例目录信息
    public static void updateCols() {
        JSONArray array = findAllCase(MyHttpUtils.getProject_id()).getJSONArray("data");
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                JSONObject obj = array.getJSONObject(i);
                String name = obj.get("name").toString();
                if (!cols.containsKey("name")) {
                    String id = obj.get("_id").toString();
                    cols.put(name, id);
                }
            }
        }
        cols.forEach((key, value) -> {
            if (key.equals(Config.colName)) {
                MyHttpUtils.col_id = value;
            }
        });
    }

    //获取父类目录id
    public static String getCatid() {
        return catid;
    }

    //获得测试用例的id
    public static String getol_id() {
        return col_id;
    }

    public static String getCol_id() {
        return col_id;
    }

    //获取项目id
    public static String getProject_id() {
        return project_id;
    }

    public static String getGroup_id() {
        return group_id;
    }

    //工具方法: 获取参数List
    private static List<NameValuePair> getNameValuePair(Map<String, String> map) {
        List<NameValuePair> list = new ArrayList<>();
        //设置参数
        if (map != null) {
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
        }
        return list;
    }


    //===============================================================对应接口Api
    //添加接口
    private static JSONObject addInterFace(AddInterface addInterface) {
        return doPost(Api.addinterface, addInterface);
    }

    //编辑接口
    private static JSONObject editInterFace(Edit editInterFacePost) {
        return doPost(Api.editInterface, editInterFacePost);
    }

    //查看某目录下的所有的接口信息
    public static JSONObject findAllInterFace(String catid) {
        return doGet(Api.findAllInterFace, "catid", catid);
    }

    //添加测试用例
    public static JSONObject addTestCase(AddTestCase addTestCase) {
        return doPost(Api.addTestCase, addTestCase);
    }

    //编辑测试用例
    public static JSONObject editTestCase(EditTestCase editTestCase) {
        return doPost(Api.editTestCase, editTestCase);
    }

    //查看某测试目录下的所有的测试用例
    public static JSONObject findAllTestCase(String col_id) {
        return doGet(Api.findAllTestCase, "col_id", col_id);
    }

    //查看某工程下所有的目录
    public static JSONObject findAllCat(String project_id) {
        return doGet(Api.findAllCat, "project_id", project_id);
    }

    //查看分组内所有的项目信息
    public static JSONObject findAllProject(String group_id) {
        return doGet(Api.findAllProject, "group_id", group_id);
    }

    //查看分组内所有的项目信息
    public static JSONObject findAllCase(String project_id) {
        return doGet(Api.findAllCase, "project_id", project_id);
    }

    //查看所有的分组
    public static JSONObject findAllGroups() {
        return doGet(Api.findAllGroups);
    }

    //===============================================================业务方法
    //判断接口是否已经存在
    private static Optional<String> isInterFaceExsit(AddInterface addInterface) {
        if (titles == null || titles.size() == 0) {//如果为空则更新
            updateInterfaces();
        }
        String id = titles.get(addInterface.getTitle());
        Optional<String> optional = Optional.ofNullable(id);
        return optional;
    }

    //创建和编辑接口, 创建和添加用例,一步解决
    public static JSONObject uploadCase(Edit edit) {
        try {
            AddInterface addInterface = new AddInterface(edit.getCatid(), edit.getId(), edit.getTitle(), edit.getPath(), edit.getMethod());
            Optional<String> optional = isInterFaceExsit(addInterface);//判断接口是否存在
            String res = optional.orElseGet(() -> {
                //如果不存在
                JSONObject addInterFaceRes = addInterFace(addInterface);
                if (addInterFaceRes.containsKey("data")) {
                    String id = addInterFaceRes.getJSONObject("data").getString("_id");//添加接口并获取id
                    edit.setId(id);
                    JSONObject jsonObject = editInterFace(edit);//编辑接口
                    updateInterfaces();//更新接口信息
                    return id;
                } else {
                    //没有data,代表出了预期之外的错误;
                    System.out.println("error" + addInterFaceRes);
                    return null;
                }
            });//返回接口id;
            TestCase o = new TestCase(edit);
            if (testCases.contains(o)) {
                //该测试用例重复
                System.out.println("重复添加--------------------");
                System.out.println(edit.getTitle());
                return null;
            } else {
                testCases.add(o);
                AddTestCase addTestCase = new AddTestCase(MyHttpUtils.getCol_id(), Arrays.asList(res), MyHttpUtils.getProject_id());
                addTestCase(addTestCase);//添加测试用例
                JSONArray data = findAllTestCase(MyHttpUtils.getCol_id()).getJSONArray("data");//获取所有用例
                String caseId = data.getJSONObject(data.size() - 1).getString("_id");//获取刚添加的用例的id
                EditTestCase editTestCase = new EditTestCase(edit, caseId);
                JSONObject jsonObject = editTestCase(editTestCase);//进行测试用例编辑
                return jsonObject;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
