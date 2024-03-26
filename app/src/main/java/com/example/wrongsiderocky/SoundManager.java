package com.example.wrongsiderocky;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import java.io.IOException;

//-----------------------------------------------------------------------
//SoundManager
//Class to manage sound effects and background sound
//-----------------------------------------------------------------------
public class SoundManager {
    private SoundPool soundPool;
    private MediaPlayer mediaPlayer;
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
    Context context;


    //-----------------------------------------------------------------------
    //loadSound()
    //Loads sound files for the game
    //-----------------------------------------------------------------------
    public void loadSound(Context context){

        this.context = context;

        mediaPlayer = new MediaPlayer();

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.d("OnLoadComplete", "onLoadComplete: "+ sampleId);
            }
        });
        try{
            //Create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;
            //create our fx
            descriptor = assetManager.openFd("level1.ogg");
            levelone = soundPool.load(descriptor, 1);
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

            descriptor = assetManager.openFd("leveltwo.ogg");
            leveltwo = soundPool.load(descriptor, 2);

            descriptor = assetManager.openFd("level3.ogg");
            levelthree = soundPool.load(descriptor, 0);

        }catch(IOException e){
            //Print an error message to the console
            Log.e("error", "failed to load sound files");
        }
    }

    //-----------------------------------------------------------------------
    //playSound()
    //play sound Effects for the game
    //-----------------------------------------------------------------------
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
                soundPool.play(police,(float)0.5,(float)0.5,0,0,1);
                break;
            case "ambulance":
                soundPool.play(ambulance,(float)0.5,(float)0.5,0,0,1);
                break;
            case "hit":
                soundPool.play(hit,1,1,0,0,1);
                break;
            case "level1":
                soundPool.play(levelone,1,1,0,0,1);
                break;
            case "level2":
                soundPool.play(leveltwo,1,1,0,0,1);
                break;
            case "level3":
                soundPool.play(levelthree,1,1,0,0,1);
                break;
        }
    }

    //-----------------------------------------------------------------------
    //stopSound()
    //stop sound Effects for the game
    //-----------------------------------------------------------------------
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
            case "level1":
                soundPool.stop(levelone);
                break;
            case "level2":
                soundPool.stop(leveltwo);
                break;
            case "level3":
                soundPool.stop(levelthree);
                break;
        }
    }

    //-----------------------------------------------------------------------
    //stopAll()
    //stop all sound Effects currently playing in the game
    //-----------------------------------------------------------------------
    public void stopAll(){
        soundPool.autoPause();
    }

    //-----------------------------------------------------------------------
    //playBgMusic()
    //plays Background Music for the game based on level
    //-----------------------------------------------------------------------
    public void playBgMusic(int level) throws IOException {
        AssetManager assetManager = this.context.getAssets();
        AssetFileDescriptor descriptor;
        mediaPlayer = new MediaPlayer();
        descriptor = assetManager.openFd("level"+level+".ogg");
        mediaPlayer.setDataSource(descriptor);
        mediaPlayer.prepare();
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    //-----------------------------------------------------------------------
    //stopBGMusic()
    //stops Background music in game
    //-----------------------------------------------------------------------
    public void stopBGMusic(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
