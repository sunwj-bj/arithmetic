package com.interview.classtest;

public class OrderBusinessService extends AbstractBusinessService {
    public void doBusiness(){
        super.doBusiness();
        System.out.println("子类业务处理！");
    }

    public void buildContext(){
        super.buildContext();
        System.out.println("子类构建上下文！");
    }

    public static void main(String[] args) {
        OrderBusinessService orderBusinessService = new OrderBusinessService();
        orderBusinessService.doBusiness();
    }
}
