package com.seewo.datamock.intrumentation;

import com.seewo.datamock.common.Config;
import com.seewo.datamock.common.aspect.MyAspect;
import com.seewo.datamock.common.utils.ControllerScanner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @Author NianGao
 * @Date 2018/5/8.
 * @Des 代理启动, 用于加载类
 */
public class ControllerTransformer implements ClassFileTransformer {
    private MyAspect myAspect;
    private boolean flag = true;

    public ControllerTransformer(MyAspect myAspect) {
        this.myAspect = myAspect;
    }

    /**
     * @param loader
     * @param className
     * @param classBeingRedefined
     * @param protectionDomain
     * @param classfileBuffer
     * @return
     * @throws IllegalClassFormatException
     */
    @Override
    @SuppressWarnings("all")
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//            TODO: 对interceptor进行增强
        try {
            if (Config.isScan(className)) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(classfileBuffer);
                byte[] bytes = ControllerScanner.classScanner(byteArrayInputStream, myAspect, Config.agentGenClassPos, className);
                System.out.println();
                return bytes;
            }
        } catch (IOException e) {
            System.out.println("IO异常...  " + e.getMessage());
        } catch (Exception e) {
            System.out.println("转换异常,不用理它....  ");
            e.printStackTrace();
        }
        return classfileBuffer;
    }

}
