package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameObjects {
    private Context context;

    private ArrayList<trafficCars> cars;
    private ArrayList<Police> police;
    private Rocky player;
    private Bitmap roadBitmap;

    private int screenX, screenY;

    public GameObjects(Context context, int screenX, int screenY) {
        this.context = context;
        this.screenX = screenX;
        this.screenY = screenY;
        cars = new ArrayList<>();
        police = new ArrayList<>();
        player = new Rocky(context, screenX, screenY);

    }



    public void initializeObjects(int level){
        switch (level){
            case 1:
                roadBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.road);
                roadBitmap  = Bitmap.createScaledBitmap(roadBitmap, screenX*2, player.getBitmap().getHeight() * 3, false);
                cars.add(new trafficCars(context, screenX, screenY));
                cars.add(new trafficCars(context, screenX, screenY));
                break;
            case 2:
                roadBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.road);
                cars.add(new trafficCars(context, screenX, screenY));
                cars.add(new trafficCars(context, screenX, screenY));
                cars.add(new trafficCars(context, screenX, screenY));
                police.add(new Police(context, screenX, screenY));
                break;
            case 3:
                cars.add(new trafficCars(context, screenX, screenY));
                cars.add(new trafficCars(context, screenX, screenY));
                cars.add(new trafficCars(context, screenX, screenY));
                police.add(new Police(context, screenX, screenY));
                police.add(new Police(context, screenX, screenY));
                break;
        }

    }

    public void updateObjects(){
        player.update();
        for(trafficCars car: cars){
            car.update(player.getSpeed(),context,screenY,0);
        }
        if (!police.isEmpty()){
            for (Police police1: police){
                police1.update(player, player.getSpeed(), (ArrayList<trafficCars>) cars);
            }
        }
    }
    public Bitmap getRoadBitmap() {
        return roadBitmap;
    }

    public ArrayList<trafficCars> getCars() {
        return cars;
    }

    public ArrayList<Police> getPolice() {
        return police;
    }

    public Rocky getPlayer() {
        return player;
    }






}
