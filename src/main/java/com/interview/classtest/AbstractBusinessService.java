package com.interview.classtest;

public abstract class AbstractBusinessService {

    public void doBusiness(){
        buildContext();
        System.out.println("父类业务处理！");
    }

    public void buildContext(){
        System.out.println("父类构建上下文！");
    }


}
