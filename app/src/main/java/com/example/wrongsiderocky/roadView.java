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
    public trafficCars car1;
    public trafficCars car2;
    public trafficCars car3;
    public trafficCars car4;
    public Police police;
    List<trafficCars> cars;
    boolean intersectsrightleft,intersectstopbottom;
    Random generator = new Random();
    Shield shield;


    public roadView(Context context, int x, int y) {
        super(context);
        this.context = context;
        ourHolder = getHolder();
        paint = new Paint();
        screenX = x;
        screenY = y;
        cars = new ArrayList<trafficCars>();
        player = new Rocky(context,screenX,screenY);
        car1 = new trafficCars(context,screenX,screenY);
        car2 = new trafficCars(context,screenX,screenY);
        car3 = new trafficCars(context,screenX,screenY);
        car4 = new trafficCars(context,screenX,screenY);
        shield = new Shield(context,screenX,screenY);
        cars.add(car1);
        cars.add(car2);
        cars.add(car3);
        cars.add(car4);
        police = new Police(context,screenX,screenY);
        intersectsrightleft = false;
        intersectstopbottom = false;

    }
    private void update(){
        player.update();
        car1.update(player.getSpeed(),context,screenY,0);
        car2.update(player.getSpeed(), context,screenY,0);
        car3.update(player.getSpeed(), context,screenY,0);
        car4.update(player.getSpeed(), context,screenY,0);
        shield.update(player.getSpeed());

        police.update(player,player.getSpeed(), (ArrayList<trafficCars>) cars);

    }
    private void draw(){
        if(ourHolder.getSurface().isValid()){
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255,
                    0,
                    0,
                    0));
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint
            );
            canvas.drawBitmap
                    (car1.getBitmap(),
                            car1.getX(),
                            car1.getY(), paint);
            canvas.drawBitmap
                    (car2.getBitmap(),
                            car2.getX(),
                            car2.getY(), paint);
            canvas.drawBitmap
                    (car3.getBitmap(),
                            car3.getX(),
                            car3.getY(), paint);
            canvas.drawBitmap
                    (car4.getBitmap(),
                            car4.getX(),
                            car4.getY(), paint);
            canvas.drawBitmap
                    (police.getBitmap(),
                            police.getX(),
                            police.getY(), paint);
            canvas.drawBitmap
                    (shield.getBitmap(),
                            shield.getX(),
                            shield.getY(), paint);

//            Rect shieldhiitbox = shield.getHitbox();
//
//            Paint shieldpaint = new Paint();
//            shieldpaint.setColor(Color.BLUE);
//            canvas.drawRect(shieldhiitbox,shieldpaint);


//            Paint policePaint = new Paint();
//            policePaint.setColor(Color.RED); // Color for police hitbox
//            for (trafficCars car : cars) {
//                Rect policeHitbox = police.getHitbox();
//                canvas.drawRect(policeHitbox, policePaint);
//            }
//
//            // Draw cars' hitboxes
//            Paint carPaint = new Paint();
//            carPaint.setColor(Color.BLUE); // Color for cars' hitboxes
//            for (trafficCars car : cars) {
//                Rect carHitbox = car.getHitbox();
//                canvas.drawRect(carHitbox, carPaint);
//            }

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
                player.setY(touchY);
                break;

        }
        return true;
    }
}
