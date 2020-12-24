package com.design.pattern.adapter;

/**
 * @author sunwj
 * 适配器模式，适配器能够整合不同的编码播放器，对外统一提供播放接口
 */
public class AdapterPattern {
    public static void main(String[] args) {
        FlvPlayer flvPlayer = new FlvPlayer();
        Mp4Player mp4Player = new Mp4Player();
        //播放Flv视频，仅需要传入flv解码器即可
        MediaAdapter mediaAdapter = new MediaAdapter(flvPlayer);
        mediaAdapter.play();
    }
}
