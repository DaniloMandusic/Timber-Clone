package com.example.mymusic3;

import android.net.Uri;

import java.io.Serializable;

public class Song implements Serializable {

    String songTitle;
    String path;
    public String imageData = null;
    //boolean imageSet = false;


    public Song(String songTitle, String path){

        this.songTitle = songTitle;
        this.path = path;

    }

    public String getImageData(){
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}