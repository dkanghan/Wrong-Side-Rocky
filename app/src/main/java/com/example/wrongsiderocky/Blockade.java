package com.example.wrongsiderocky;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import java.util.Random;

//-----------------------------------------------------------------------
//Blockade
//Class to initialize the blockade and pothole images
//-----------------------------------------------------------------------
public class Blockade {
    private Bitmap bitmap;
    private int x,y;
    private final int maxY;
    private final int maxX;
    private final Rect hitBox;
    private final int frameWidth;
    private final int playerHeight;
    private final Context context;

    //-----------------------------------------------------------------------
    //Constructor(context,screenX,screenY,y,x,playerheight)
    //initializes the bitmap and variables needed for the class
    //-----------------------------------------------------------------------
    public Blockade(Context context, int screenX, int screenY, int y,int x, int playerheight){
        maxX = screenX;
        maxY = screenY;
        this.context = context;
        this.playerHeight = playerheight;


        // Load blockade bitmap
        bitmap = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.blockade);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, false);

        this.y = y ;
        this.x = x;
        frameWidth = bitmap.getWidth();
        int frameHeight = bitmap.getHeight();
        hitBox = new Rect(x, y, frameWidth, frameHeight);
    }

    //------------------------------------------------------------------------------------
    // Update()
    // Moves the blockade
    // Keeps changing appearance once the bitmap is out of screen
    // Respawns the new bitmap on the screen based on level and lanes
    //------------------------------------------------------------------------------------
    public void update(int playerSpeed, int level){
        Random generator = new Random();
        x -= playerSpeed*10;

        // Change blockade type and position if it moves off-screen
        if(x < -frameWidth-10){
            int i = generator.nextInt(2);
            if(i == 0){
                // Load blockade bitmap
                bitmap = BitmapFactory.decodeResource
                        (context.getResources(), R.drawable.blockade);
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, false);
            } else {
                // Load pothole bitmap
                bitmap = BitmapFactory.decodeResource
                        (context.getResources(), R.drawable.pothole);
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/8, bitmap.getHeight()/8, false);
            }

            // Set blockade position based on level
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
                y = maxY / 2 - playerHeight / 2;
            }
        }
        }

        // Update the hitbox position
        hitBox.left = x;
        hitBox.top = y;
        hitBox.right = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }


    //Getters and Setters
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
