package com.example.runaway;

import android.content.ComponentName;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.widget.Toast;

import static android.graphics.Color.RED;
import static com.example.runaway.MainActivity.ed;
import static com.example.runaway.MainActivity.mediaPlayer;
import static com.example.runaway.ObstacleManager.score;

public class GameplayScene implements Scene{

    private Rect r = new Rect();

    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private int length = 0;

    private boolean movingPlayer = false;

    private long gameOverTime;

    private boolean gameOver = false;



    public int l;

    private OrientationData orientationData;
    private long frameTime;

    public GameplayScene(){ player = new RectPlayer(new Rect(100,100,200,200), Color.rgb(255,0, 0));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200,500, 60, Color.GRAY);

        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();
    }



    public void reset(){
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        mediaPlayer.start();

        obstacleManager = new ObstacleManager(200,400, 60, Color.GRAY);
        movingPlayer = false;
    }

    @Override
    public void update() {

            if(!gameOver) {
            if(frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            int elapsedTime = (int)(System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            if(orientationData.getOrientation() != null && orientationData.getStartOrientation() != null) {
                float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];

                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH/1000f;
                float ySpeed = pitch * Constants.SCREEN_HEIGHT/1000f;

                playerPoint.x += Math.abs(xSpeed*elapsedTime) > 5 ? xSpeed*elapsedTime : 0;
                playerPoint.y -= Math.abs(ySpeed*elapsedTime) > 5 ? ySpeed*elapsedTime : 0;
            }

            if(playerPoint.x < 0)
                playerPoint.x = 0;
            else if(playerPoint.x > Constants.SCREEN_WIDTH)
                playerPoint.x = Constants.SCREEN_WIDTH;
            if(playerPoint.y < 0)
                playerPoint.y = 0;
            else if(playerPoint.y > Constants.SCREEN_HEIGHT)
                playerPoint.y = Constants.SCREEN_HEIGHT;

            player.update(playerPoint);
            obstacleManager.update();

            if(obstacleManager.playerCollide(player)) {
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }

        l = MainActivity.high.getInt("Best", 0);
        if(gameOver && l < score){
            ed.putInt("Best", score);
            ed.commit();
            ed.apply();
        }
        if(gameOver) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(length);

        }
    }

    @Override
    public void recieveTouch(MotionEvent event) {
         switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && player.getRectangle().contains((int)event.getX(), (int)event.getY()))
                    movingPlayer = true;
                if(gameOver && System.currentTimeMillis() - gameOverTime >= 0) {
                    reset();
                    score = 0;
                    gameOver = false;
                    orientationData.newGame();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!gameOver && movingPlayer)
                    playerPoint.set((int)event.getX(), (int)event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawBitmap(GamePanel.plantBG, 0, 0, null);

        player.draw(canvas);
        obstacleManager.draw(canvas);

        if(gameOver){
            Paint paint = new Paint();
            Paint paint2 = new Paint();
            paint.setTextSize(75);
            paint2.setTextSize(60);
            paint.setColor(RED);
            paint2.setColor(RED);
            drawCenterText(canvas, paint,"Game Over");
            drawText(canvas, paint2, "Tap to reset");
        }
    }


    private void drawText(Canvas canvas, Paint paint, String text){
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom + 250;
        canvas.drawText(text, x, y, paint);
    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }
}




