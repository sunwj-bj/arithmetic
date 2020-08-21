package com.nio;

import java.nio.ByteBuffer;

/**
 * -XX:MaxDirectMemorySize=5m
 * -XX:+PrintGCDetails
 */
public class OOM_DirectBufferMemory {
    public static void main(String[] args) {
        System.out.println("配置的最大内存空间为："+(sun.misc.VM.maxDirectMemory()/(double)1024/1024)+"MB");
        ByteBuffer.allocateDirect(6*1024*1024);

    }
}
