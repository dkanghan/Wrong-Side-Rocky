package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class healthbar {
    private List<Bitmap> bitmaps;
    private int x, y;
    private final int frameDuration = 100;
    private int frameCount = 3;
    private final Rect hitBox;
    private int frameWidth, frameHeight;
    private int currentFrame = 0;
    private long lastFrameTime;
    public healthbar(Context context, int screenX, int screenY, int num){
        bitmaps = new ArrayList<>();

        Bitmap healthBitmap = BitmapFactory.decodeResource(
                context.getResources(), R.drawable.healthbar);
        healthBitmap = Bitmap.createScaledBitmap(healthBitmap, 90,30,true);

        frameWidth = healthBitmap.getWidth() / frameCount;
        frameHeight = healthBitmap.getHeight();

        for (int i = 0; i < frameCount; i++) {
            int startX = i * frameWidth;
            Bitmap frameBitmap = Bitmap.createBitmap(healthBitmap, startX, 0, frameWidth, frameHeight);
            bitmaps.add(frameBitmap);
        }

        hitBox = new Rect(x, y, bitmaps.get(0).getWidth(), bitmaps.get(0).getHeight());
        if (num == 1) {
            y = 20;
            x = 20;
        }
        else if(num == 2){
            y = 20;
            x = 20+bitmaps.get(0).getWidth();
        }


    }
    public void update() {

        long time = System.currentTimeMillis();
        if (time > lastFrameTime + frameDuration) {
            currentFrame++;
            if (currentFrame >= frameCount) {
                currentFrame = 0;
            }
            lastFrameTime = time;
        }
    }

    public void increaseFrame(){
        if(currentFrame<3){
            currentFrame++;
        }

    }
    public void decreaseFrame(){
        if(currentFrame>0){
            currentFrame--;
        }
    }

    public Bitmap getBitmap() {
        return bitmaps.get(currentFrame); // Return current frame
    }

    public Rect getHitbox() {
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
}
