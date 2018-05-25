package com.niangao.http.bean;

import lombok.Data;

/**
 * @Author NianGao
 * @Date 2018/5/19.
 * @description 表单参数对应的bean
 */
@Data
public class FormParams {
    private String desc = "";
    private String example = "";
    private String name;
    private String required = "1";
    private String type = "text";//file
    private String value;
}
