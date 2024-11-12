package com.example.musi;

import java.io.Serializable;

public class AudioModel implements Serializable {
        String path;
        String title;
        Boolean fav = false ;
        String artist;

        public AudioModel(String path, String title,  String artist ) {
            this.path = path;
            this.title = title;
            this.artist= artist;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }



        public String getArtist() {return artist;}
        public void setArtist(String artist) {this.artist= artist;}

        public Boolean getFav() {return fav;}
        public void setFav(Boolean fav) {this.fav= fav;}


}
