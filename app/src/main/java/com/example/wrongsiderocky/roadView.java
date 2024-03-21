package com.example.wrongsiderocky;

import android.content.Context;
import android.content.SharedPreferences;
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
    private boolean collisionDetected, hitDetected;
    SoundManager sm;

    private boolean isBGPlaying;
    private int distance;
    private Bitmap playBtn;
    private boolean boosting;
    private int boostingdistance;
    private healthbar healthbar1,healthbar2;
    private long timeTaken, timeStarted;
    private long longestDistance;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private boolean pause;


    public roadView(Context context, int x, int y) {
        super(context);
        this.context = context;

        prefs = context.getSharedPreferences("HiScores", Context.MODE_PRIVATE);
        editor = prefs.edit();
        longestDistance = prefs.getLong("LongestDistance", 1000);
        screenX = x;
        screenY = y;
        startGame();

    }
    private void startGame(){
        pause = false;
        lm = new LevelManager();
        isBGPlaying = false;
        playing = true;
        ourHolder = getHolder();
        paint = new Paint();
        boostingdistance = 0;
        gameObjects = new GameObjects(context, screenX, screenY);
        distance = 0;
        gameObjects.initializeObjects(lm.getCurrentLevel());
        shield = 0;
        collisionDetected = false;
        hitDetected = false;
        gameEnded = false;
        playBtn = BitmapFactory.decodeResource(context.getResources(), R.drawable.playbtn);
        playBtn = Bitmap.createScaledBitmap(playBtn, 48, 48, true);
        healthbar1 = new healthbar(context,screenX,screenY,1);
        healthbar2 = new healthbar(context,screenX,screenY,2);
        sm = new SoundManager();
        sm.loadSound(context);
        timeTaken = 0;
        timeStarted = System.currentTimeMillis();
    }
    private void update() throws RuntimeException, InterruptedException, IOException {
        if(playing) {
            if(!isBGPlaying){
                sm.playBgMusic(lm.getCurrentLevel());
                isBGPlaying = true;
            }
            if (distance++ == 100 || distance++ == 2000){
                nextLevel();
            }
            gameObjects.updateObjects();

            if (lm.getCurrentLevel()!=1) {
                if (Rect.intersects(gameObjects.getPlayer().getHitbox(), gameObjects.getShield().getHitbox())) {
                    if (shield < 4) {

                        sm.playSound("extra_life");
                        gameObjects.getShield().setvisible(false);
                        gameObjects.getShield().setX(-screenX);
                        if(shield==0){
                            shield += 2;
                            healthbar1.increaseFrame();
                            healthbar1.increaseFrame();
                        }else if (shield == 1){
                            shield += 2;
                            healthbar1.increaseFrame();
                            healthbar2.increaseFrame();
                        }else if (shield == 2){
                            shield += 2;
                            healthbar2.increaseFrame();
                            healthbar2.increaseFrame();
                        }else if (shield == 3){
                            shield += 1;
                            healthbar2.increaseFrame();
                        }

                    }

                }

                if(Rect.intersects(gameObjects.getPlayer().getHitbox(),gameObjects.getSpeedBoost().getHitbox())){
                    boosting = true;
                    boostingdistance = distance;
                }
                if(boosting){
                    if(distance-boostingdistance <= 50){
                        distance += 5;
                        gameObjects.getPlayer().startBoosting();
                    }else {
                        gameObjects.getPlayer().stopBoosting();
                    }
                }

            }

            if (collisionDetected) {
                if (shield > 1) {
                    sm.playSound("explode");

                    if(shield==2){
                        healthbar1.decreaseFrame();
                        healthbar1.decreaseFrame();
                    }else if(shield == 3){
                        healthbar2.decreaseFrame();
                        healthbar1.decreaseFrame();
                    }else{
                        healthbar2.decreaseFrame();
                        healthbar2.decreaseFrame();
                    }
                    shield -= 2;
                    collisionDetected = false;
                } else {
                    sm.playSound("explode");
                    sm.stopAll();
                    playing = false;
                    gameEnded = true;
                }
            }

            if(hitDetected){
                if (shield > 0) {
                    shield--;
                    sm.playSound("hit");
                    hitDetected = false;
                    if(shield<=2){
                        healthbar1.decreaseFrame();
                    }else{
                        healthbar2.decreaseFrame();
                    }

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
            timeTaken = System.currentTimeMillis() - timeStarted;
            if(distance > longestDistance){
                editor.putLong("LongestDistance", distance);
                editor.commit();
                longestDistance = distance;
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

                canvas.drawBitmap(gameObjects.getPauseBitmap(), screenX - 100, 60 ,paint);

                canvas.drawBitmap(gameObjects.getRoadBitmap(), gameObjects.getRoad().getX(),gameObjects.getRoad().getY() , paint);
                canvas.drawBitmap(
                        gameObjects.getPlayer().getBitmap(),
                        gameObjects.getPlayer().getX(),
                        gameObjects.getPlayer().getY(),
                        paint
                );

                canvas.drawBitmap(healthbar1.getBitmap(), healthbar1.getX(), healthbar1.getY(),paint);
                canvas.drawBitmap(healthbar2.getBitmap(), healthbar2.getX(), healthbar2.getY(),paint);

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
                            collisionDetected = true;
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
                            hitDetected = true;
                            gameObjects.getBlockade().setX(-screenX-100);
                        }
                    if(Rect.intersects(gameObjects.getPlayer().getHitbox(),gameObjects.getSpeedBoost().getHitbox())){
                        gameObjects.getSpeedBoost().setX(-screenX-100);
                    }

                }

            for(trafficCars car: gameObjects.getCars()){
                canvas.drawBitmap(car.getBitmap(), car.getX(), car.getY(), paint);
                if(Rect.intersects(gameObjects.getPlayer().getHitbox(),car.getHitbox())){
                    collisionDetected = true;
                    car.setX(-screenX-100);
                }
            }

            for(roadObjects roadObjects : gameObjects.getLamp()){
                canvas.drawBitmap(roadObjects.getBitmap(), roadObjects.getX(), roadObjects.getY(), paint);
            }

            }
            if(!gameEnded){
                timeTaken = System.currentTimeMillis() - timeStarted;
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(25);
                canvas.drawText("Distance:" +
                        distance +
                        "m", screenX - 300 ,  screenY - 50, paint);

            }else{
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over", (float) screenX / 2, 100, paint);
                paint.setTextSize(25);
                canvas.drawText("Distance Travelled:" +
                        distance + "m", (float) screenX / 2, 160, paint);
                canvas.drawText("Longest Distance Travelled:" +
                        longestDistance + "m", (float) screenX / 2, 250, paint);
                canvas.drawText("Time:" + formatTime(timeTaken) +
                        "s", (float) screenX / 2, 200, paint);
                paint.setTextSize(80);
                canvas.drawText("Tap to replay", (float) screenX / 2, 350, paint);

            }
          ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private String formatTime(long time) {
        long seconds = (time) / 1000;
        long thousandths = (time) - (seconds * 1000);
        String strThousandths = String.valueOf(thousandths);
        if (thousandths < 100) {
            strThousandths = "0" + thousandths;
        }
        if (thousandths < 10) {
            strThousandths = "0" + strThousandths;
        }
        String stringTime = seconds + "." + strThousandths;
        return stringTime;
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
        pause = true;
        try {
            gameThread.join();
        } catch (InterruptedException ignored) {
        }
    }

    public void resume() {
        playing = true;
        pause = false;
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
                }else{
                    boolean condition = touchX >= screenX - 148 && touchX <= screenX + 148 && touchY >= 60 - 48 && touchY <= 60 + 48;
                    if(pause) {
                        if (condition) {
                            resume();
                        }
                    }else{
                        if (condition) {
                            pause();
                        }
                    }
                }

                if(gameEnded){
                    startGame();
                }
                break;
        }
        return true;
    }
}
