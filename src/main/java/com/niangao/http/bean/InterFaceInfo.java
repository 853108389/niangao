package com.niangao.http.bean;

import lombok.Data;

/**
 * @Author NianGao
 * @Date 2018/5/21.
 * @description 接口信息对应的bean 没用上
 */
@Data
public class InterFaceInfo {
    private String _id;
    private String add_time;
    private boolean api_opened;
    private String catid;
    private String edit_uid;
    private String index;
    private String method;
    private String path;
    private String project_id;
    private String status;
    private String title;
    private String uid;
}
