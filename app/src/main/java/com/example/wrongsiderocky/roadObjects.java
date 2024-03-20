package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class roadObjects {
    private Bitmap bitmap;
    private int x,y;
    private final int maxY;
    private final int maxX;
    private final Rect hitBox;
    private int frameWidth, frameHeight;
    private int playerHeight;
    private int level;
    private boolean visible = true;
    private Context context;

    public roadObjects(Context context, int screenX, int screenY, int y, int x, int playerheight){
        maxX = screenX;
        maxY = screenY;
        this.playerHeight = playerheight;
        this.context = context;
        bitmap = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.lamp1);
//        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, false);

        this.y = y ;
        this.x = x;
        frameWidth = bitmap.getWidth();
        frameHeight = bitmap.getHeight();
        hitBox = new Rect(x, y, frameWidth, frameHeight);
    }
    public void update(int playerSpeed, Bitmap road){
        Random generator = new Random();
        x -= playerSpeed*10;
        if(x < frameWidth){
            y = maxY/2 - 7*road.getHeight()/8;
            x = generator.nextInt(maxX)+maxX;
            int i = generator.nextInt(6);
            if(i==0){
                bitmap = BitmapFactory.decodeResource
                        (context.getResources(), R.drawable.lamp1);
            }else if (i ==1){
                bitmap = BitmapFactory.decodeResource
                        (context.getResources(), R.drawable.lamp2);
            }else if (i ==2){
                bitmap = BitmapFactory.decodeResource
                        (context.getResources(), R.drawable.lamp3);
            }else if (i ==3){
                bitmap = BitmapFactory.decodeResource
                        (context.getResources(), R.drawable.lamp4);
            }else if (i ==4){
                bitmap = BitmapFactory.decodeResource
                        (context.getResources(), R.drawable.lamp5);
            }else if (i ==5) {
                y = maxY/2 - 3*road.getHeight()/4;
                bitmap = BitmapFactory.decodeResource
                        (context.getResources(), R.drawable.extinguisher);
                bitmap = Bitmap.createScaledBitmap(bitmap, 60, 150, false);
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

