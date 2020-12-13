package com.spring.proxy.jdkproxy.impl;

import com.spring.proxy.jdkproxy.face.PeopleInterface;

public class PeopleImpl implements PeopleInterface {
    @Override
    public void makeFood() {

        System.out.println("做饭");
    }

    @Override
    public void eatFood() {
        System.out.println("吃饭");

    }

    @Override
    public void wash() {
        System.out.println("洗碗");

    }
}
