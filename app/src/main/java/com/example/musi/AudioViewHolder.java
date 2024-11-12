package com.example.musi;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class AudioViewHolder extends RecyclerView.ViewHolder  {

    TextView tit;
    TextView artist;
    public AudioViewHolder(@NonNull View itemView) {
        super(itemView);
        findViews(itemView);
    }

    public void findViews (View itemView){
        tit = (TextView) itemView.findViewById(R.id.tvTitle);
        tit = (TextView) itemView.findViewById(R.id.tvArtist);


    }

    public void setItem(final AudioModel audio) {
        tit.setText(audio.getTitle());
        artist.setText(audio.getArtist());
    }

}


