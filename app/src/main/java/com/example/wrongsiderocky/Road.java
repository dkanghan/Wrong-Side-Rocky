package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//-----------------------------------------------------------------------
//Road
//Class to initialize the road
// -----------------------------------------------------------------------
public class Road {
    private Bitmap bitmap;
    private int x;
    private final int y;
    private final int minX;
    private final int frameWidth;

    //-----------------------------------------------------------------------
    //Road(context,screenX,screenY,level,playerheight)
    //initializes the bitmap and variables needed for the class
    //initialize bitmap based on player height,screen width and number of lanes
    //-----------------------------------------------------------------------
    public Road(Context context, int screenX, int screenY, int level, int playerheight) {

        x = 0;
        minX = 0;
        if (level == 1) {
            bitmap = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.road1);
            bitmap = Bitmap.createScaledBitmap(bitmap, screenX * 2, playerheight * 4, false);
        }
        else if(level == 2){
                bitmap = BitmapFactory.decodeResource
                        (context.getResources(), R.drawable.road2);
                bitmap = Bitmap.createScaledBitmap(bitmap, screenX*2, playerheight * 5, false);
        }
        else if(level == 3){
            bitmap = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.road3);
            bitmap = Bitmap.createScaledBitmap(bitmap, screenX*2, playerheight * 7, false);
        }
        y = screenY/2 - bitmap.getHeight()/2 ;
        frameWidth = bitmap.getWidth();
    }

    //------------------------------------------------------------------------------------
    // Update()
    // Moves the road
    // Keeps the road moving
    //------------------------------------------------------------------------------------
    public void update(int playerSpeed){
        x -= playerSpeed*10;
        if(x < minX-frameWidth/2){
            x = 0;
        }

    }

    //getters
    public Bitmap getBitmap() {
        return bitmap;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

}
