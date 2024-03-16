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
    private boolean boosting = false;
    private int maxY;
    private int minY;
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    public Rocky(Context context, int screenX, int screenY) {
        x=50;
        y=50;
        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocky);
        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
        minY = 1;
        maxY = screenY - bitmap.getHeight()+1;

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
        this.y = y;
    }
    public void setBoosting(){
        boosting = true;
    }
    public void stopBoosting(){
        boosting = false;
    }

    public void update() {
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }
        if (y < minY) {
            y = minY;
        }
        if (y > maxY) {
            y = maxY;
        }

    }

    public Rect getHitbox() {
        return hitBox;
    }
}
