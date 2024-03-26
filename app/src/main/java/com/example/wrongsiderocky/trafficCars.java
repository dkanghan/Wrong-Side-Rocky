package com.example.wrongsiderocky;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

//-----------------------------------------------------------------------
//Police
//Class to initialize the trafficCars Object
//-----------------------------------------------------------------------
public class trafficCars {
    private Bitmap bitmap;
    private Bitmap[] bitmapAm;


    private int x,y;
    private int speed;
    private final int maxX;
    private final int minX;
    private final Rect hitBox;
    private int frameWidth, frameHeight;
    private int currentFrame;
    private long lastFrameTime;
    private boolean ambulance;

    //-----------------------------------------------------------------------
    //Constructor
    //initializes the bitmap and variables for the class
    //initializes bitmap to either be a normal car or an ambulance
    //initializes the hitbox for the class
    //initializes the base x and y coordinates of police object
    //-----------------------------------------------------------------------
    public trafficCars(Context context, int screenX, int y){
        Random generator = new Random();
        int cars = generator.nextInt(4);
        if(cars == 0) {
            bitmap = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.car);
        }
        else if (cars == 1) {
            bitmap = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.truck);

        }
        else if (cars == 2){

            bitmapAm = new Bitmap[3];
            bitmapAm[0] = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.ambulance1);
            bitmapAm[1] = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.ambulance2);
            bitmapAm[2] = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.ambulance3);


            frameWidth = bitmapAm[0].getWidth();
            frameHeight = bitmapAm[0].getHeight();
            lastFrameTime = System.currentTimeMillis();
            ambulance = true;

        }
        else {
            bitmap = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.mini_truck);
        }
        maxX = screenX;
        minX = 0;
        this.y = y;
        if(ambulance){
            hitBox = new Rect(x, y, frameWidth, frameHeight);
        }else {
            hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
        }
        speed = 1;
        speed = generator.nextInt(6)+10;
        x = screenX;


    }

    //------------------------------------------------------------------------------------
    // Update()
    // Moves the traffic cars
    // Change the bitmap
    // Respawns the new bitmap on the screen when trafficCar object moves out of screen
    //------------------------------------------------------------------------------------

    public void update(int playerSpeed, Context context, int maxY,  int playerHeight,int level){
        x -= playerSpeed*3;
        x -= speed;

        if(ambulance){
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
            //reInitialize it based on level
            if(x < minX-frameWidth){
                Random generator = new Random();
                speed = generator.nextInt(10)+10;
                x = maxX;

                if(level == 1){
                    int sety = generator.nextInt(2);
                    if(sety == 0){
                        y = maxY/2 - 3*playerHeight/2;
                    }else{
                        y = maxY/2 + playerHeight/2;
                    }

                } else if (level == 3) {
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
            hitBox.right = x + frameWidth;
            hitBox.bottom = y + frameHeight;
        }
        else {
            //If object goes out of screen
            //reInitialize it based on level
            if(x < minX-bitmap.getWidth()){
                Random generator = new Random();
                int cars = generator.nextInt(3);
                if(cars == 0) {
                    bitmap = BitmapFactory.decodeResource
                            (context.getResources(), R.drawable.car);
                }
                else if (cars == 1) {
                    bitmap = BitmapFactory.decodeResource
                            (context.getResources(), R.drawable.truck);

                }
                else {
                    bitmap = BitmapFactory.decodeResource
                            (context.getResources(), R.drawable.mini_truck);
                }
                speed = generator.nextInt(10)+10;
                x = maxX;
                if(level == 1){
                    int sety = generator.nextInt(2);
                    if(sety == 0){
                        y = maxY/2 - 3*playerHeight/2;
                    }else{
                        y = maxY/2 + playerHeight/2;
                    }

                } else if (level == 3) {
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


    }

    //getters and setters
    public Bitmap getBitmap(){
        if (ambulance){
            return bitmapAm[currentFrame];
        }
        return bitmap;
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

    public void setSpeed(int x) {
        this.speed = x;
    }
    public int getSpeed(){return speed;}


}
