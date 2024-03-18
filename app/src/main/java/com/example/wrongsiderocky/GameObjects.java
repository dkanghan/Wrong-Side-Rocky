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
    private Shield shield;
    private Rocky player;
    private Bitmap roadBitmap;

    private int screenX, screenY;
    private Road road;
    private int playerHeight;
    private int level;

    public GameObjects(Context context, int screenX, int screenY) {
        this.context = context;
        this.screenX = screenX;
        this.screenY = screenY;
        cars = new ArrayList<>();
        police = new ArrayList<>();



    }



    public void initializeObjects(int level){
        this.level = level;
        playerHeight = new Rocky(context, screenX, screenY, 1,1).getBitmap().getHeight();
        road = new Road(context,screenX,screenY,level,playerHeight);
        roadBitmap = road.getBitmap();
        switch (level){
            case 1:
                player = new Rocky(context, screenX, screenY,screenY/2 - roadBitmap.getHeight()/2 + playerHeight/2, screenY/2+roadBitmap.getHeight()/2 - 3*playerHeight/2);

                cars.add(new trafficCars(context, screenX, screenY,screenY/2 - 3*playerHeight/2));
                cars.add(new trafficCars(context, screenX, screenY,screenY/2 + playerHeight/2));
                break;
            case 2:
                player = new Rocky(context, screenX, screenY,(screenY/2) - (roadBitmap.getHeight()/2) + playerHeight/2, screenY/2 + roadBitmap.getHeight()/2 - 3*playerHeight/2);
                shield = new Shield(context,screenX,screenY,playerHeight);
                cars.add(new trafficCars(context, screenX, screenY,screenY/2 - playerHeight));
                cars.add(new trafficCars(context, screenX, screenY,screenY/2 + playerHeight/2));
                cars.add(new trafficCars(context, screenX, screenY,screenY/2 + playerHeight*3/2));
                police.add(new Police(context, screenX, screenY));
                break;
            case 3:
                player = new Rocky(context, screenX, screenY,screenY/2 - roadBitmap.getHeight()/2 + playerHeight/2, screenY/2+roadBitmap.getHeight()/2 - playerHeight/2);

//                cars.add(new trafficCars(context, screenX, screenY));
//                cars.add(new trafficCars(context, screenX, screenY));
//                cars.add(new trafficCars(context, screenX, screenY));
                police.add(new Police(context, screenX, screenY));
                break;
        }

    }

    public void updateObjects(){
        player.update();
        road.update(player.getSpeed());
        for(trafficCars car: cars){
            car.update(player.getSpeed(),context,screenY,0,playerHeight,level);
        }
        if (!police.isEmpty()){
            for (Police police1: police){
                police1.update(player, player.getSpeed(), (ArrayList<trafficCars>) cars);
            }
        }
        if(level ==2 ){
            shield.update(player.getSpeed());
        }


    }
    public Bitmap getRoadBitmap() {
        return roadBitmap;
    }
    public Road getRoad(){return road;}
    public Shield getShield(){return shield;}

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
