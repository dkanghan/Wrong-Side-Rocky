package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class roadView extends SurfaceView implements Runnable {

    private final int screenX;
    private final int screenY;
    volatile boolean playing;
    volatile boolean gameEnded;
    Thread gameThread = null;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    private Context context;
    int shield;
    private GameObjects gameObjects;
    LevelManager lm;
    private boolean hitdetected;
    SoundManager sm;

    private boolean isBGPlaying;
    private int distance;
    private Bitmap playBtn;
    private boolean boosting;
    private int boostingdistance;


    public roadView(Context context, int x, int y) {
        super(context);
        lm = new LevelManager();
        this.context = context;
        isBGPlaying = false;
        playing = true;
        ourHolder = getHolder();
        paint = new Paint();
        screenX = x;
        screenY = y;
        boostingdistance = 0;
        gameObjects = new GameObjects(context, screenX, screenY);
        distance = 0;
        gameObjects.initializeObjects(lm.getCurrentLevel());
        shield = 1;
        hitdetected = false;
        gameEnded = false;
        playBtn = BitmapFactory.decodeResource(context.getResources(), R.drawable.playbtn);
        playBtn = Bitmap.createScaledBitmap(playBtn, 48, 48, true);
        sm = new SoundManager();
        sm.loadSound(context);

    }
    private void update() throws RuntimeException, InterruptedException, IOException {
        if(playing) {
            if(!isBGPlaying){
                sm.playBgMusic(lm.getCurrentLevel());
                isBGPlaying = true;
            }
            if (distance++ == 3000 || distance++ == 8000){
                nextLevel();
            }
            gameObjects.updateObjects();

            if (lm.getCurrentLevel()!=1) {
                if (Rect.intersects(gameObjects.getPlayer().getHitbox(), gameObjects.getShield().getHitbox())) {
                    if (shield <= 2) {
                        shield++;
                        sm.playSound("extra_life");
                        gameObjects.getShield().setvisible(false);
                        gameObjects.getShield().setX(-screenX);
                    }

                }

                if(Rect.intersects(gameObjects.getPlayer().getHitbox(),gameObjects.getSpeedBoost().getHitbox())){
                    boosting = true;
                    boostingdistance = distance;
                }
                if(boosting){
                    if(distance-boostingdistance <= 100){
                        distance += 5;
                        gameObjects.getPlayer().startBoosting();
                    }else {
                        gameObjects.getPlayer().stopBoosting();
                    }
                }

            }

            if (hitdetected) {
                if (shield > 0) {
                    shield--;
                    sm.playSound("hit");
                    hitdetected = false;
                } else {
                    sm.playSound("explode");
                    sm.stopAll();
                    playing = false;
                    gameEnded = true;
                }
            }
            if(gameEnded){
                sm.stopBGMusic();

            }

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
            if(playing){

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
                        if(police.getX() <= 0){
                            sm.stop("police");
                        }
                        if(police.getX() >= screenX-police.getBitmap().getWidth() && police.getX() <= screenX){
                            sm.playSound("police");
                        }
                        canvas.drawBitmap(police.getBitmap(), police.getX(), police.getY(), paint);
                        if(Rect.intersects(gameObjects.getPlayer().getHitbox(),police.getHitbox())){
                            hitdetected = true;
                            police.setX(-screenX);
                        }
                    }

                    canvas.drawBitmap(
                                gameObjects.getBlockade().getBitmap(),
                                gameObjects.getBlockade().getX(),
                                gameObjects.getBlockade().getY(),
                                paint
                        );
                    canvas.drawBitmap(
                            gameObjects.getSpeedBoost().getBitmap(),
                            gameObjects.getSpeedBoost().getX(),
                            gameObjects.getSpeedBoost().getY(),
                            paint
                    );
                    if(Rect.intersects(gameObjects.getPlayer().getHitbox(),gameObjects.getBlockade().getHitbox())){
                            hitdetected = true;
                            gameObjects.getBlockade().setX(-screenX);
                        }
                    if(Rect.intersects(gameObjects.getPlayer().getHitbox(),gameObjects.getSpeedBoost().getHitbox())){
                        gameObjects.getSpeedBoost().setX(-screenX);
                    }

                }

            for(trafficCars car: gameObjects.getCars()){
                canvas.drawBitmap(car.getBitmap(), car.getX(), car.getY(), paint);
                if(Rect.intersects(gameObjects.getPlayer().getHitbox(),car.getHitbox())){
                    hitdetected = true;
                    car.setX(-screenX);
                }
            }
            }
            if(!gameEnded){
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(25);
                canvas.drawText("Distance:" +
                        distance +
                        "m", 50 ,  50, paint);

            }
          ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void nextLevel() throws InterruptedException {
        sm.stopAll();
        sm.stopBGMusic();
        isBGPlaying = false;
        lm.nextLevel();
       canvas = ourHolder.lockCanvas();
       canvas.drawColor(Color.BLACK);
       paint.setColor(Color.YELLOW);
       paint.setTextSize(120);
       paint.setTextAlign(Paint.Align.CENTER);
       canvas.drawText("NEXT LEVEL : "+(Integer.toString(lm.getCurrentLevel())), canvas.getWidth()/2, canvas.getHeight()/2 , paint);
       canvas.drawBitmap(playBtn, (float) screenX /2, (float) (screenY /2 + 200), paint);
       ourHolder.unlockCanvasAndPost(canvas);
       sm.playSound("next_level");

       pause();



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
        } catch (InterruptedException ignored) {
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
                } catch (RuntimeException | InterruptedException | IOException e) {
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
                    if (touchX>= screenX/2 - 24 && touchX<= screenX/2 + 24 && touchY >= (screenY/2+200) - 24 && touchY <= (screenY/2+200) + 24){
                        gameObjects.initializeObjects(lm.getCurrentLevel());
                        sm.stop("next_level");
                        resume();

                    }
                }

        }
        return true;
    }
}
