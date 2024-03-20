package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Road {
    private Bitmap bitmap;
    private Bitmap[] bitmapAm;


    private int x,y;
    private int speed = 1;
    private final int maxY;
    private final int minY;
    private final int maxX;
    private final int minX;
    private final Rect hitBox;

    private int frameWidth, frameHeight;
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    public Road(Context context, int screenX, int screenY, int level, int playerheight) {
        x = 0;
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        if (level == 1) {
            bitmap = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.road1);
            bitmap = Bitmap.createScaledBitmap(bitmap, screenX * 2, playerheight * 4, false);
        }
        else if(level == 2){
                bitmap = BitmapFactory.decodeResource
                        (context.getResources(), R.drawable.road2);
                bitmap = Bitmap.createScaledBitmap(bitmap, screenX*2, playerheight * 5, false);
        }
        else if(level == 3){
            bitmap = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.road3);
            bitmap = Bitmap.createScaledBitmap(bitmap, screenX*2, playerheight * 7, false);
        }

        y = screenY/2 - bitmap.getHeight()/2 ;
        frameWidth = bitmap.getWidth();
        frameHeight = bitmap.getHeight();
        hitBox = new Rect(x, y, frameWidth, frameHeight);
    }

    public void update(int playerSpeed){
        x -= playerSpeed*10;
        if(x < minX-frameWidth/2){
            x = 0;
        }

    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Rect getHitbox(){
        return hitBox;
    }
}
