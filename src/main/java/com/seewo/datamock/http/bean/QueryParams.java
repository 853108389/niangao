package com.seewo.datamock.http.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author NianGao
 * @Date 2018/5/17.
 * @description query参数对应的bean
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryParams {
    private String example = "";
    private String name;
    private String desc = "";
    private String required = "1"; //0代表不是必须  1代表是必须的
    private String value;

    public QueryParams(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
