package com.design.pattern.adapter;

public class Mp4Player implements MediaPlayer {
    @Override
    public void play() {
        System.out.println("加载MP4视频解码器！");
    }
}
