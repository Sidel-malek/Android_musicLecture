package com.example.musi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView titleTv, currentTimeTv, totalTimeTv;
    ImageView  nextBtn, previousBtn , fav ,playPause , startService, liste;

    MaBaseManager maBaseManager;

    private AudioModel currentSong;

    public static final String ACTION_NEXT = "com.example.musi.ACTION_NEXT";
    public static final String ACTION_PREVIOUS = "com.example.musi.ACTION_PREVIOUS";
    public static final String ACTION_PAUSE = "com.example.musi.ACTION_PAUSE";
    public static final String ACTION_PLAY = "com.example.musi.ACTION_PLAY";

    public static final String FAVORITE = "com.example.musi.FAVORITE";
    public static final String DISFAVORITE = "com.example.musi.DISFAVORITE";
    public static final String ACTION_PLAY_ITEM_FAV = "com.example.musi.ACTION_PLAY_ITEM_FAV";

    public Boolean isPlaying = true;



    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent.getAction().equals("clickItem")){
            currentSong = (AudioModel) intent.getSerializableExtra("song");
            playItemFav(currentSong);

        }
        titleTv = findViewById(R.id.title);

        playPause = findViewById(R.id.playPause);
        startService= findViewById(R.id.startService);


        nextBtn = findViewById(R.id.next);
        previousBtn = findViewById(R.id.previous);
        fav = findViewById(R.id.fav);
        titleTv.setSelected(true);

        liste = findViewById(R.id.liste_fav);

        maBaseManager = new MaBaseManager(this);

        if (checkPermission() == false) {
            requestPermission();
            return;
        }
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService.setVisibility(View.INVISIBLE);
                previousBtn.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
                fav.setVisibility(View.VISIBLE);
                playPause.setVisibility(View.VISIBLE);

                startService(new Intent(getApplicationContext(), MusicService.class));

            }
        });
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPlaying) {
                        playPause.setImageResource(R.drawable.ic_play);

                        pause();
                    }else  {
                        playPause.setImageResource(R.drawable.ic_pause);
                        play();
                    }
                }


        });

        ArrayList<AudioModel> songs = new ArrayList<>();

        liste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavorList.class) ;
                intent.putExtra("songs", songs);
                startActivity(intent);
            }
        });



        maBaseManager = new MaBaseManager(this);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get the current drawable
                        Drawable currentDrawable = fav.getDrawable();
                        Drawable heartDrawable = ContextCompat.getDrawable(v.getContext(), R.drawable.heart);

                        if (currentDrawable != null && heartDrawable != null) {
                            if (currentDrawable.getConstantState().equals(heartDrawable.getConstantState())) {
                                fav.setImageResource(R.drawable.plain);
                                favor();
                            }else {
                                fav.setImageResource(R.drawable.heart);
                                disFavor();
                            }
                        }

                    }});
            }
        });




        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous();
            }
        });



        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ACTION_FINISH_ACTIVITY");
        intentFilter.addAction("currentSong");
        intentFilter.addAction(MusicService.ACTION_UPDATE_SONG);
        intentFilter.addAction(MusicService.ACTION_PLAY_PAUSE);

        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getApplicationContext(), MusicService.class));
        unregisterReceiver(myReceiver);
    }

    boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "READ PERMISSION IS REQUIRED, PLEASE ALLOW FROM SETTINGS", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
    }


    private void playItemFav(AudioModel currentSong) {
        isPlaying= true;
        Intent intent = new Intent(ACTION_PLAY_ITEM_FAV);
        intent.putExtra("currentSong" , currentSong);
        sendBroadcast(intent);
    }

    private void next() {
        playPause.setImageResource(R.drawable.ic_pause);
        Intent intent = new Intent(ACTION_NEXT);
        sendBroadcast(intent);
    }

    private void previous() {
        playPause.setImageResource(R.drawable.ic_pause);
        Intent intent = new Intent(ACTION_PREVIOUS);
        sendBroadcast(intent);
    }
    private void pause() {
        isPlaying = false;
        Intent intent = new Intent(ACTION_PAUSE);
        sendBroadcast(intent);
    }
    private void play() {
        isPlaying= true;
        Intent intent = new Intent(ACTION_PLAY);
        sendBroadcast(intent);
    }


    private void favor(){
        maBaseManager.addFavorite(currentSong.getPath(), currentSong.getTitle(), currentSong.getArtist());
    }

    private void disFavor(){
        maBaseManager.removeFavorite(currentSong.getPath());
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction())
            {
                case MusicService.ACTION_UPDATE_SONG:
                    String title = intent.getStringExtra("title");
                    titleTv.setText(title);
                    break;

                case "currentSong":
                    currentSong = (AudioModel) intent.getSerializableExtra("currentSong");
                    Boolean isFavor = maBaseManager.isFavorite(currentSong.getPath());
                    if (isFavor){fav.setImageResource(R.drawable.plain);}else{fav.setImageResource(R.drawable.heart);}




            }

        }
    }
}
