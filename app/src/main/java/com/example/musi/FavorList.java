package com.example.musi;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavorList extends AppCompatActivity {

    private RecyclerView recyView;
    List<AudioModel> songs = new ArrayList<>();
    MaBaseManager maBaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_favor_activity);

        maBaseManager = new MaBaseManager( FavorList.this);
        recyView = findViewById(R.id.liste);
        songs =   maBaseManager.getAllFavorites();
        AudioAdapter audioAdapter = new AudioAdapter(songs);
        recyView.setAdapter(audioAdapter);
        recyView.setLayoutManager(new LinearLayoutManager(this)); // Don't forget to set the layout manager

    }

    @Override
    public void onSaveInstanceState(Bundle etat) {
        super.onSaveInstanceState(etat);
    }
}
