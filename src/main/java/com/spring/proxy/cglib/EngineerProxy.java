package com.spring.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class EngineerProxy implements MethodInterceptor {
    private Object target;

    public EngineerProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (method.getName().equals("eat")){
            System.out.println("吃饭前准备！");
            //返回值为null，不用接收结果
            method.invoke(target,objects);
        }
        return null;
    }

    public static Object getProxy(Object target) {
        Enhancer enhancer = new Enhancer();
        // 设置需要代理的对象
        enhancer.setSuperclass(target.getClass());
        // 设置代理人
        enhancer.setCallback(new EngineerProxy(target));
        return enhancer.create();
    }
}
