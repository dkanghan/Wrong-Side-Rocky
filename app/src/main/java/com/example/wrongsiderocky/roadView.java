package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class roadView extends SurfaceView implements Runnable {

    private final int screenX;
    private final int screenY;
    volatile boolean playing;
    Thread gameThread = null;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    private Context context;
    int shield;
    private GameObjects gameObjects;
    LevelManager lm;
    private boolean hitdetected;
    private boolean isPlaying;

    private int distance;
    Bitmap playBtn;


    public roadView(Context context, int x, int y) {
        super(context);
        lm = new LevelManager();
        this.context = context;
        isPlaying = true;
        ourHolder = getHolder();
        paint = new Paint();
        screenX = x;
        screenY = y;
        gameObjects = new GameObjects(context, screenX, screenY);
        distance = 0;
        gameObjects.initializeObjects(lm.getCurrentLevel());
        shield = 0;
        hitdetected = false;
        playBtn = BitmapFactory.decodeResource(context.getResources(), R.drawable.playbtn);
        playBtn = Bitmap.createScaledBitmap(playBtn, 24, 24, true);

    }
    private void update() throws RuntimeException, InterruptedException {
        if(isPlaying) {
            if (distance == 100){
                nextLevel();
            }
            gameObjects.updateObjects();

            if (lm.getCurrentLevel()!=1) {
                if (Rect.intersects(gameObjects.getPlayer().getHitbox(), gameObjects.getShield().getHitbox())) {
                    if (shield <= 2) {
                        shield++;
                    }
                    gameObjects.getShield().setvisible(false);
                    gameObjects.getShield().setX(-screenX);
                    System.out.println(shield);
                }
                if (hitdetected) {
                    if (shield > 0) {
                        shield--;
                    } else {
                        isPlaying = false;
                    }
                }
            }
            distance++;
        }

    }
    private void draw() throws RuntimeException {
        if(ourHolder.getSurface().isValid()){

            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255,
                    0,
                    0,
                    0));
            paint.setColor(Color.argb(255, 255, 255, 255));
            if(isPlaying){

            canvas.drawBitmap(gameObjects.getRoadBitmap(), gameObjects.getRoad().getX(),gameObjects.getRoad().getY() , paint);
            canvas.drawBitmap(
                    gameObjects.getPlayer().getBitmap(),
                    gameObjects.getPlayer().getX(),
                    gameObjects.getPlayer().getY(),
                    paint
            );
                if (lm.getCurrentLevel()!= 1){
                    if (gameObjects.getShield().isVisible()) {
                        canvas.drawBitmap(
                                gameObjects.getShield().getBitmap(),
                                gameObjects.getShield().getX(),
                                gameObjects.getShield().getY(),
                                paint
                        );

                    }
                    for(Police police: gameObjects.getPolice()){
                        canvas.drawBitmap(police.getBitmap(), police.getX(), police.getY(), paint);
                        if(Rect.intersects(gameObjects.getPlayer().getHitbox(),police.getHitbox())){
                            hitdetected = true;
                        }
                    }
                }

            for(trafficCars car: gameObjects.getCars()){
                canvas.drawBitmap(car.getBitmap(), car.getX(), car.getY(), paint);
                if(Rect.intersects(gameObjects.getPlayer().getHitbox(),car.getHitbox())){
                    hitdetected = true;
                }
            }

            }


            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void nextLevel() throws InterruptedException {
        lm.nextLevel();
       canvas = ourHolder.lockCanvas();
       canvas.drawColor(Color.BLACK);
       paint.setColor(Color.YELLOW);
       paint.setTextSize(80);
       canvas.drawText("NEXT LEVEL", screenX/2, 100, paint);

       canvas.drawBitmap(playBtn, (float) screenX /2, (float) screenY /2, paint);

       ourHolder.unlockCanvasAndPost(canvas);
       pause();
       gameObjects.initializeObjects(lm.getCurrentLevel());


    }
    private void control(){
        try {
            Thread.sleep(17);
        } catch (InterruptedException ignored) {

        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public void run() {
        while(playing){


                try {

                    update();
                    draw();
                } catch (RuntimeException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                control();
            }


    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        int touchX = (int) motionEvent.getX();
        int touchY = (int) motionEvent.getY();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_MOVE:
                if(gameObjects.getPlayer().getX()<touchX){
                    gameObjects.getPlayer().setX(touchX);
                }

                gameObjects.getPlayer().setY(touchY);
                break;
            case MotionEvent.ACTION_DOWN:
                if (!playing){
                    if (touchX>= screenX/2 - playBtn.getWidth()/2 && touchX<= screenX/2 + playBtn.getWidth()/2 && touchY >= screenY/2 - playBtn.getHeight()/2 && touchY <= screenY/2 + playBtn.getHeight()/2){
                        resume();
                    }
                }

        }
        return true;
    }
}
