package com.example.musi;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MusicService extends Service {

    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    int currentPosition = 0;
    Boolean isPlaying =  true;

    PendingIntent pausePendingIntent;
    private static final String CHANNEL_ID = "MusicChannel";
    private ArrayList<AudioModel> songsList;
    private AudioModel currentSong;

    MyReceiver myReceiver;
    NotificationReceiver notifyresv;
    private PendingIntent previousPendingIntent;
    private PendingIntent nextPendingIntent;
    private PendingIntent playPausePendingIntent;
    private PendingIntent pendingIntent;
    public static final String ACTION_UPDATE_SONG = "com.example.musi.ACTION_UPDATE_TITLE";
    public static final String ACTION_PLAY_PAUSE = "com.example.musi.ACTION_PLAY_PAUSE";



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        registerReceivers();
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        pausePendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("Pause"), PendingIntent.FLAG_IMMUTABLE);

        nextPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("Next"), PendingIntent.FLAG_IMMUTABLE);
        previousPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("Previous"), PendingIntent.FLAG_IMMUTABLE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        if (songsList == null) {
            songsList = loadAudioFiles();
        }


        if (currentSong == null) {
            setResourcesWithMusic();
        }
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) mediaPlayer.stop();
        mediaPlayer.release();
        stopSelf();
        unregisterReceiver(myReceiver); // Unregister receiver
        unregisterReceiver(notifyresv);
    }


    private ArrayList<AudioModel> loadAudioFiles() {
        ArrayList<AudioModel> songsList = new ArrayList<>();
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
        while (cursor.moveToNext()) {
            AudioModel songData = new AudioModel(cursor.getString(1), cursor.getString(0),  cursor.getString(2));
            if (new File(songData.getPath()).exists())
                songsList.add(songData);
        }
        return songsList;
    }

    void setResourcesWithMusic() {
        currentSong = songsList.get(MyMediaPlayer.currentIndex);
        updateSong(currentSong.getTitle());
        if (currentSong.getFav())
            sendBroadcast(new Intent("favor"));
        else
            sendBroadcast(new Intent("disfavor"));

        sendBroadcast(new Intent("currentSong").putExtra("currentSong", currentSong).putExtra("isPlaying" , mediaPlayer.isPlaying()));
        playMusic(currentSong);


    }


    private void playMusic(AudioModel curSong) {

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(curSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            updateNotification();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void play() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(currentPosition);
            mediaPlayer.start();
            updateNotification();
        }

    }

    private void pause() {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            currentPosition = mediaPlayer.getCurrentPosition();
            updateNotification();
        }
    }

    private void playNextSong() {

        if (MyMediaPlayer.currentIndex == songsList.size() - 1)
            return;
        MyMediaPlayer.currentIndex += 1;
        mediaPlayer.reset();
        setResourcesWithMusic();
        updateNotification();

    }

    private void playPreviousSong() {
        if (MyMediaPlayer.currentIndex == 0)
            return;
        MyMediaPlayer.currentIndex -= 1;
        mediaPlayer.reset();
        setResourcesWithMusic();

        updateNotification();
    }


    @SuppressLint("DefaultLocale")
    public static String convertToMMSS(String duration) {
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    private void updateSong(String title) {
        Intent intent = new Intent(ACTION_UPDATE_SONG);
        intent.putExtra("title", title);
        sendBroadcast(intent);
    }

    private void registerReceivers() {
        if (myReceiver == null) {
            myReceiver = new MyReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(MainActivity.ACTION_NEXT);
            intentFilter.addAction(MainActivity.ACTION_PREVIOUS);
            intentFilter.addAction(MainActivity.ACTION_PAUSE);
            intentFilter.addAction(MainActivity.ACTION_PLAY);
            intentFilter.addAction(MainActivity.FAVORITE);
            intentFilter.addAction(MainActivity.DISFAVORITE);
            intentFilter.addAction("clickItem");
            intentFilter.addAction(MainActivity.ACTION_PLAY_ITEM_FAV);
            registerReceiver(myReceiver, intentFilter);

        }

        if (notifyresv == null) {
            notifyresv = new NotificationReceiver();
            registerReceiver(notifyresv, new IntentFilter("Pause"));
            registerReceiver(notifyresv, new IntentFilter("Next"));
            registerReceiver(notifyresv, new IntentFilter("Previous"));
        }
    }



    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case MainActivity.ACTION_PAUSE : pause();break;
                case MainActivity.ACTION_PREVIOUS : playPreviousSong();break;
                case MainActivity.ACTION_NEXT :  playNextSong();break;
                case MainActivity.ACTION_PLAY : play();break;
                case MainActivity.ACTION_PLAY_ITEM_FAV :
                    currentSong = (AudioModel) intent.getSerializableExtra("currentSong");
                    playMusic(currentSong);


            }

        }
    }








    //__________________________________NOTIFICATION_________________________________________//


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Playback",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Music playback controls");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }


    private void updateNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(currentSong.getTitle())
                .setSmallIcon(R.drawable.icon_music_player)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_pause, "Next", nextPendingIntent)
                .addAction(mediaPlayer.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play,
                        mediaPlayer.isPlaying() ? "Pause" : "Play",
                         pausePendingIntent )
                .addAction(R.drawable.ic_pause, "Previous", previousPendingIntent);

        startForeground(1, builder.build());
    }


    public class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "Pause":
                    if(mediaPlayer.isPlaying())  pause();
                    else play();
                    break;
                case "Next":
                    playNextSong();
                    break;
                case "Previous":
                    playPreviousSong();
                    break;


            }
        }
    }

}