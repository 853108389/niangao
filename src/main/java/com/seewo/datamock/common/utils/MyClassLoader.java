package com.seewo.datamock.common.utils;

import com.seewo.datamock.common.Config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author NianGao
 * @Date 2018/5/29.
 * @description
 */
public class MyClassLoader extends ClassLoader {

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (!name.equals(Config.weavingInterceptorClassName)) {
            //由于类加载机制,非我们自己的类一定要用当前线程的类加载器去加载!
            System.out.println("getContextClassLoader(): " + Thread.currentThread().getContextClassLoader().getClass().getName());
            return Thread.currentThread().getContextClassLoader().loadClass(name);
        } else {
            return findClass(name);
        }
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] res = new byte[1024];
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             //使用流,而不是文件系统,因为已经打成jar了!
             InputStream in = MyClassLoader.class.getClassLoader().getResourceAsStream(name + ".class");
        ) {
            System.out.println(name + ".class" + "____" + in);
            byte[] buff = new byte[1024]; //buff用于存放循环读取的临时数据
            int rc = 0;
            while ((rc = in.read(buff, 0, 1024)) > 0) {
                bos.write(buff, 0, rc);
            }
            res = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defineClass(res, 0, res.length);
    }
}
