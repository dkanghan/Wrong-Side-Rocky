package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class trafficCars {
    private Bitmap bitmap;
    private Bitmap[] bitmapAm;


    private int x,y;
    private int speed = 1;
    private final int maxY;
    private final int minY;
    private final int maxX;
    private final int minX;
    private final Rect hitBox;
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;
    private int frameCount = 3;
    private long frameDuration = 100;
    private int frameWidth, frameHeight;
    private int currentFrame;
    private long lastFrameTime;
    private boolean ambulance;

    public trafficCars(Context context, int screenX, int screenY, int y){
        Random generator = new Random();
        int cars = generator.nextInt(4);
        if(cars == 0) {
            bitmap = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.car);
        }
        else if (cars == 1) {
            bitmap = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.truck);

        }
        else if (cars == 2){

            bitmapAm = new Bitmap[3];
            bitmapAm[0] = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.ambulance1);
            bitmapAm[1] = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.ambulance2);
            bitmapAm[2] = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.ambulance3);


            frameWidth = bitmapAm[0].getWidth();
            frameHeight = bitmapAm[0].getHeight();
            lastFrameTime = System.currentTimeMillis();
            ambulance = true;

        }
        else {
            bitmap = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.mini_truck);
        }


        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        this.y = y;
        if(ambulance){
            hitBox = new Rect(x, y, frameWidth, frameHeight);
        }else {
            hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
        }
        speed = generator.nextInt(6)+10;
        x = screenX;


    }

    public void update(int playerSpeed, Context context, int maxY, int minY){

        x -= playerSpeed;
        x -= speed;

        if(ambulance){
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastFrameTime > frameDuration) {
                currentFrame = (currentFrame + 1) % frameCount;
                lastFrameTime = currentTime;
            }

            if(x < minX-frameWidth){
                Random generator = new Random();
                speed = generator.nextInt(10)+10;
                x = maxX;
//                y = generator.nextInt(maxY - frameHeight + 1)+ minY ;
            }

            hitBox.left = x;
            hitBox.top = y;
            hitBox.right = x + frameWidth;
            hitBox.bottom = y + frameHeight;
        }
        else {
            if(x < minX-bitmap.getWidth()){
                Random generator = new Random();
                int cars = generator.nextInt(3);
                if(cars == 0) {
                    bitmap = BitmapFactory.decodeResource
                            (context.getResources(), R.drawable.car);
                }
                else if (cars == 1) {
                    bitmap = BitmapFactory.decodeResource
                            (context.getResources(), R.drawable.truck);

                }
                else {
                    bitmap = BitmapFactory.decodeResource
                            (context.getResources(), R.drawable.mini_truck);
                }
                speed = generator.nextInt(10)+10;
                x = maxX;
                //y = generator.nextInt(maxY - bitmap.getHeight() + 1) + minY;
            }

            hitBox.left = x;
            hitBox.top = y;
            hitBox.right = x + bitmap.getWidth();
            hitBox.bottom = y + bitmap.getHeight();

        }


    }

    public int getMAX_SPEED() {
        return MAX_SPEED;
    }

    public int getMIN_SPEED() {
        return MIN_SPEED;
    }

    public Bitmap getBitmap(){
        if (ambulance){
            return bitmapAm[currentFrame];
        }
        return bitmap;
    }
    public Rect getHitbox(){
        return hitBox;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }

    public void setSpeed(int x) {
        this.speed = x;
    }
    public int getSpeed(){return speed;}


}
