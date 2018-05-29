package com.seewo.datamock.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author NianGao
 * @Date 2018/5/8.
 * @description
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LocalParams {
    private String name;//参数名称
    private String desc; //方法描述符
}
