package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;


public class LevelManager {

    private int currentLevel;
    private int maxLevel;

    public LevelManager() {
       currentLevel = 1;
       maxLevel = 3;
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
