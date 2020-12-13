package com.spring.proxy.jdkproxy.handler;

import com.spring.proxy.jdkproxy.face.PeopleInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * jdk动态代理需要有接口才能实现
 * cglib代理不需要接口就能代理，它是针对代理的类, 动态生成一个子类, 然后子类覆盖代理类中的方法, 如果是private或是final类修饰的方法,则不会被重写
 */
public class PeopleInvocationHandler implements InvocationHandler {

    private PeopleInterface peopleInterface;

    public PeopleInvocationHandler(PeopleInterface peopleInterface) {
        this.peopleInterface = peopleInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("makeFood")){
            System.out.println("做饭前买菜！");
            method.invoke(peopleInterface,args);
        }
        return null;
    }
}
