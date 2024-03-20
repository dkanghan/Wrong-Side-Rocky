package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Blockade {
    private Bitmap bitmap;
    private int x,y;
    private final int maxY;
    private final int maxX;
    private final Rect hitBox;
    private int frameWidth, frameHeight;
    private int playerHeight;
    private int level;
    private boolean visible = true;

    public Blockade(Context context, int screenX, int screenY, int y,int x, int playerheight){
        maxX = screenX;
        maxY = screenY;
        this.playerHeight = playerheight;

        bitmap = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.blockade);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, false);

        this.y = y ;
        this.x = x;
        frameWidth = bitmap.getWidth();
        frameHeight = bitmap.getHeight();
        hitBox = new Rect(x, y, frameWidth, frameHeight);
    }
    public void update(int playerSpeed, int level){
        Random generator = new Random();
        x -= playerSpeed*10;

        if(x < frameWidth){
        if (level == 3) {
            x = maxX*generator.nextInt(3);
            int sety = generator.nextInt(4);
            if(sety == 0){
                y = maxY/2 - playerHeight;
            } else if (sety ==1) {
                y = maxY/2 + playerHeight/2;
            } else if (sety ==2) {
                y = maxY/2 + 3*playerHeight/2;
            } else  {
                y =  maxY/2 - 5*playerHeight/2;
            }
            this.visible = true;

        } else if (level == 2){
            x = maxX*generator.nextInt(5);
            int sety = generator.nextInt(3);
            if(sety == 0){
                y = maxY/2 - 2*playerHeight;
            } else if (sety ==1) {
                y = maxY/2 + playerHeight/2;
            } else {
                y = maxY/2 - playerHeight/2;
            }
            this.visible = true;
        }
        }

        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
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
    public Bitmap getBitmap() {
        return bitmap;
    }

}
