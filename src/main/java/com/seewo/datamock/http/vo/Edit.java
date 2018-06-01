package com.seewo.datamock.http.vo;

import com.seewo.datamock.common.Config;
import com.seewo.datamock.http.bean.FormParams;
import com.seewo.datamock.http.bean.HeaderParams;
import com.seewo.datamock.http.bean.QueryParams;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author NianGao
 * @Date 2018/5/21.
 * @description
 */
@ToString
@Data
public class Edit {
    //默认值

    private boolean api_opened = true;//开放接口
    private String desc = "<p>自动化添加</p>";//描述 备注的富文本形式
    private String markdown = "自动化添加";//备注
    private String status = "done";//状态 未完成已完成
    private boolean req_body_is_json_schema = false;//json格式
    private boolean res_body_is_json_schema = false;//json或者json_schema
    private boolean switch_notice = true;//邮件通知
    //与当前接口的信息相同
    private String title;//标题   --->方法名
    private String catid;//父目录id     --->注解上拿
    private String id; //当前接口的id  (add&edit时传递projectId即可)
    //关键配置
    private String method;  //
    private String path = "";//路径
    private List<FormParams> req_body_form = new ArrayList<>();//
    private List<QueryParams> req_query;//请求参数
    private Set<HeaderParams> req_headers = new HashSet<HeaderParams>() {
        {
            Config.defaultHeaders.forEach((key, val) -> {
                add(new HeaderParams(key, val));
            });
        }
    };//请求头
    private List<?> req_params;//请求参数
    private String res_body_type = "json";//返回是json格式
    private String res_body = "";    //响应体
    private String Req_body_other;
    private String req_body_type;

}
