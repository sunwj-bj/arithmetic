package com.spring.proxy.jdkproxy;

import com.spring.proxy.jdkproxy.face.PeopleInterface;
import com.spring.proxy.jdkproxy.handler.PeopleInvocationHandler;
import com.spring.proxy.jdkproxy.impl.PeopleImpl;

import java.lang.reflect.Proxy;

public class PeopleInvocationHandlerTest {
    public static void main(String[] args) {
        PeopleInterface people = (PeopleInterface)Proxy.newProxyInstance(PeopleInvocationHandlerTest.class.getClassLoader(),
                new Class[]{PeopleInterface.class},
                new PeopleInvocationHandler(new PeopleImpl()));
        people.makeFood();
    }
}
