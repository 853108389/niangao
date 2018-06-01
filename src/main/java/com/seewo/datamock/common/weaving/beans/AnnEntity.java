package com.seewo.datamock.common.weaving.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author NianGao
 * @Date 2018/5/10.
 * @description 对应一个注解
 * 类比@RequestMapping(value = "testMethod2", method = {RequestMethod.GET, RequestMethod.DELETE})
 */
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnEntity {
    private String AnnDesc;//注解名的描述符 @RequestMapping
    private List<AnnEntry> entryList = new LinkedList<>(); //后面的键值对 (value = "testMethod2", method = {RequestMethod.GET, RequestMethod.DELETE})

    public AnnEntity(String annName) {
        AnnDesc = annName;
    }

}
