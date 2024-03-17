package com.example.wrongsiderocky;

import android.content.Context;
import android.graphics.Bitmap;

public class LevelManager {
    private int currentLevel;
    private final Level[] levels;

    public LevelManager(Context context, int screenX, int screenY) {
        // Initialize levels
        levels = new Level[] {
                new Level(),
                new Level(),
                new Level()
                // Add more levels as needed
        };
        currentLevel = 0; // Start from the first level
    }

    public void startCurrentLevel() {
        levels[currentLevel].startLevel();
    }
    public void goToNextLevel() {
        currentLevel++;
        if (currentLevel < levels.length) {
            startCurrentLevel();
        }
    }

    public Level getCurrentLevel() {
        return levels[currentLevel];
    }
}


