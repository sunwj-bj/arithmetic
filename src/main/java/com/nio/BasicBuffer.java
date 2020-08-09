package com.nio;

import java.nio.IntBuffer;

/**
 * NIO Buffer的使用
 */
public class BasicBuffer {
    public static void main(String[] args) {

        //1、创建一个buffer，可以存储五个整数
        IntBuffer intBuffer =IntBuffer.allocate(5);
        //2、向buffer中存放数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i*2);
        }
        //3、从buffer读取数据,flip函数用来进行读写转换
        intBuffer.flip();
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
