package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.Random;

//-----------------------------------------------------------------------
//roadObjects
//Class to initialize the roadObjects
//-----------------------------------------------------------------------
public class roadObjects {
    private Bitmap bitmap;
    private int x,y;
    private final int maxY;
    private final int maxX;
    private final int frameWidth;
    private final Context context;

    //-----------------------------------------------------------------------
    //Constructor
    //initializes the lamp and bitmap images for class
    //-----------------------------------------------------------------------
    public roadObjects(Context context, int screenX, int screenY, int y, int x){
        maxX = screenX;
        maxY = screenY;
        this.context = context;
        bitmap = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.lamp1);

        this.y = y ;
        this.x = x;
        frameWidth = bitmap.getWidth();
    }

    //------------------------------------------------------------------------------------
    // Update()
    // Moves the road objects with the speed of road
    // Keeps changing appearance once the bitmap is out of screen
    // Respawns the new bitmap on the screen based on road height
    //------------------------------------------------------------------------------------
    public void update(int playerSpeed, Bitmap road){
        Random generator = new Random();
        x -= playerSpeed*10;

        //respawns roadObject when out of screen
        if(x < frameWidth){
            y = maxY/2 - 7*road.getHeight()/8;
            x = generator.nextInt(maxX)+maxX;
            int i = generator.nextInt(5);
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
            }else {
                bitmap = BitmapFactory.decodeResource
                        (context.getResources(), R.drawable.lamp5);
            }
        }

    }

    //getters and setters
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

