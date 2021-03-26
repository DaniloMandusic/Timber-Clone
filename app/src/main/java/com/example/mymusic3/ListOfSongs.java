package com.example.mymusic3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListOfSongs extends AppCompatActivity {

    ListView listOfSongs;
    ArrayList<Song> arrayList;
    SongAdapter songAdapter;

    Song song1;

    int currentSongPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_songs);

        //this.getSupportActionBar().hide();

        listOfSongs = (ListView) findViewById(R.id.listOfSongs);

        arrayList = new ArrayList<>();

        songAdapter = new SongAdapter(this, R.layout.item_song, arrayList);

        listOfSongs.setAdapter(songAdapter);



        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},99);
            return;
        }
        else{
            //you have permission to read from external storage
            getSongs();
        }

        Song song = arrayList.get(0);
        Intent playSong = new Intent(ListOfSongs.this,MainActivity.class);
        playSong.putExtra("song",song);
//        startActivity(playSong);

        listOfSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = arrayList.get(position);
                Intent playSong = new Intent(ListOfSongs.this,MainActivity.class);
                playSong.putExtra("song",song);
                startActivity(playSong);

            }
        });


        listOfSongs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                song1 = arrayList.get(position);

                //convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,null);
                //ImageView songImage = convertView.findViewById(R.id.songImage);

                currentSongPosition = position;

                Intent setImage = new Intent();
                setImage.setType("image/*");
                setImage.setAction(Intent.ACTION_GET_CONTENT);
                //request code for gallery
                startActivityForResult(Intent.createChooser(setImage,"Pick an image"),123);

                return true;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            Uri imageData = data.getData();

            song1.setImageData(imageData.toString());
            arrayList.set(currentSongPosition,song1);
//            finish();
//            startActivity(getIntent());

            songAdapter = new SongAdapter(this, R.layout.item_song, arrayList);

            listOfSongs.setAdapter(songAdapter);
            songAdapter.notifyDataSetChanged();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 99){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getSongs();
            }
        }
    }

    private void getSongs(){
        //read songs from phone

        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor songCursor = contentResolver.query(songUri,null,null,null,null);
        if(songCursor != null && songCursor.moveToFirst()){

            int indexTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int indexData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do{
                String title = songCursor.getString(indexTitle);
                String path = songCursor.getString(indexData);

                arrayList.add(new Song(title, path));

            }while(songCursor.moveToNext());

        }

        songAdapter.notifyDataSetChanged();

    }
}