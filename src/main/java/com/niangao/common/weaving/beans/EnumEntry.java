package com.niangao.common.weaving.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author NianGao
 * @Date 2018/5/10.
 * @description 注解里键值对的value
 */
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnumEntry {
    private String enumDesc = "";//枚举类的描述符 ,非枚举类型此处为""
    private Object enumValue = "";//枚举类型的值
}
