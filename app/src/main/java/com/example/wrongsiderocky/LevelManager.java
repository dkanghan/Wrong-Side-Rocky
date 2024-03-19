package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;


public class LevelManager {

    private int currentLevel;
    private int maxLevel;
    private SoundManager sm;

    public LevelManager() {
       currentLevel = 1;
       maxLevel = 3;
       sm = new SoundManager();
    }

    public void playLevelSound(){
        if (currentLevel == 1){
            sm.playSound("level1");
        }
        else if (currentLevel == 2){
            sm.playSound("level2");
        }
        else if (currentLevel == 3){
            sm.playSound("level3");
        }
    }
    public int getCurrentLevel() {
        return currentLevel;
    }

    public void nextLevel(){
        if (currentLevel <= maxLevel) {
            currentLevel++;
        }
    }
}
