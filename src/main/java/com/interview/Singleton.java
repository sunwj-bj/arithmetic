package com.interview;

/**
 * @author sunwj
 */
public class Singleton {
    //加volatile是为了防止指令重排序
    private volatile static Singleton singleton;

    private Singleton() {
    }

    public static Singleton newInstance() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
