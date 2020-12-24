package com.design.pattern.adapter;

public class MediaAdapter implements MediaPlayer {

    MediaPlayer mediaPlayer;

    public MediaAdapter(MediaPlayer mediaPlayer){
       this.mediaPlayer=mediaPlayer;
    }

    @Override
    public void play() {
        mediaPlayer.play();
    }
}
