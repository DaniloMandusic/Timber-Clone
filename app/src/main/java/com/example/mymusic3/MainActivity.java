package com.example.mymusic3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    Button startButton,pauseButton;
    TextView startTime, endTime, title;
    ImageView image, prevousButton, nextButton, backButton, optionsButton;
    ImageButton playButton, repeatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //this.getSupportActionBar().hide();

        //set up views
        playButton = (ImageButton) findViewById(R.id.buttonPlay);

        //prevousButton = (ImageView) findViewById(R.id.buttonPrev);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        endTime = (TextView) findViewById(R.id.endTime);
        startTime = (TextView) findViewById(R.id.startTime);
        title = (TextView) findViewById(R.id.title);
        image = (ImageView) findViewById(R.id.imageView);
        backButton = (ImageView) findViewById(R.id.backButton);
        optionsButton = (ImageView) findViewById(R.id.options);
        repeatButton = (ImageButton) findViewById(R.id.repeat);

        Song song = (Song) getIntent().getSerializableExtra("song");

        title.setText(song.getSongTitle());

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mediaPlayer.isLooping()){

                    mediaPlayer.setLooping(false);

                    Toast.makeText(MainActivity.this, "Repeat:off", Toast.LENGTH_SHORT).show();

                }
                else{

                    mediaPlayer.setLooping(true);

                    Toast.makeText(MainActivity.this, "Repeat:on", Toast.LENGTH_SHORT).show();

                }

            }
        });

        if(song.imageData != null) {
            Uri imageData = Uri.parse(song.imageData);
            image.setImageURI(imageData);
        }



        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(song.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.seekTo(0);
        seekBar.setProgress(50);


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    playButton.setImageResource(R.drawable.ic__play);
                }
                else{
                    mediaPlayer.start();
                    playButton.setImageResource(R.drawable.ic_pause);
                }

            }
        });

        //set the song max duration
        String endTime = millisecondsToString(mediaPlayer.getDuration());
        this.endTime.setText(endTime);

        //set up seek bar to go along with music
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                //this b is is input from user or somethin idc quite well but put it anyway
                if(b) {
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(mediaPlayer != null){

                    if(mediaPlayer.isPlaying()){
                        try {
                            final double current = mediaPlayer.getCurrentPosition();
                            final String elapsedTime = millisecondsToString((int) current);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startTime.setText(elapsedTime);
                                    seekBar.setProgress((int) current);
                                }
                            });

                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}

                    }

                }

            }
        }).start();

        final Intent goToListOfSongs = new Intent(this, ListOfSongs.class);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                startActivity(goToListOfSongs);

            }
        });

    }

    public String millisecondsToString(int time){

        String elapsedTime = "";
        int minutes = time/1000/60;
        int seconds = time/1000%60;
        elapsedTime = minutes + ":";

        if (seconds<10){

            elapsedTime+= "0";

        }

        elapsedTime+=seconds;

        return elapsedTime;

    }

}