package com.example.musi;

import android.media.MediaPlayer;

public class MyMediaPlayer {
    static MediaPlayer instance;
    public static int currentIndex = 0;

    public static MediaPlayer getInstance(){
        if(instance == null){
            instance = new MediaPlayer();
        }
        return instance;
    }


}