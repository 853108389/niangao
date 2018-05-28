package com.niangao.http.vo;

import com.niangao.http.bean.HeaderParams;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Iterator;
import java.util.Set;

/**
 * @Author NianGao
 * @Date 2018/5/19.
 * @description
 */
@ToString
@Data
@EqualsAndHashCode(callSuper=true)
public class Edit_Post_Form extends Edit {
    private String method = "POST";
    private String req_body_type = "form";//表单格式

    public Edit_Post_Form() {
        this.getReq_headers().add(new HeaderParams("Content-Type", "application/x-www-form-urlencoded"));
    }

    @Override
    @SuppressWarnings("all")
    public void setReq_headers(Set<HeaderParams> req_headers) {
        Iterator<HeaderParams> iterator = req_headers.iterator();
        while (iterator.hasNext()) {
            HeaderParams next = iterator.next();
            if (this.getReq_headers().contains(next)) {
                this.getReq_headers().remove(next);
            }
            this.getReq_headers().add(next);
        }
    }
}