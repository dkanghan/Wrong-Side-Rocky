package com.example.wrongsiderocky;


//----------------------------------------------------------------------------
//LevelManager
//Class to initialize the level and manage them if player moves to next level
//-----------------------------------------------------------------------------

public class LevelManager {

    private int currentLevel;
    private final int maxLevel;

    //-----------------------------------------------------------------------
    //Constructor
    //Initializes the base level and sets the limit of max level
    //-----------------------------------------------------------------------
    public LevelManager() {
       currentLevel = 1;
       maxLevel = 3;
    }

    //-----------------------------------------------------------------------
    //nextLevel()
    //Checks the current level of the game and move to next if possible
    //-----------------------------------------------------------------------

    public void nextLevel(){
        if (currentLevel <= maxLevel) {
            currentLevel++;
        }
    }
    public int getCurrentLevel() {
        return currentLevel;
    }
}
