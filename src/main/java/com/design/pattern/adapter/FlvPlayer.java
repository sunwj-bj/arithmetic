package com.design.pattern.adapter;

public class FlvPlayer implements MediaPlayer {
    @Override
    public void play() {
        System.out.println("加载flv解码器！");
    }
}
