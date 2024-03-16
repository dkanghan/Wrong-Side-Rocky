package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class Level1 extends Level {

    ArrayList<trafficCars> cars;
    private Bitmap roadbitmap;
    private int screenX;
    private int screenY;
    public Level1(Context context, int screenX, int screenY){
        super();

        this.screenY = screenY;
        this.screenX = screenX;
        cars = new ArrayList<trafficCars>(2);
        cars.add(new trafficCars(context,screenX,screenY));
        cars.add(new trafficCars(context,screenX,screenY));
        roadbitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.road);
    }
    public void startLevel(int playerSpeed, Context context) {
        for(trafficCars car : cars ){
            car.update(playerSpeed,context,screenY,0);
        }
        // Initialize level-specific elements and start gameplay
    }
    public Bitmap getBitmap() {
        return roadbitmap;
    }
}