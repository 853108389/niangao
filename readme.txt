一丶目录结构
    |-java
        |-common(字节码植入的类)
            |-aspect(切面,为asm代码,减少耦合,以后切面变多了,可以继续抽象)
                |-BaseAspect.java 切面的抽象类
                |-MyAspectFactory.java 切面类工厂
                |-ControllerAspect.java 核心切面类,用于Controller
                |-InterceptorAspect.java 核心切面类，用于Interceptor
            |-bean(实体)
                |-Info.java 扫描时,封装扫描类的信息
                |-LocalParams.java 封装方法参数信息
            |-myAdapter(适配器,适配ASM,用于扫描类信息)
                |-ControllerAdapter.java 类适配置器,用于对@Controller注解的类进行扫描
                |-ControllerClassAnoAdapter.java 类注解适配器
                |-ControllerMethodAdapter.java  方法适配器
                |-ControllerMethodAnoAdapter.java 方法注解适配器
                |-ControllerParamsAnoAdapter.java 参数注解适配器
                |-InterceptorAdapter.java
                |-InterceptorMethodAdapter.java
            |-utils(工具)
                |-ClassTools.java 用于扫描指定包里的类(开发时使用,代理情况下,扫描每个类无需我们关心)
                |-ControllerScanner 对类进行扫描并进行字节码增强的入口Api
            |-weaving(植入)
                |-beans(实体)
                    |-AnnEntity.java  如@RequestMapping(value = "testMethod2", method = {RequestMethod.GET, RequestMethod.DELETE})
                    |-AnnEntry.java   如 value = "testMethod2" 或 method = {RequestMethod.GET, RequestMethod.DELETE}
                    |-EnumEntry.java  如 RequestMethod.GET和 RequestMethod.DELETE    用于封装注解信息.
                |-interceptor(拦截器)
                    |-UploadInterceptor.java
                |-utils(工具类)
                    |-ControllerWeavingUtils.java 被植入的方法,以及从从被构造上传参数到上传之间的全过程
            |-Config.java 读取配置文件,并加载其中参数到配置中.
        -http(用于向平台发送http请求)
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
        |-test
            |-MyTrace.java 用于打印asm的字节码指令
        |-Main.java main方法,用于开发时启动
    |-resources
        |-config
            |-config.properties 配置文件
        |-UploadInterceptor.class 要植入的拦截器的class文件
二丶使用方式
    1.编辑/config/config.properties文件,修改参数,并生成jar.修改的参数有:
        IPackageBaseName:需要扫描的基础包名,即该目录下的class文件会被扫描(不写代表扫描所有)
        IPackageName:在基础包名上,需要扫描的包,可以更细致的指定的范围(可不写)
        username:用户名
        password:密码
        groupName:项目组名称
        projectName:项目名称
        catName:接口分类名称
        colName:测试分类名称
        headers:每次请求附带的默认请求头(目的是跳过拦截器,可不写)
    2.将jar包中的lib目录复制到与jar包同级的目录下,即
        |-lib
        |-jar
    4.命令行代理启动
      格式如 java -javaagent:D:\workplace\test\out\artifacts\seewo_datamock\seewo-datamock.jar=Hello -jar D:\seewo-roomcenter.jar
      java -javaagent:${此jar包的全路径}=${自定义参数} -jar ${被代理启动jar的全路径}
三丶注:
    1.一定注意：
        开发时引用的jar
        打包后引用的jar
        和被代理的目标jar
        classpath不一样
    2.所以,自定义的,与spring产生依赖的类,都要小心处理,不能在本jar中处理任何与spring相关的类
    3.不能直接将拦截器通过asm植入到代理jar内.正确的方法是将自定义拦截器编译后,将.class文件拷贝到resources目录下
    4.不要随便改类名
    5.配置文件为空时可以写';' 不要擅自删除某一项
    6.一些细节在代码中以注释标明
四丶一些未解决的问题或者坑或者莫名其妙的问题
    1.interceptor
       1.1 interceptor的request的流只能获取一次的问题
       1.2 interceptor拿不到@requestBody的返回值
    2.controller
       2.1 controller方式增强,如果存在着被拦截器拦截的请求,是不会被录入的
    3.controllerAdvice并没有考虑到以下问题:
       5.1.多个ControllerAdvice
       5.2.存在着已经实现过ResponseBodyAdvice的接口,这个时候需要写methodAdapter,把新增方法改为修改方法
       5.3 ResponseBodyAdvice的接口的request的流是已经关闭的,暂时没发现可以获取到请求体的方法