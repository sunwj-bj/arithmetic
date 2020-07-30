package com.multithread;

public class JoinTest implements Runnable {

    public static int a = 0;
    public static int count = 10;

    @Override
    public void run() {
        for (int k = 0; k < count; k++) {
            a = a + 1;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable r = new JoinTest();
        Thread t = new Thread(r);
        t.start();
        //这里如果没有join，后面的输出一般都不会是10，join的目的是让当前线程t执行完毕再执行main线程
        t.join();
        System.out.println(a);
    }
}
