package com.seewo.datamock.http.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @Author NianGao
 * @Date 2018/5/16.
 * @description 添加一个接口
 */
@Data
@ToString
public class AddInterface {
    private String project_id;//项目id
    private String catid;//父分类id
    private String title;//接口名称
    private String path;//接口路径
    private String method;//接口方法

    public AddInterface() {

    }

    public AddInterface(String catid, String project_id, String title, String path, String method) {
        this.catid = catid;
        this.title = title;
        this.path = path;
        this.project_id = project_id;
        this.method = method;
    }
}
