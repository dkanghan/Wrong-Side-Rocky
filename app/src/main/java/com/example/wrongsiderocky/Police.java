package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import java.util.ArrayList;
import java.util.Random;

//-----------------------------------------------------------------------
//Police
//Class to initialize the police Object
//-----------------------------------------------------------------------

public class Police {
    private final Bitmap[] bitmap;
    private int x,y;
    private int speed;
    private final int maxY;
    private final int maxX;
    private final int minX;
    private final Rect hitBox;
    private final int frameWidth;
    private final int frameHeight;
    private int currentFrame;
    private long lastFrameTime;

    //-----------------------------------------------------------------------
    //Constructor
    //initializes the bitmap and variables for the class
    //initializes the hitbox for the class
    //initializes the base x and y coordinates of police object
    //-----------------------------------------------------------------------
    public Police(Context context, int screenX, int screenY){
        Random generator = new Random();
        bitmap = new Bitmap[3];
        bitmap[0] = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.police1);
        bitmap[1] = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.police2);
        bitmap[2] = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.police3);

        frameWidth = bitmap[0].getWidth();
        frameHeight = bitmap[0].getHeight();
        lastFrameTime = System.currentTimeMillis();

        maxX = screenX;
        maxY = screenY;
        System.out.println(screenY);
        minX = 0;
        hitBox = new Rect(x, y, frameWidth, frameHeight);

        speed = generator.nextInt(6)+10;
        x = screenX*3;
        y = screenY/2 ;
    }


    //------------------------------------------------------------------------------------
    // Update()
    // Moves the police object
    // If player is above police object start increasing y coordinates
    // If player is below police object start decreasing y coordinates
    // Respawns the new bitmap on the screen when police object moves out of screen
    // Checks if there is a collision between police and trafficCars, if there is slow down and try to change direction until clear
    //------------------------------------------------------------------------------------

    public void update(Rocky player, int playerSpeed, ArrayList<trafficCars> cars ){

        for (trafficCars car : cars) {
            Rect carHitbox = car.getHitbox();

            //checks for collision between cars and police objects
            if (Rect.intersects(hitBox, carHitbox)) {
                Rect policeHitbox = getHitbox();

                if (policeHitbox.top < carHitbox.top && policeHitbox.bottom < carHitbox.bottom)  {
                    //intersects from top
                    //move car up and decrease speed
                    x -= playerSpeed/2;
                    x -= speed/2;
                    y -= playerSpeed/6;
                    y -= speed/6;
                } else if (policeHitbox.top > carHitbox.top && policeHitbox.bottom > carHitbox.bottom ) {
                    //intersect from below
                    //move car down and decrease speed
                    x -= playerSpeed/2;
                    x -= speed/2;
                    y += playerSpeed/6;
                    y += speed/6;

                }


                else if(policeHitbox.right < carHitbox.right && policeHitbox.left < carHitbox.left){
                    //intersects from front
                    //decrease speed
                    //move car in y axis only until clear
                    setSpeed(car.getSpeed()-1);
                    if (player.getY() < y && player.getX() < x) {
                        y -= playerSpeed/6;
                        y -= speed/6;
                    } else if (player.getY() > y && player.getX() < x) {
                        y += playerSpeed/6;
                        y += speed/6;
                    }
                } else if(policeHitbox.right > carHitbox.right && policeHitbox.left > carHitbox.left){
                    //intersects from behind
                    //decrease speed
                    //move car in y axis only until clear
                    car.setSpeed(getSpeed()-1);
                    if (player.getY() < y && player.getX() < x) {
                        y -= playerSpeed/6;
                        y -= speed/6;
                    } else if (player.getY() > y && player.getX() < x) {
                        y += playerSpeed/6;
                        y += speed/6;
                    }
                }
            }
            else{
                //move car according to player
                x -= playerSpeed*3;
                x -= speed/2;
                if (player.getY() < y && player.getX() < x) {
                    y -= speed / 8;
                } else if (player.getY() > y && player.getX() < x) {
                    y += speed / 8;
                }

            }
        }


        long currentTime = System.currentTimeMillis();
        long frameDuration = 100;

        //manage bitmap frame
        //increase frame
        if (currentTime - lastFrameTime > frameDuration) {
            int frameCount = 3;
            currentFrame = (currentFrame + 1) % frameCount;
            lastFrameTime = currentTime;
        }

        //If object goes out of screen
        //reInitialize it
        if(x < minX-frameWidth){
            Random generator = new Random();
            speed = generator.nextInt(5)+10;
            x = maxX*3;
            y = maxY/2 ;
        }

        //reinitialize hitbox
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + frameWidth;
        hitBox.bottom = y + frameHeight;


    }

    //getters and setters
    public Bitmap getBitmap(){
        return bitmap[currentFrame];
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
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int x) {
        speed=x;
    }
    public void setX(int x) {
        this.x = x;
    }

}
