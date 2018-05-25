package com.niangao.http.vo;

import com.niangao.http.bean.HeaderParams;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author NianGao
 * @Date 2018/5/19.
 * @description
 */
@Data
@ToString
public class Edit_Post_Form extends Edit {
    private String method = "POST";
    private String req_body_type = "form";//表单格式
    private List<HeaderParams> req_headers = new ArrayList<HeaderParams>() {
        {
            add(new HeaderParams("Content-Type", "application/x-www-form-urlencoded"));
        }
    };
}