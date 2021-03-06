package com.example.runaway;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class ObstacleManager {

    private ArrayList<Obstacle>obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;
    private int k;


    private float scale;

    private long startTime;


    public static int score = 0;



    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color){
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        startTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();

        populateObstacles();

        scale = 0.5f;

    }


    public boolean playerCollide(RectPlayer player){
        for(Obstacle ob : obstacles){
            if(ob.playerCollide(player)){
                return true;
            }
        }
        return false;
    }

    public void populateObstacles(){
        int currY = -5*Constants.SCREEN_HEIGHT/4;
        while(currY < 0) {
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(new Obstacle(obstacleHeight, color, xStart, currY, playerGap));
            currY += obstacleHeight + obstacleGap;
        }
    }

    public void update() {
        if(startTime < Constants.INIT_TIME){
            startTime = Constants.INIT_TIME;}
        int elapsedTime = (int)(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = Constants.SCREEN_HEIGHT/(10000.0f - 5000);
        for(Obstacle ob: obstacles){
            ob.incrementY(speed * elapsedTime * scale);
            if(scale < 3) scale += 0.0009f;
        }
        if(obstacles.get(obstacles.size()- 1).getRectangle().top >= Constants.SCREEN_HEIGHT){
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(0, new Obstacle(obstacleHeight, color, xStart,obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap , playerGap));
            obstacles.remove(obstacles.size()-1);
            score++;
        }
    }

    public void draw(Canvas canvas){
        for(Obstacle ob: obstacles)
            ob.draw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(60);
        paint.setColor(Color.rgb(112, 76, 164));
        canvas.drawText("" + score, 50, 50 + paint.descent() - paint.ascent(), paint);

        Paint paintt = new Paint();
        paintt.setTextSize(60);
        paintt.setColor(Color.rgb(112, 76, 164));
        k = MainActivity.high.getInt("Best", 0);
        canvas.drawText("Best:" + k, 295, 50 + paintt.descent() - paintt.ascent(), paintt);
    }
}
