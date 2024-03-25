package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Rocky {
    private Bitmap bitmap;
    private int x,y;
    private int speed = 0;
    private Rect hitBox;
    private int maxY;
    private int minY;
    private int maxX;
    private int minX;
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    public Rocky(Context context, int screenX, int screenY, int minY, int maxY) {
        this.minY = minY;
        this.maxY = maxY;
        x=50;
        y=50;
        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocky);
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
        maxX = screenX/2-bitmap.getWidth();
        minX = 50;

    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    public int getSpeed() {
        return speed;
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

    public void setY(int y) {

        if (y <= minY) {
            this.y = minY;
        }
        else this.y = Math.min(y, maxY);
    }
    public void startBoosting(){
        speed += 1;
    }
    public void stopBoosting(){
        speed -= 1;
    }
    public void increaseSpeed(){ speed += 1;}

    public void update() {
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }
        if (y <= minY) {
            y = minY;
        }
        if (y >= maxY) {
            y = maxY;
        }
        if (x <= minX) {
            x = minX;
        }
        if (x >= maxX) {
            x = maxX;
        }
        if (x > minX) {
            x -= 5;
        }
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();

    }

    public Rect getHitbox() {
        return hitBox;
    }
}
