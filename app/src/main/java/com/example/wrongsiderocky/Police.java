package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Police {
    private final Bitmap[] bitmap;
    private int x,y;
    private int speed = 1;
    private final int maxY;
    private final int minY;
    private final int maxX;
    private final int minX;
    private final Rect hitBox;
    private int frameCount = 3;
    private long frameDuration = 100;
    private int frameWidth, frameHeight;
    private int currentFrame;
    private long lastFrameTime;

    public Police(Context context, int screenX, int screenY){
        Random generator = new Random();
        bitmap = new Bitmap[3];
        bitmap[0] = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.police1);
        bitmap[1] = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.police2);
        bitmap[2] = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.police3);

        frameWidth = bitmap[0].getWidth();
        frameHeight = bitmap[0].getHeight();
        lastFrameTime = System.currentTimeMillis();

        maxX = screenX;
        maxY = screenY;
        System.out.println(screenY);
        minX = 0;
        minY = 0;
        hitBox = new Rect(x, y, frameWidth, frameHeight);

        speed = generator.nextInt(6)+10;
        x = screenX*3;
        y = screenY/2 ;
    }

    public void update(Rocky player, int playerSpeed, ArrayList<trafficCars> cars ){

        for (trafficCars car : cars) {
            Rect carHitbox = car.getHitbox();
            if (Rect.intersects(hitBox, carHitbox)) {
                Rect policeHitbox = getHitbox();

                if (policeHitbox.top < carHitbox.top && policeHitbox.bottom < carHitbox.bottom)  {
                    x -= playerSpeed/2;
                    x -= speed/2;
                    y -= playerSpeed/6;
                    y -= speed/6;
                } else if (policeHitbox.top > carHitbox.top && policeHitbox.bottom > carHitbox.bottom ) {
                    x -= playerSpeed/2;
                    x -= speed/2;
                    y += playerSpeed/6;
                    y += speed/6;

                }
                else if(policeHitbox.right < carHitbox.right && policeHitbox.left < carHitbox.left){
                    setSpeed(car.getSpeed()-1);
                    if (player.getY() < y && player.getX() < x) {
                        y -= playerSpeed/6;
                        y -= speed/6;
                    } else if (player.getY() > y && player.getX() < x) {
                        y += playerSpeed/6;
                        y += speed/6;
                    }
                } else if(policeHitbox.right > carHitbox.right && policeHitbox.left > carHitbox.left){
                    car.setSpeed(getSpeed()-1);
                    if (player.getY() < y && player.getX() < x) {
                        y -= playerSpeed/6;
                        y -= speed/6;
                    } else if (player.getY() > y && player.getX() < x) {
                        y += playerSpeed/6;
                        y += speed/6;
                    }
                }
            }
            else{
                x -= playerSpeed*3;
                x -= speed/2;
                if (player.getY() < y && player.getX() < x) {
                    y -= speed / 8;
                } else if (player.getY() > y && player.getX() < x) {
                    y += speed / 8;
                }

            }
        }


        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime > frameDuration) {
            currentFrame = (currentFrame + 1) % frameCount;
            lastFrameTime = currentTime;
        }

        if(x < minX-frameWidth){
            Random generator = new Random();
            speed = generator.nextInt(5)+10;
            x = maxX*3;
            y = maxY/2 ;
        }

        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + frameWidth;
        hitBox.bottom = y + frameHeight;


    }

    public Bitmap getBitmap(){
        return bitmap[currentFrame];
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
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int x) {
        speed=x;
    }
    public void setX(int x) {
        this.x = x;
    }

}
