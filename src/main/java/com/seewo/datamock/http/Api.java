package com.seewo.datamock.http;

/**
 * @Author NianGao
 * @Date 2018/5/16.
 * @description 对应的测试平台的接口
 */
public class Api {
    //登录
    public static final String login = "http://sapi.gz.cvte.cn/api/user/login";
    //添加接口
    public static final String addinterface = "http://sapi.gz.cvte.cn/api/interface/add";
    //编辑接口信息
    public static final String editInterface = "http://sapi.gz.cvte.cn/api/interface/up";
    //添加测试用例
    public static final String addTestCase = "http://sapi.gz.cvte.cn/api/col/add_case_list";
    //编辑测试用例
    public static final String editTestCase = "http://sapi.gz.cvte.cn/api/col/up_case";
    //查看某目录下的所有的测试用例 ?col_id=
    public static final String findAllTestCase = "http://sapi.gz.cvte.cn/api/col/case_list";
    //查看某目录下的所有的接口信息 ?page=1&limit=20&catid=79
    public static final String findAllInterFace = "http://sapi.gz.cvte.cn/api/interface/list_cat";
    //查看某工程下所有的目录?project_id=69
    public static final String findAllCat = "http://sapi.gz.cvte.cn/api/interface/list_menu";
    //查看分组内所有的项目信息?group_id=20
    public static final String findAllProject = "http://sapi.gz.cvte.cn/api/project/list";
    //查看分组内所有的测试用例目录?project_id=69
    public static final String findAllCase = "http://sapi.gz.cvte.cn/api/col/list";
    //查看所有的分组
    public static final String findAllGroups = "http://sapi.gz.cvte.cn/api/group/list";
}
