package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;


//-----------------------------------------------------------------------
//Police
//Class to initialize the player Rocky
//-----------------------------------------------------------------------
public class Rocky {
    private final Bitmap bitmap;
    private int x,y;
    private int speed;
    private final Rect hitBox;
    private final int maxY;
    private final int minY;
    private final int maxX;
    private final int minX;

    //-----------------------------------------------------------------------
    //Constructor
    //initializes the bitmap and variables for the class
    //initializes the hitbox for the class
    //initializes the base x and y coordinates of police object
    //initializes max and min Y allowed for player based on level
    //-----------------------------------------------------------------------
    public Rocky(Context context, int screenX, int minY, int maxY) {
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

    //------------------------------------------------------------------------------------
    // Update()
    // Keeps speed of player in check
    // Keeps check if player don't move out of the screen
    //------------------------------------------------------------------------------------
    public void update() {
        int MAX_SPEED = 20;
        //checks to maintain speed
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        int MIN_SPEED = 1;
        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }
        //checks to keep player on the road
        if (y <= minY) {
            y = minY;
        }
        if (y >= maxY) {
            y = maxY;
        }

        //check to keep player on the initial x coordinate
        if (x <= minX) {
            x = minX;
        }
        //check if player goes out of allowed limit
        if (x >= maxX) {
            x = maxX;
        }
        //If player is not in initial x coordinate try, decrease x coordinate
        if (x > minX) {
            x -= 5;
        }

        //reinitialize hitbox
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();

    }

    //------------------------------------------------------------------------------------
    //startBoosting()
    //if player is boosting increase speed
    //------------------------------------------------------------------------------------
    public void startBoosting(){
        speed += 1;
    }

    //------------------------------------------------------------------------------------
    //startBoosting()
    //if player stopped boosting decrease speed
    //------------------------------------------------------------------------------------
    public void stopBoosting(){
        speed -= 1;
    }

    //getters and setters
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
    public Rect getHitbox() {
        return hitBox;
    }
}
