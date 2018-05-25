package com.niangao.common.weaving.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author NianGao
 * @Date 2018/5/10.
 * @description 对应注解里的一对key-value
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AnnEntry {
    private String key = "";//该键值对的key
    private boolean isEnumType = false;//是否为枚举类型
    private List<EnumEntry> enumEntrylist = new LinkedList<>();//用于存储枚举或者非枚举类型

    public AnnEntry(String key) {
        this.key = key;
    }

}
