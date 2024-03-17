package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.util.ArrayList;

public class Level1 extends Level {

    ArrayList<trafficCars> cars;
    private Bitmap roadbitmap;
    private int screenX;
    private int screenY;

    private Rocky player;
    public Level1(Context context, int screenX, int screenY){
        super();

        this.screenY = screenY;
        this.screenX = screenX;
        initalizeRoad();
        cars = new ArrayList<trafficCars>(2);
        cars.add(new trafficCars(context,screenX,screenY));
        cars.add(new trafficCars(context,screenX,screenY));
        roadbitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.road);
        player = new Rocky(context, screenX, screenY);


    }
    public void startLevel(int playerSpeed, Context context) {
        for(trafficCars car : cars ){
            car.update(playerSpeed,context,screenY,0);
        }
        // Initialize level-specific elements and start gameplay
    }

    private void initalizeRoad(){
        paint.setColor(Color.argb(255, 255, 255, 255));
        canvas.drawBitmap(
                roadbitmap,
                0,
                0,
                paint
        );
    }


    public Bitmap getBitmap() {
        return roadbitmap;
    }
}