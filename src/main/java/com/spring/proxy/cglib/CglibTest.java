package com.spring.proxy.cglib;

public class CglibTest {
    public static void main(String[] args) {
        Engineer engineer = (Engineer)EngineerProxy.getProxy(new Engineer());
        engineer.eat();
    }
}
