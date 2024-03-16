package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Road {

    private Bitmap roadBitmap; // List to hold each frame of the shield
    private int x, y;
    private final int maxY;
    private final int minY;
    private final int maxX;
    private final int minX;
    private final int frameDuration = 100;
    private int frameCount = 8;
    private Rect hitBox;


    private int frameWidth, frameHeight;
    private int currentFrame = 0;
    private long lastFrameTime;

    public Road(Level level, Context context, int screenX, int screenY) {
        Random generator = new Random();

        roadBitmap = BitmapFactory.decodeResource(
                context.getResources(), R.drawable.road);

        frameWidth = roadBitmap.getWidth() / frameCount;
        frameHeight = roadBitmap.getHeight();

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        hitBox = new Rect(x, y, roadBitmap.getWidth(), roadBitmap.getHeight());
        y = generator.nextInt(maxY) - roadBitmap.getHeight();
        x = screenX;


    }
    public void update(int playerSpeed) {
        x -= playerSpeed;
        Random generator = new Random();

        if (x < minX - frameWidth) {
            x = maxX;
            y = generator.nextInt(maxY - frameHeight + 1);
        }

        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + roadBitmap.getWidth();
        hitBox.bottom = y + roadBitmap.getHeight();

        // Update current frame
        long time = System.currentTimeMillis();
        if (time > lastFrameTime + frameDuration) {
            currentFrame++;
            if (currentFrame >= frameCount) {
                currentFrame = 0;
            }
            lastFrameTime = time;
        }
    }

    public Bitmap getBitmap() {
        return roadBitmap; // Return current frame
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

}
