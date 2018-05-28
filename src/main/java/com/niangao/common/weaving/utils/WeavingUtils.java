package com.niangao.common.weaving.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.niangao.common.weaving.beans.AnnEntity;
import com.niangao.common.weaving.beans.AnnEntry;
import com.niangao.common.weaving.beans.EnumEntry;
import com.niangao.http.bean.FormParams;
import com.niangao.http.bean.HeaderParams;
import com.niangao.http.bean.QueryParams;
import com.niangao.http.utils.MyHttpUtils;
import com.niangao.http.vo.Edit;
import com.niangao.http.vo.Edit_Get;
import com.niangao.http.vo.Edit_Post_Form;
import com.niangao.http.vo.Edit_Post_Json;
import jdk.internal.org.objectweb.asm.Type;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author NianGao
 * @Date 2018/5/10.
 * @description 该类会以字节码的形式植入到目标类中
 */

@SuppressWarnings("all")
public class WeavingUtils {


    /**
     * 增强方法接口,增强方法的入口,
     * 该方法禁止重载
     *
     * @param returnObj          req 返回值
     * @param methodName         req 类名.方法名
     * @param methodList         req 上传方法(GET POST..) 可能有多个
     * @param urlList            req 该接口的请求路径   可能有多个
     * @param params             !re @requestParam封装后获得的键值对
     * @param requestBody        !re @requestBody封装后的对象
     * @param httpServletRequest !re HttpSetvletRequest封装后的对象
     * @param headers            !re @requestHeader封装后的键值对
     */
    public static void doMock(Object returnObj, String methodName, List<String> methodList, List<String> urlList, HashMap<String, Object> params, Object requestBody, HttpServletRequest httpServletRequest, HashMap<String, Object> headers) {
        try {
            System.out.println("------------>mock<------------");
            System.out.println(urlList);
            System.out.println(methodList);
            for (String path : urlList) {
                for (String method : methodList) {
                    String status = chooseUploadType(method, httpServletRequest, params, requestBody);
                    System.out.println("status:" + status);
                    Set<HeaderParams> requestHeader = getRequestHeader(headers, httpServletRequest);
                    String title = "(" + method + ")" + path;
                    switch (status) {
                        case "NONE":
                            System.out.println("上传错误..");
                            break;
                        case "GET":
                            List<QueryParams> queryParams = getQueryParams(params, httpServletRequest);
                            Edit edit_get = new Edit_Get();
                            edit_get.setReq_query(queryParams);
                            upload(edit_get, title, path, method, requestHeader, returnObj);
                            break;
                        case "POST_FORM":
                            List<FormParams> formParams = getFormParams(params, httpServletRequest);
                            Edit edit_post_form = new Edit_Post_Form();
                            edit_post_form.setReq_body_form(formParams);
                            upload(edit_post_form, title, path, method, requestHeader, returnObj);
                            break;
                        case "POST_JSON":
                            String reqJsonBody = getReqJsonBody(requestBody, httpServletRequest);
                            Edit edit_post_json = new Edit_Post_Json();
                            edit_post_json.setReq_body_other(reqJsonBody);
                            upload(edit_post_json, title, path, method, requestHeader, returnObj);
                            break;
                        case "POST_FILE":
//                            TODO:处理文件的情况 @Multipart注解情况适配
                            break;
                        default:
                            System.out.println("----未知的错误");
                            break;
                    }
                }
            }
            System.out.println("------------>mockover<------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断上传方式
    public static String chooseUploadType(String method, HttpServletRequest httpServletRequest, HashMap<String, Object> params, Object requestBody) {
        String status = "NONE";
        /*
            requestBody
            requestParams
            request
         */
        if (method.equals("GET")) {
            //GET方式
            status = "GET";
        } else {
            //POST方式
            if (httpServletRequest != null && httpServletRequest.getHeader("Content-Type") != null) {
                String header = httpServletRequest.getHeader("Content-Type");
                switch (header) {
                    case "application/json":
                        //post_json
                        status = "POST_JSON";
                        break;
                    case "application/x-www-form-urlencoded":
                        //post_form
                        status = "POST_FORM";
                        break;
                    case "multipart/form-data":
                        status = "POST_FILE";
                        //post_file  TODO:处理文件的情况 @Multipart注解情况适配
                        break;
                }
            } else {
                //没有request对象,
                if (requestBody != null) {
                    //如果有requestBody 当json处理
                    status = "POST_JSON";
                    //post_json;
                } else if (params != null && params.size() > 1) {
                    //如果有requestParam 当form处理
                    status = "POST_FORM";
                    //post_form
                } else {
                    status = "POST_JSON"; //默认json把
//                    既没有 requestParam 也没有requestBody  TODO: 使用@Model或者@ModelAndView 接受参数了
                }
            }
        }
        return status;
    }

    //获取QueryParams对象(GET格式)
    public static List<QueryParams> getQueryParams(HashMap<String, Object> params, HttpServletRequest request) {
        List<QueryParams> list = new ArrayList<>();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> map : params.entrySet()) {
                QueryParams queryParams = new QueryParams();
                queryParams.setName(map.getKey());
                list.add(queryParams);
            }
        } else {
            if (request != null) {
                Map<String, String[]> parameterMap = request.getParameterMap();
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    QueryParams queryParams = new QueryParams();
                    queryParams.setName(entry.getKey());
                    list.add(queryParams);
                }
            }
        }
        return list;
    }

