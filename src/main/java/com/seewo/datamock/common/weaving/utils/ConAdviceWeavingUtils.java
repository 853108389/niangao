package com.seewo.datamock.common.weaving.utils;

import com.alibaba.fastjson.JSONObject;
import com.seewo.datamock.http.bean.FormParams;
import com.seewo.datamock.http.bean.HeaderParams;
import com.seewo.datamock.http.bean.QueryParams;
import com.seewo.datamock.http.utils.MyHttpUtils;
import com.seewo.datamock.http.vo.Edit;
import com.seewo.datamock.http.vo.Edit_Get;
import com.seewo.datamock.http.vo.Edit_Post_Form;
import com.seewo.datamock.http.vo.Edit_Post_Json;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @Author NianGao
 * @Date 2018/5/30.
 * @description
 */
public class ConAdviceWeavingUtils {

    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    //获取json格式的请求体
    public static String getRequestPostStr(HttpServletRequest request) {
        String res = "";
        try {
            byte[] buffer = getRequestPostBytes(request);
            String charEncoding = request.getCharacterEncoding();
            if (charEncoding == null) {
                charEncoding = "UTF-8";
            }
            res = new String(buffer, charEncoding);
        } catch (IOException e) {
            System.out.println("获取字节数组失败");
            e.printStackTrace();
        }
        return res;
    }

    //初始化edit
    public static Edit getEdit(HttpServletRequest request) {
        Edit edit = null;
        String method = request.getMethod();
        String contentType = request.getContentType();
        if (method.equals("GET")) {
            //queryParamsList
            edit = new Edit_Get();
            edit.setReq_query(getQueryParamsList(request));//设置query
        } else {
            switch (contentType) {
                case "application/json":
                    edit = new Edit_Post_Json();
//                    edit.setReq_body_other(getRequestPostStr(request));
                    //TODO:获取json格式字符串
                    break;
                case "application/x-www-form-urlencoded":
                    //post_form
                    edit = new Edit_Post_Form();
                    //FormParams
                    edit.setReq_body_form(getFormParams(request));//设置表单参数
                    break;
                case "multipart/form-data":
                    //post_file  TODO:处理文件的情况 @Multipart注解情况适配
                    break;
                default:
                    //TODO:其他的请求头
                    System.out.println("ControllerWeavingUtils: 未记录的请求头: " + contentType);
                    break;
            }
        }
        return edit;
    }

    //获取表单参数
    private static List<FormParams> getFormParams(HttpServletRequest request) {
        List<FormParams> formParamslist = new ArrayList<>();
        Optional.ofNullable(request.getParameterMap()).ifPresent(parameterMap -> {
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                FormParams formParams = new FormParams();
                formParams.setName(entry.getKey());
                formParamslist.add(formParams);
            }
        });
        return formParamslist;
    }

    //获取queryParams
    private static List<QueryParams> getQueryParamsList(HttpServletRequest request) {
        List<QueryParams> queryParamsList = new ArrayList<>();
        Optional.ofNullable(request.getQueryString()).filter(a -> !a.equals("")).ifPresent(a -> {
            String[] split = a.split("&");
            Arrays.stream(split).forEach(str -> {
                String[] split1 = str.split("=");
                QueryParams queryParams = new QueryParams(split1[0], split1[1]);
                queryParamsList.add(queryParams);
            });
        });
        return queryParamsList;
    }

    //获取header
    public static Set<HeaderParams> getHeaderParamsSet(HttpServletRequest request) {
        Set<HeaderParams> headerParamsSet = new HashSet<>();
        Optional.ofNullable(request.getHeaderNames()).ifPresent(e -> {
            while (e.hasMoreElements()) {
                String headerName = (String) e.nextElement();
                HeaderParams headerParams = new HeaderParams(headerName, request.getHeader(headerName));
                headerParamsSet.add(headerParams);
            }
        });
        return headerParamsSet;
    }

    //获取title
    public static String getTitle(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String title = "(" + method + ")" + uri;
        return title.length() > 100 ? title.substring(0, 100) : title;
    }


    public static void doMock(HttpServletRequest request, Object response) {
        Edit edit = getEdit(request);//初始化Edit
        edit.setReq_headers(getHeaderParamsSet(request));//设置请求头
        edit.setPath(request.getRequestURL().toString());//设置路径
        System.out.println("------------>upload<------------");
        edit.setTitle(getTitle(request));//设置标题
        edit.setMethod(request.getMethod());//设置请求方法
        edit.setCatid(MyHttpUtils.getCatid());
        edit.setId(MyHttpUtils.getProject_id());
        edit.setRes_body(JSONObject.toJSONString(response));
//        System.out.println(JSONObject.toJSONString(edit));
        MyHttpUtils.uploadCase(edit);
//        System.out.println("------------>upload<------------");
//        System.out.println("MediaType " + mediaType);
//        System.out.println("MethodParameter" + methodParameter);
//        System.out.println("请求对象 " + request);
//        System.out.println("返回值 " + response);//返回值
    }
}
