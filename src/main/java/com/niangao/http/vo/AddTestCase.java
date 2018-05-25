package com.niangao.http.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author NianGao
 * @Date 2018/5/21.
 * @description 添加一个测试用例
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTestCase {
    private String col_id ;   //分类id
    private List<String> interface_list;  //接口的id  可以批量添加,但不需要
    private String project_id;  //项目id
}
