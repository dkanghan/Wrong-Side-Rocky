package com.example.wrongsiderocky;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;

//Game Activity
//initializes and starts the game
public class GameActivity extends Activity {

    private roadView gameView;

    //Creates a display and initializes roadView with the size of the screen.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        gameView = new roadView(this, size.x,size.y);
        setContentView(gameView);
    }

    //Method to pause the game

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    // Method to resume the game
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    //In case if player closes the game.
    //Method to close the game safely.
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }
}