    //获取FormParams对象(POST.Form格式)
    public static List<FormParams> getFormParams(HashMap<String, Object> params, HttpServletRequest request) {
        List<FormParams> list = new ArrayList<>();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> map : params.entrySet()) {
                FormParams formParams = new FormParams();
                formParams.setName(map.getKey());
                list.add(formParams);
            }
        } else {
            if (request != null) {
                Map<String, String[]> parameterMap = request.getParameterMap();
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    FormParams formParams = new FormParams();
                    formParams.setName(entry.getKey());
                    list.add(formParams);
                }
            }
        }
        return list;
    }

    //获得JSONObj对象(POST.JSON格式)
    public static String getReqJsonBody(Object requestBody, HttpServletRequest request) {
        String s = "{}";
        if (requestBody != null) {
            s = JSONObject.toJSONString(requestBody);
        }
        return s;
    }

    //获得RequestHeader对象()
    public static Set<HeaderParams> getRequestHeader(HashMap<String, Object> headers, HttpServletRequest request) {
        Set<HeaderParams> list = new HashSet<>();
        if (request != null) {
            //从request里没有值,从headers里拿
            Enumeration e = request.getHeaderNames();
            while (e.hasMoreElements()) {
                String headerName = (String) e.nextElement();
                String headValue = request.getHeader(headerName);
                HeaderParams headerParams = new HeaderParams();
                headerParams.setName(headerName);
                headerParams.setValue(request.getHeader(headerName));
                list.add(headerParams);
            }
        } else {
            if (headers != null && headers.size() > 0) {
                //headers里有值,从headers里拿
                for (Map.Entry<String, Object> map : headers.entrySet()) {
                    HeaderParams headerParams = new HeaderParams();
                    headerParams.setName(map.getKey());
                    headerParams.setValue(map.getValue().toString());
                    list.add(headerParams);
                }
            }
        }
        return list;
    }

    //进行上传
    public static void upload(Edit edit, String title, String path, String method,
                              Set<HeaderParams> requestHeader, Object returnObj) {
        System.out.println("------------>upload<------------");
        edit.setTitle(title.length() > 100 ? title.substring(0, 100) : title);//方法名字
        edit.setPath(path);//请求路径
        edit.setMethod(method);
        edit.setCatid(MyHttpUtils.getCatid());
        edit.setId(MyHttpUtils.getProject_id());
   /*     if (edit.getReq_headers() != null && edit.getReq_headers().size() != 0) {
        } else {
        }*/
        edit.setReq_headers(requestHeader);
        edit.setRes_body(JSONObject.toJSONString(returnObj));
        System.out.println(JSONObject.toJSONString(edit));
        MyHttpUtils.uploadCase(edit);
        System.out.println("------------>upload<------------");
    }

    //==================================================================================================================
    //获得请求方式
    public static List<String> getMethod(LinkedList<AnnEntity> annList) {
        List<String> methodList = new ArrayList<>();
        annList.stream().forEach(annEntity -> {
            String method = getMethod(annEntity.getAnnDesc());
            methodList.add(method);
            if (method == "") {
                annEntity.getEntryList().stream().filter(entry -> entry.getKey().equals("method")).forEach(a -> {
                    a.getEnumEntrylist().forEach(enumEntry -> {
                        methodList.add(enumEntry.getEnumValue().toString());
                    });
                });
            }
        });
        return methodList;
    }

    //获取请求方式2
    private static String getMethod(String annDesc) {
        String methodType = "";
        switch (annDesc) {
            case "Lorg/springframework/web/bind/annotation/PostMapping;":
                return "POST";
            case "Lorg/springframework/web/bind/annotation/PutMapping;":
                return "PUT";
            case "Lorg/springframework/web/bind/annotation/DeleteMapping;":
                return "DELETE";
            case "Lorg/springframework/web/bind/annotation/GetMapping;":
                return "GET";
            case "Lorg/springframework/web/bind/annotation/RequestMapping;":
                break;
        }
        return methodType;
    }

    //==================================================================================================================
    //获取请求体
    @Deprecated
    public static List<QueryParams> getParams(Object... args) {
        List<QueryParams> list = new LinkedList<>();
        for (Object obj : args) {
            if (obj instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest) obj;
                Map<String, String[]> parameterMap = request.getParameterMap();
                QueryParams queryParams = new QueryParams();
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    String val = entry.getValue()[0];
                    queryParams.setName(entry.getKey());
                    list.add(queryParams);
                }
            }
        }
        return list;
    }

    //打印注解信息
    @Deprecated
    public static void printAnnInfo(LinkedList<AnnEntity> annList) {
        System.out.print(getAnnInfo(annList));
    }


    @Deprecated
    //获取请求头
    public static List<HeaderParams> getHeaders(Object... args) {
        List<HeaderParams> list = new LinkedList<>();
        for (Object obj : args) {
            if (obj instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest) obj;
                Enumeration e = request.getHeaderNames();
                while (e.hasMoreElements()) {
                    String headerName = (String) e.nextElement();
                    String headValue = request.getHeader(headerName);
                    HeaderParams headerParams = new HeaderParams();
                    headerParams.setName(headerName);
                    headerParams.setValue(request.getHeader(headerName));
                    list.add(headerParams);
                }
            }
        }
        return list;
    }

    @Deprecated
    public static String getAnnInfo(LinkedList<AnnEntity> annList) {
        StringBuilder sb = new StringBuilder();
        for (AnnEntity annEntity : annList) {
            String annDesc = annEntity.getAnnDesc();//注解名字描述符
            sb.append("@" + Type.getType(annDesc).getClassName() + "(");
            sb.append("\n");
            for (AnnEntry annEntry : annEntity.getEntryList()) {
                boolean isEnumType = annEntry.isEnumType();
                String key = annEntry.getKey();
                sb.append("\t" + key + " = ");
                if (isEnumType) {
                    sb.append("{");
                } else {
                    sb.append("\"");
                }
                for (EnumEntry enumEntry : annEntry.getEnumEntrylist()) {
                    if (isEnumType) {
                        sb.append(Type.getType(enumEntry.getEnumDesc()).getClassName());//获取类名
                        sb.append(".");
                    }
                    sb.append(enumEntry.getEnumValue());//枚举值
                    sb.append(", ");//枚举值
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.deleteCharAt(sb.length() - 1);
                if (isEnumType) {
                    sb.append("}");
                } else {
                    sb.append("\"");
                }
                sb.append("\n");
            }
            sb.append(")");
            sb.append("\n");
        }
        return sb.toString();
    }

    //打印方法的参数信息
    @Deprecated
    public static void printParams(Object... args) {
        System.out.println(args.length + "个参数:");
        for (int i = 0; i < args.length; i++) {
            dealObj(args[i]);
            System.out.print(i + ": " + args[i]);
            if (i < args.length - 1) {
                System.out.print("____");
            }
        }
        System.out.println();
    }

    @Deprecated
    public static void dealObj(Object obj) {
        if (obj != null) {
            if (obj instanceof HttpServletRequest) {
                dealRequestObj(obj);
            } else {
                dealnormalObj(obj);
            }
        }
    }

    @Deprecated
    public static void dealRequestObj(Object obj) {

        try {
            StringBuilder sb = new StringBuilder();
//            RequestFacade
            if (obj instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest) obj;
                sb.append("参数和值: ");
                Map<String, String[]> parameterMap = request.getParameterMap();
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    String val = Arrays.stream(entry.getValue()).reduce("", (a, b) -> a + "__" + b);
                    sb.append(entry.getKey() + "=" + val);
                }
                sb.append("\n");
                sb.append("mine: " + request.getContentType());
                sb.append("\n");
                sb.append("请求头: ");
                Enumeration e = request.getHeaderNames();
                while (e.hasMoreElements()) {
                    String headerName = (String) e.nextElement();
                    String headValue = request.getHeader(headerName);
                    sb.append(headerName + "=" + headValue + "  ");
                }
                sb.append("\n");
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
            System.out.println("HttpServletRequest异常");
            e.printStackTrace();
//        }
        }
    }

    @Deprecated
    public static void dealnormalObj(Object obj) {
        String s = JSONObject.toJSONString(obj);
        JSONObject jsonObject = JSON.parseObject(s);
        System.out.println("json: " + s);
    }

    //获得拼接后的url  String method = getMethod(annEntity.getAnnDesc());
    @Deprecated
    public static List<String> getUrl(LinkedList<AnnEntity> annList, Map<Integer, AnnEntity> map, Object... args) {
        //注解里的url
        List<String> urlList = new ArrayList<>();//可能一个注解里面写了多个url
        annList.stream().forEach(annEntity -> {
            //获得key为value的所有值
            annEntity.getEntryList().stream().filter(a -> a.getKey().equals("value")).forEach(annEntry -> {
                annEntry.getEnumEntrylist().forEach(enumEntry -> {
                    urlList.add(enumEntry.getEnumValue().toString());
                });
            });
        });
        //url里的参数对应的值
        Map<String, String> map2 = new HashMap<>();
        for (Map.Entry<Integer, AnnEntity> entry : map.entrySet()) {
            entry.getValue().getAnnDesc().equals("Lorg/springframework/web/bind/annotation/PathVariable;");
            map2.put(args[entry.getKey()].toString(), entry.getValue().getEntryList().get(0).getEnumEntrylist().get(0).getEnumValue().toString());
        }

        //进行参数替换
        urlList.forEach(url -> {
            String pattern = "\\{(.*?)\\}";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(url);
            while (m.find()) {
//                System.out.println(map2.get(m.group(0)));
                m.replaceAll(map2.get(m.group(0)));
            }

        });
        return urlList;
    }


}
