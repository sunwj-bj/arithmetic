package com.interview;

import java.util.concurrent.locks.LockSupport;

/**
 * 用两个线程，一个输出数字，一个输出字母，交替输出
 */
public class AlternatePrint {
    char[] num = "0123456789".toCharArray();
    char[] chars = "abcdefghij".toCharArray();

    Thread t1=null;
    Thread t2=null;

    public void lockSupport(){

        t1=new Thread(()->{
           for (char c:chars){
               System.out.print(c);
               LockSupport.unpark(t2);
               LockSupport.park();
           }
        },"t1");

        t2=new Thread(()->{
           for (char c:num){
               LockSupport.park();
               System.out.print(c);
               LockSupport.unpark(t1);
           }
        },"t2");

        t1.start();
        t2.start();

    }

    public static void main(String[] args) {
        new AlternatePrint().lockSupport();
    }
}
