package com.example.runaway;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends  Activity {
    public static SharedPreferences high;
    public static SharedPreferences.Editor ed;
    public static MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.killmeplss);
        mediaPlayer.setLooping(true);

         high = getSharedPreferences("Best", MODE_PRIVATE);
         ed = high.edit();
         ed.apply();
         ed.commit();

        DisplayMetrics dn = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dn);
        Constants.SCREEN_WIDTH = dn.widthPixels;
        Constants.SCREEN_HEIGHT = dn.heightPixels;

        setContentView(R.layout.activity_main);

    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null){
            mediaPlayer.pause();
            if (isFinishing()){
                mediaPlayer.pause();
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }

    }


        public void click(View view){
            setContentView(new GamePanel(this));
            if (isFinishing()){
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }
    }

