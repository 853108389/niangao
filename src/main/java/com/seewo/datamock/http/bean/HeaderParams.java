package com.seewo.datamock.http.bean;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeaderParams that = (HeaderParams) o;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        return result;
    }
}
