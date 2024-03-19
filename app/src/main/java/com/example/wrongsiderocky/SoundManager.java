package com.example.wrongsiderocky;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import java.io.IOException;

public class SoundManager {
    private SoundPool soundPool;
    int next_level = -1;
    int coin_pickup = -1;
    int explode = -1;
    int extra_life = -1;
    int police = -1;
    int ambulance = -1;
    int hit = -1;
    int levelone = -1;
    int leveltwo = -1;
    int levelthree = -1;


    public void loadSound(Context context){
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        try{
            //Create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;
            //create our fx

            descriptor = assetManager.openFd("next_level.ogg");
            next_level = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("coin_pickup.ogg");
            coin_pickup = soundPool.load(descriptor, 0);


            descriptor = assetManager.openFd("explode.ogg");
            explode = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("extra_life.ogg");
            extra_life = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("police.ogg");
            police = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("ambulance.ogg");
            ambulance = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("hit.ogg");
            hit = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("level.ogg");
            levelone = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("leveltwo.ogg");
            leveltwo = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("levelthree.ogg");
            levelthree = soundPool.load(descriptor, 0);

        }catch(IOException e){
            //Print an error message to the console
            Log.e("error", "failed to load sound files");
        }
    }
    public void playSound(String sound){
        switch (sound){

            case "next_level":
                soundPool.play(next_level, 1, 1, 0, 0, 1);
                break;
            case "coin_pickup":
                soundPool.play(coin_pickup, 1, 1, 0, 0, 1);
                break;

            case "explode":
                soundPool.play(explode, 1, 1, 0, 0, 1);
                break;
            case "extra_life":
                soundPool.play(extra_life, 1, 1, 0, 0, 1);
                break;
            case "police":
                soundPool.play(police,1,1,0,0,1);
                break;
            case "ambulance":
                soundPool.play(ambulance,1,1,0,0,1);
                break;
            case "hit":
                soundPool.play(hit,1,1,0,0,1);
                break;
            case "levelone":
                soundPool.play(levelone,1,1,0,0,1);
                break;
            case "leveltwo":
                soundPool.play(leveltwo,1,1,0,0,1);
                break;
            case "levelthree":
                soundPool.play(levelthree,1,1,0,0,1);
                break;
        }
    }
    public void stop(String sound){
        switch (sound){

            case "next_level":
                soundPool.stop(next_level);
                break;
            case "coin_pickup":
                soundPool.stop(coin_pickup);
                break;

            case "explode":
                soundPool.stop(explode);
                break;
            case "extra_life":
                soundPool.stop(extra_life);
                break;
            case "police":
                soundPool.stop(police);
                break;
            case "ambulance":
                soundPool.stop(ambulance);
                break;
            case "hit":
                soundPool.stop(hit);
                break;
            case "levelone":
                soundPool.stop(levelone);
                break;
            case "leveltwo":
                soundPool.stop(leveltwo);
                break;
            case "levelthree":
                soundPool.stop(levelthree);
                break;
        }
    }
}
