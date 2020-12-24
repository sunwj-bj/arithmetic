package com.design.pattern.singleton;

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
                    //这里可能发生指令重排序，正常初始化一个对象分为三个步骤：
                    //1.初始化内存空间 2.将对象实例化 3.引用指向该内存空间
                    //如果发生指令重排序，第二步和第三步调换执行顺序，别的线程来获取实例，
                    // 判断引用不为空就会直接拿去用，但这时候对象可能并未初始化完成，可能会发生空指针。
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
