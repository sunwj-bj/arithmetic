package com.multithread;

import org.springframework.util.StopWatch;

/**
 * 证明CPU指令重排序
 * @author lenovo
 */
public class CPU_Disorder {

    public static int a,b,x,y;

    public static void main(String[] args) throws Exception {
        StopWatch s = new StopWatch();
        s.start();
        int i = 0;
        while(true) {
            i++;
            a=0;b=0;x=0;y=0;
            Thread t1 = new Thread(() -> {
                a = 1;
                x = b;
            });
            Thread t2 = new Thread(()->{
                b = 1;
                y = a;
            });

            t1.start();
            t2.start();
            t1.join();
            t2.join();

            if( x == 0 && y == 0) {
                s.stop();
                System.out.println("第" + i + "次：x="+x + ",y=" + y );
                System.out.println(s.prettyPrint());
                break;
            }
        }
    }
}
