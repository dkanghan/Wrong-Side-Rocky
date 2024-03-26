package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//-----------------------------------------------------------------------
//Shield
//Class to initialize the shield Object
//-----------------------------------------------------------------------
public class Shield {
    private final List<Bitmap> bitmaps;
    private int x, y;
    private final int maxY;

    private final int maxX;
    private final int minX;
    private final int frameCount = 8;
    private final Rect hitBox;
    private boolean visible = true;

    private final int frameWidth;
    private int currentFrame = 0;
    private long lastFrameTime;

    //-----------------------------------------------------------------------
    //Constructor
    //initializes the bitmap frames and variables for the class
    //initializes the hitbox for the class
    //initializes the base x and y coordinates of object
    //-----------------------------------------------------------------------
    public Shield(Context context, int screenX, int screenY, int y) {
        bitmaps = new ArrayList<>();

        Bitmap shieldBitmap = BitmapFactory.decodeResource(
                context.getResources(), R.drawable.shield);

        frameWidth = shieldBitmap.getWidth() / frameCount;
        int frameHeight = shieldBitmap.getHeight();

        for (int i = 0; i < frameCount; i++) {
            int startX = i * frameWidth;
            Bitmap frameBitmap = Bitmap.createBitmap(shieldBitmap, startX, 0, frameWidth, frameHeight);
            bitmaps.add(frameBitmap);
        }

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        hitBox = new Rect(x, y, bitmaps.get(0).getWidth(), bitmaps.get(0).getHeight());
        this.y = maxY/2 - y;
        x = screenX;
    }

    //------------------------------------------------------------------------------------
    // Update()
    // Moves the shield object
    // Respawns the bitmap on the screen when object moves out of screen
    //------------------------------------------------------------------------------------
    public void update(int playerSpeed, int playerHeight) {
        x -= playerSpeed*10;
        Random generator = new Random();

        //If object goes out of screen
        //reInitialize it
        if (x < minX - frameWidth - 10) {
            x = maxX*generator.nextInt(5);
            y = generator.nextInt(2*playerHeight)+maxY/3;
            this.visible = true;
        }

        //reinitialize hitbox
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmaps.get(currentFrame).getWidth();
        hitBox.bottom = y + bitmaps.get(currentFrame).getHeight();

        // Update current frame
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


    //getters and setters
    public Bitmap getBitmap() {
        return bitmaps.get(currentFrame);
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
    public void setvisible(boolean visible){ this.visible=visible;}

    public boolean isVisible() {
        return visible;
    }
}
