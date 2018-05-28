package com.niangao.http.vo;

import com.niangao.http.bean.FormParams;
import com.niangao.http.bean.HeaderParams;
import com.niangao.http.bean.QueryParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * @Author NianGao
 * @Date 2018/5/21.
 * @description 编辑测试用例
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditTestCase {
    private String id; //当前例子的id-
    private String casename;    //当前例子的名称-
    private String case_env = "local";//未知
    private List<?> req_params;    //请求参数-
    private List<QueryParams> req_query;     //查询参数-
    private Set<HeaderParams> req_headers;   //请求头-
    private String req_body_type;   //请求体类型  form /json-
    private String req_body_other;//-
    private List<FormParams> req_body_form; //返回-
    private String test_script = ""; //未知
    private boolean enable_script = false;  //未知

    public EditTestCase(Edit edit, String id) {
        this.id = id;
        this.casename = edit.getTitle();
        this.req_params = edit.getReq_params();
        this.req_query = edit.getReq_query();
        this.req_headers = edit.getReq_headers();
        this.req_query = edit.getReq_query();
        this.req_body_type = edit.getReq_body_type();
        this.req_body_other = edit.getReq_body_other();
        this.req_body_form = edit.getReq_body_form();
    }
}
