package com.niangao.http.bean;

import com.niangao.http.vo.Edit;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author NianGao
 * @Date 2018/5/21.
 * @description 测试用例对应的bean  没用上
 */
@Data
@NoArgsConstructor
public class TestCase {
    private String __v;//未知
    private String _id;//case的id
    private String add_time;
    private String casename;
    private String col_id;
    private boolean enable_script;
    private String index;
    private String interface_id;
    private String method;
    private boolean mock_verify;
    private String path;
    private String project_id;
    private List<FormParams> req_body_form;
    private List<HeaderParams> req_headers;
    private List<?> req_params;//不知道
    private List<QueryParams> req_query;
    private String res_body_type;
    private String title;
    private String uid;
    private String up_time;
    private String req_body_type;
    private String req_body_other;

    public TestCase(Edit edit) {
        this.casename = edit.getTitle();
        this.req_body_form = edit.getReq_body_form();
        this.req_headers = edit.getReq_headers();
        this.req_params = edit.getReq_params();
        this.req_query = edit.getReq_query();
        this.req_body_type = edit.getReq_body_type();
        this.req_body_other = edit.getReq_body_other();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TestCase testCase = (TestCase) o;
        if (!casename.equals(testCase.casename)) return false;
        if (req_body_form != null ? !req_body_form.toString().equals(testCase.req_body_form.toString()) : testCase.req_body_form != null)
            return false;
        if (req_headers != null ? !req_headers.toString().equals(testCase.req_headers.toString()) : testCase.req_headers != null)
            return false;
        if (req_params != null ? !req_params.toString().equals(testCase.req_params.toString()) : testCase.req_params != null) return false;
        if (req_query != null ? !req_query.toString().equals(testCase.req_query.toString()) : testCase.req_query != null) return false;
        if (req_body_type != null ? !req_body_type.equals(testCase.req_body_type) : testCase.req_body_type != null)
            return false;
        return req_body_other != null ? req_body_other.equals(testCase.req_body_other) : testCase.req_body_other == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + casename.hashCode();
        result = 31 * result + (req_body_form != null ? req_body_form.hashCode() : 0);
        result = 31 * result + (req_headers != null ? req_headers.hashCode() : 0);
        result = 31 * result + (req_params != null ? req_params.hashCode() : 0);
        result = 31 * result + (req_query != null ? req_query.hashCode() : 0);
        result = 31 * result + (req_body_type != null ? req_body_type.hashCode() : 0);
        result = 31 * result + (req_body_other != null ? req_body_other.hashCode() : 0);
        return result;
    }
}
