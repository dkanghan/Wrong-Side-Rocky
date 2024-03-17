package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class roadView extends SurfaceView implements Runnable {

    private final int screenX;
    private final int screenY;
    volatile boolean playing;
    Thread gameThread = null;
    private Rocky player;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    private Context context;
//    public trafficCars car1;
//    public trafficCars car2;
//    public trafficCars car3;
//    public trafficCars car4;
//    public Police police;
//    List<trafficCars> cars;
    boolean intersectsrightleft,intersectstopbottom;
    Random generator = new Random();
    Shield shield;
    private GameObjects gameObjects;

    LevelManager lm;


    public roadView(Context context, int x, int y) {
        super(context);

        this.context = context;
        ourHolder = getHolder();
        paint = new Paint();
        screenX = x;
        screenY = y;
        gameObjects = new GameObjects(context, screenX, screenY);
        gameObjects.initializeObjects(1);


        intersectsrightleft = false;
        intersectstopbottom = false;

    }
    private void update(){

        gameObjects.updateObjects();

    }
    private void draw(){
        if(ourHolder.getSurface().isValid()){
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255,
                    0,
                    0,
                    0));
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawBitmap(gameObjects.getRoadBitmap(), 0, (float) screenY / 2- (float) gameObjects.getRoadBitmap().getHeight() / 2, paint);
            canvas.drawBitmap(
                    gameObjects.getPlayer().getBitmap(),
                    gameObjects.getPlayer().getX(),
                    gameObjects.getPlayer().getY(),
                    paint
            );

            for(trafficCars car: gameObjects.getCars()){
                canvas.drawBitmap(car.getBitmap(), car.getX(), car.getY(), paint);
            }
            for(Police police: gameObjects.getPolice()){
                canvas.drawBitmap(police.getBitmap(), police.getX(), police.getY(), paint);
            }

            ourHolder.unlockCanvasAndPost(canvas);
        }
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
            update();
            draw();
            control();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        int touchX = (int) motionEvent.getX();
        int touchY = (int) motionEvent.getY();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_MOVE:
//                player.setX(touchX);
                gameObjects.getPlayer().setY(touchY);
                break;

        }
        return true;
    }
}
