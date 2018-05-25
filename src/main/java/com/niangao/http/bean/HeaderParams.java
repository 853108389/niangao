package com.niangao.http.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author NianGao
 * @Date 2018/5/17.
 * @description header参数对应的bean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeaderParams {
    private String example = "";
    private String name;
    private String value;
    private String desc = "";

    public HeaderParams(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
