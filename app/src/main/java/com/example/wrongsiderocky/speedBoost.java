package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

//-----------------------------------------------------------------------
//speedBoost
//Class to initialize the speedBoost Object
//-----------------------------------------------------------------------
public class speedBoost {
    private Bitmap bitmap;
    private int x,y;
    private final int maxY;
    private final int maxX;
    private final Rect hitBox;
    private final int frameWidth;
    private final int playerHeight;


    //-----------------------------------------------------------------------
    //Constructor
    //initializes the bitmap and variables for the class
    //initializes the hitbox for the class
    //initializes the base x and y coordinates of object
    //-----------------------------------------------------------------------
    public speedBoost(Context context, int screenX, int screenY, int y, int x, int playerheight){
        maxX = screenX;
        maxY = screenY;
        this.playerHeight = playerheight;

        bitmap = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.speedboost);
        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);

        this.y = y ;
        this.x = x;
        frameWidth = bitmap.getWidth();
        int frameHeight = bitmap.getHeight();
        hitBox = new Rect(x, y, frameWidth, frameHeight);

    }

    //------------------------------------------------------------------------------------
    // Update()
    // Moves the speedBoost object
    // Respawns the bitmap on the screen when object moves out of screen
    //------------------------------------------------------------------------------------
    public void update(int playerSpeed, int level){
        Random generator = new Random();
        x -= playerSpeed*10;

        //If object goes out of screen
        //reInitialize it
        //y axis based on level
        if(x < -frameWidth-10){
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
            }
        }

        //reinitialize hitbox
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }

    //getters and setters

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
