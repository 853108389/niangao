目录结构
|-java
    |-common(字节码植入的类)
        |-aspect(切面,将对方法植入的代码进行抽象,减少耦合,以后切面变多了,可以继续抽象)
            |-MyAspect.java 切面的抽象类
            |-MyAspectFactory.java 切面类工厂
            |-ControllerAspect.java 核心切面类,用于Controller
        |-bean(实体)
            |-Info.java 扫描时,封装扫描类的信息
            |-LocalParams.java 封装方法参数信息
        |-myAdapter(适配器,适配ASM,用于扫描类信息)
            |-ControllerAdapter.java 类适配置器,用于对@Controller注解的类进行扫描
            |-ControllerClassAnoAdapter.java 类注解适配器
            |-ControllerMethodAdapter.java  方法适配器
            |-ControllerMethodAnoAdapter.java 方法注解适配器
            |-ControllerParamsAnoAdapter.java 参数注解适配器
            |-InterceptorAdapter.java TODO:拦截器适配器
            |-InterceptorMethodAdapter.java TODO:拦截器方法适配器
        |-utils(工具)
            |-ClassTools.java 用于扫描指定包里的类(开发时使用,代理情况下,扫描每个类无需我们关心)
            |-ControllerScanner 对类进行扫描并进行字节码增强的入口Api
        |-weaving(植入)
            |-beans(实体)
                |-AnnEntity.java  如@RequestMapping(value = "testMethod2", method = {RequestMethod.GET, RequestMethod.DELETE})
                |-AnnEntry.java   如 value = "testMethod2" 或 method = {RequestMethod.GET, RequestMethod.DELETE}
                |-EnumEntry.java  如 RequestMethod.GET和 RequestMethod.DELETE    用于封装注解信息.
            |-interceptor(拦截器)
                |-UploadInterceptor.java TODO:植入的拦截器类
            |-utils(工具类)
                -WeavingUtils.java 被植入的方法,以及从从被构造上传参数到上传之间的全过程
        |-Config.java 读取配置文件,并加载其中参数到配置中.
    -http(用于向平台发送http青穹)
        |-bean
            |-Col.java 对应分类目录信息
            |-FormParams.java 对应表单参数
            |-HeaderParams.java 对应请求头参数
            |-InterFaceInfo.java 对应接口信息
            |-QueryParams.java 对应查询参数
            |-TestCase.java 对应测试用例信息
        |-utils
            |-MyHttpUtils.java 封装http请求
        |-vo
            |-AddInterface.java 添加接口
            |-AddTestCase.java 添加测试用例
            |-Edit.java 编辑
            |-Edit_Get.java Get方式
            |-Edit_Post_Form.java Edit_Post_Form方式
            |-Edit_Post_Json.java Edit_Post_Json方式
            |-EditTestCase.java 编辑测试用例
            |-Login.java 登入
        |-Api.java  对应的所有url
    |-intrumentation(代理启动)
        |-ControllerTransformer.java 代理启动指定的转化类
        |-MyAgent.java  代理启动入口
    |-Main.java main方法,用于开发时启动
|-resources
    |-config
        |-config.properties 配置文件
使用方式
0.修改MyHttpUtils.group_id
1.编辑/config/config.properties文件,修改参数,并生成jar
2.将jar包中的lib目录复制
3.复制到与jar包同级的目录下即
    |-lib
    |-jar
4.命令行代理启动
  格式如 java -javaagent:D:\workplace\test\out\artifacts\seewo_datamock\seewo-datamock.jar=Hello -jar D:\seewo-roomcenter.jar
  java -javaagent:${此jar包的全路径}=${自定义参数} -jar ${被代理启动jar的全路径}
