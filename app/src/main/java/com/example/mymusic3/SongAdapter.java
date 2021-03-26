package com.example.mymusic3;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {

    public SongAdapter(@NonNull Context context,int resource, @NonNull List<Song> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,null);
        ImageView songImage = convertView.findViewById(R.id.songImage);

        TextView songTitle = convertView.findViewById(R.id.songTitle);

        Song song = getItem(position);
        songTitle.setText(song.getSongTitle());
        if(song.imageData != null)
            songImage.setImageURI(Uri.parse(song.getImageData()));

        return convertView;
    }
}
