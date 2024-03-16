package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Shield {
    private List<Bitmap> bitmaps;
    private int x, y;
    private final int maxY;
    private final int minY;
    private final int maxX;
    private final int minX;
    private final int frameDuration = 100;
    private int frameCount = 8;
    private final Rect hitBox;

    private int frameWidth, frameHeight;
    private int currentFrame = 0;
    private long lastFrameTime;

    public Shield(Context context, int screenX, int screenY) {
        Random generator = new Random();
        bitmaps = new ArrayList<>();

        Bitmap shieldBitmap = BitmapFactory.decodeResource(
                context.getResources(), R.drawable.shield);

        frameWidth = shieldBitmap.getWidth() / frameCount;
        frameHeight = shieldBitmap.getHeight();

        for (int i = 0; i < frameCount; i++) {
            int startX = i * frameWidth;
            Bitmap frameBitmap = Bitmap.createBitmap(shieldBitmap, startX, 0, frameWidth, frameHeight);
            bitmaps.add(frameBitmap);
        }

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        hitBox = new Rect(x, y, bitmaps.get(0).getWidth(), bitmaps.get(0).getHeight());
        y = generator.nextInt(maxY) - bitmaps.get(0).getHeight();
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
        hitBox.right = x + bitmaps.get(currentFrame).getWidth();
        hitBox.bottom = y + bitmaps.get(currentFrame).getHeight();

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

}
