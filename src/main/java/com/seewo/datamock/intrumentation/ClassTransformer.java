package com.seewo.datamock.intrumentation;

import com.seewo.datamock.common.Config;
import com.seewo.datamock.common.aspect.BaseAspect;
import com.seewo.datamock.common.utils.ClassScanner;

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
public class ClassTransformer implements ClassFileTransformer {
    private BaseAspect baseAspect;
    private boolean flag = true;

    public ClassTransformer(BaseAspect baseAspect) {
        this.baseAspect = baseAspect;
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
        try {
            if (Config.isScan(className)) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(classfileBuffer);
                byte[] bytes = ClassScanner.classScanner(byteArrayInputStream, baseAspect, Config.agentGenClassPos, className);
                System.out.println();
                return bytes;
            }
        } catch (IOException e) {
            System.out.println("IO异常...  " + e.getMessage());
        } catch (Exception e) {
            if (e instanceof ClassCastException || e instanceof ClassNotFoundException) {
                System.out.println("转换异常,不用理它....  ");
//                e.printStackTrace();
            } else {
                e.printStackTrace();
            }
        }
        return classfileBuffer;
    }

}
