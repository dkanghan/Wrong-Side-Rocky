package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class GameObjects {
    private final Context context;

    private final ArrayList<trafficCars> cars;
    private final ArrayList<Police> police;
    private Blockade blockade;
    private Shield shield;
    private Rocky player;
    private Bitmap roadBitmap;
    private Bitmap pauseBitmap;

    private final int screenX;
    private final int screenY;
    private Road road;
    private int playerHeight;
    private int level;
    private speedBoost speedBoost;
    private final ArrayList<roadObjects> roadObjects;

    //-----------------------------------------------------------------------
    //GameObjects
    //Class to initialize Game objects
    //Game Objects initializes objects for the game based on level.
    // Initializes car,police,road,roadObjects,pause button for the game
    //-----------------------------------------------------------------------
    public GameObjects(Context context, int screenX, int screenY) {
        this.context = context;
        this.screenX = screenX;
        this.screenY = screenY;
        cars = new ArrayList<>();
        police = new ArrayList<>();
        roadObjects = new ArrayList<>();
        pauseBitmap = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.pause);
        pauseBitmap = Bitmap.createScaledBitmap(pauseBitmap, 48,48, false);
    }

    //---------------------------------------------------
    //initializeObjects(int)
    //Takes parameter level
    //calculates player height
    //creates necessary objects for level
    //Level 1:
    //Initializes Player Rocky
    //Initializes two car objects
    //Level 2:
    //Initializes Player Rocky
    //Initializes three car objects
    //Initializes blockade Object
    //Initialize PowerUp Objects (shield, speedBoost)
    //Level 3:
    //Initializes Player Rocky
    //Initializes three car objects
    //Initializes police objects
    //Initializes blockade Object
    //Initialize PowerUp Objects (shield, speedBoost)
    //---------------------------------------------------
    public void initializeObjects(int level){
        //clear array list to initialize new objects
        cars.clear();
        police.clear();
        roadObjects.clear();
        this.level = level;
        //calculate player height as other objects will be scaled on this
        playerHeight = new Rocky(context, screenX, 1,1).getBitmap().getHeight();
        road = new Road(context,screenX,screenY,level,playerHeight);
        roadBitmap = road.getBitmap();

        //Initialize Objects based on the level
        switch (level){
            case 1:
                player = new Rocky(context, screenX, screenY/2 - roadBitmap.getHeight()/2 + playerHeight/2, screenY/2+roadBitmap.getHeight()/2 - 3*playerHeight/2);
                cars.add(new trafficCars(context, screenX*3/2, screenY/2 - 3*playerHeight/2));
                cars.add(new trafficCars(context, screenX, screenY/2 + playerHeight/2));
                roadObjects.add(new roadObjects(context,screenX,screenY,screenY/2-7*roadBitmap.getHeight()/8,screenX + screenX/3));
                roadObjects.add(new roadObjects(context,screenX,screenY,screenY/2-7*roadBitmap.getHeight()/8,screenX + screenX/2));
                roadObjects.add(new roadObjects(context,screenX,screenY,screenY/2-7*roadBitmap.getHeight()/8,screenX + screenX/4));
                roadObjects.add(new roadObjects(context,screenX,screenY,screenY/2-7*roadBitmap.getHeight()/8,screenX));

                break;
            case 2:
                player = new Rocky(context, screenX, screenY/2 - roadBitmap.getHeight()/2 + playerHeight/2, screenY/2+roadBitmap.getHeight()/2 - playerHeight);
                shield = new Shield(context,screenX,screenY,playerHeight);
                cars.add(new trafficCars(context, screenX*2, screenY/2 - 2*playerHeight));
                cars.add(new trafficCars(context, screenX, screenY/2 - playerHeight/2));
                cars.add(new trafficCars(context, screenX*3/2, screenY/2 + playerHeight));
                blockade = new Blockade(context,screenX,screenY,screenY/2 - playerHeight,screenX,playerHeight);
                speedBoost = new speedBoost(context,screenX,screenY,screenY/2 + 2*playerHeight,screenX,playerHeight);
                roadObjects.add(new roadObjects(context,screenX,screenY,screenY/2-7*roadBitmap.getHeight()/8,screenX + screenX/3));
                roadObjects.add(new roadObjects(context,screenX,screenY,screenY/2-7*roadBitmap.getHeight()/8,screenX + screenX/2));
                roadObjects.add(new roadObjects(context,screenX,screenY,screenY/2-7*roadBitmap.getHeight()/8,screenX + screenX/4));
                roadObjects.add(new roadObjects(context,screenX,screenY,screenY/2-7*roadBitmap.getHeight()/8,screenX));
                break;
            case 3:
                player = new Rocky(context, screenX, (screenY/2) - (roadBitmap.getHeight()/2) + playerHeight/2, screenY/2 + roadBitmap.getHeight()/2 - 3*playerHeight/2);
                shield = new Shield(context,screenX,screenY,playerHeight);
                cars.add(new trafficCars(context, screenX, screenY/2 - playerHeight));
                cars.add(new trafficCars(context, screenX, screenY/2 + playerHeight/2));
                cars.add(new trafficCars(context, screenX, screenY/2 + playerHeight*3/2));
                blockade = new Blockade(context,screenX,screenY,screenY/2 - playerHeight,screenX*2,playerHeight);
                police.add(new Police(context, screenX, screenY));
                speedBoost = new speedBoost(context,screenX,screenY,screenY/2 + playerHeight*3/2,screenX,playerHeight);

                break;
        }

    }

    //----------------------------------------------------------------------
    //updateObjects()
    //update player objects and other objects initialized according to level
    //----------------------------------------------------------------------

    public void updateObjects(){
        player.update();
        road.update(player.getSpeed());
        if(level !=1) {
            blockade.update(player.getSpeed(), level);
            speedBoost.update(player.getSpeed(), level);
        }
        for(trafficCars car: cars){
            car.update(player.getSpeed(),context,screenY,playerHeight,level);
        }
        if (!police.isEmpty()){
            for (Police police1: police){
                police1.update(player, player.getSpeed(), cars);
            }
        }
        for(roadObjects roadObjects : this.roadObjects){
            roadObjects.update(getPlayer().getSpeed(), getRoadBitmap());
        }
        if(level>1 ){
            shield.update(player.getSpeed(), playerHeight);
        }
    }

    //Getters and Setters
    public Bitmap getPauseBitmap() {
        return pauseBitmap;
    }
    public Bitmap getRoadBitmap() {
        return roadBitmap;
    }
    public speedBoost getSpeedBoost(){
        return speedBoost;
    }
    public Road getRoad(){return road;}
    public Shield getShield(){return shield;}
    public Blockade getBlockade(){
        return blockade;
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


    public ArrayList<roadObjects> getLamp() {
        return roadObjects;
    }
}
