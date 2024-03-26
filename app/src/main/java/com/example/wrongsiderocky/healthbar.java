package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

//-----------------------------------------------------------------------
//Blockade
//Class to initialize the health bar on top left
//-----------------------------------------------------------------------

public class healthbar {
    private final List<Bitmap> bitmaps;
    private int x, y;
    private final int frameCount = 3;
    private int currentFrame = 0;
    private long lastFrameTime;

    //-----------------------------------------------------------------------
    //Constructor
    //Initializes the bitmap and sets the position of the bitmap
    //-----------------------------------------------------------------------
    public healthbar(Context context, int num){
        bitmaps = new ArrayList<>();

        Bitmap healthBitmap = BitmapFactory.decodeResource(
                context.getResources(), R.drawable.healthbar);
        healthBitmap = Bitmap.createScaledBitmap(healthBitmap, 90,30,true);

        int frameWidth = healthBitmap.getWidth() / frameCount;
        int frameHeight = healthBitmap.getHeight();

        for (int i = 0; i < frameCount; i++) {
            int startX = i * frameWidth;
            Bitmap frameBitmap = Bitmap.createBitmap(healthBitmap, startX, 0, frameWidth, frameHeight);
            bitmaps.add(frameBitmap);
        }

        if (num == 1) {
            y = 20;
            x = 20;
        }
        else if(num == 2){
            y = 20;
            x = 20+bitmaps.get(0).getWidth();
        }


    }

    //-----------------------------------------------------------------------
    //update()
    //Checks the current frame of the bitmap and increases it when called
    //-----------------------------------------------------------------------
    public void update() {

        long time = System.currentTimeMillis();
        int frameDuration = 100;
        if (time > lastFrameTime + frameDuration) {
            currentFrame++;
            if (currentFrame >= frameCount) {
                currentFrame = 0;
            }
            lastFrameTime = time;
        }
    }

    //-----------------------------------------------------------------------
    //increaseFrame()
    //Increases the frame of the bitmap
    //-----------------------------------------------------------------------
    public void increaseFrame(){
        if(currentFrame<3){
            currentFrame++;
        }

    }

    //-----------------------------------------------------------------------
    //decreaseFrame()
    //Decreases the frame of the bitmap
    //-----------------------------------------------------------------------
    public void decreaseFrame(){
        if(currentFrame>0){
            currentFrame--;
        }
    }


    //Getters and Setters
    public Bitmap getBitmap() {
        return bitmaps.get(currentFrame); // Return current frame
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
