package com.example.runaway;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private SceneManager manager;
    public static Bitmap plantBG;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);



        Constants.CURRENT_CONTEXT = context;

        thread = new MainThread(getHolder(), this);

        manager = new SceneManager();

        setFocusable(true);
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);
        Constants.INIT_TIME = System.currentTimeMillis();
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry){
            try{
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {e.printStackTrace();}
            retry = false;
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event){
            manager.recieveTouch(event);
        return true;
        //return super.onTouchEvent(event);
    }
    public void update(){
        manager.update();
    }
    @Override
    public void draw(Canvas canvas){

        plantBG = BitmapFactory.decodeResource(getResources(), R.drawable.space2);

        super.draw(canvas);

        manager.draw(canvas);
    }


}
