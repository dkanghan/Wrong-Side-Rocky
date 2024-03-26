package com.example.wrongsiderocky;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

//-----------------------------------------------------------------------
//MainActivity
//Handles main activity
//get the high score and shows on the main screen
//-----------------------------------------------------------------------
public class MainActivity extends Activity implements View.OnClickListener {
    private EditText playerNameEditText;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs;
        prefs = getSharedPreferences("HiScores", MODE_PRIVATE);

        //get high score and load
        final TextView textLongestDistance = findViewById(R.id.texthighScore);
        long LongestDistance = prefs.getLong("LongestDistance", 1000);
        textLongestDistance.setText("Longest Distance :" + LongestDistance + "m");

        playerNameEditText = findViewById(R.id.editTextPlayerName);

        //set onclick listener for play button
        final Button buttonPlay = findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);

        //set road and car image
        //move car
        ImageButton movingCar = findViewById(R.id.movingCar);

        Animation carAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1f,
                Animation.RELATIVE_TO_PARENT, 1f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f
        );
        carAnimation.setDuration(2000);
        carAnimation.setRepeatCount(Animation.INFINITE);
        movingCar.startAnimation(carAnimation);
    }

    //-----------------------------------------------------------------------
    //onClick()
    //handles player clicks
    //if player tries to play the game without entering name, prompt player
    //start the game activity
    //-----------------------------------------------------------------------
    @Override
    public void onClick(View v) {

        String playerName = playerNameEditText.getText().toString();

        if (playerName.isEmpty()) {
            //if player name is empty, display a message or handle the scenario as needed
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        } else {
            //if player name is not empty, store it in SharedPreferences and start the game activity
            SharedPreferences prefs = getSharedPreferences("HiScores", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("PlayerName", playerName);
            editor.apply();

            //start the game activity
            Intent i = new Intent(this, GameActivity.class);
            startActivity(i);
            finish();
        }
    }

    //-----------------------------------------------------------------------
    //onKeyDown()
    //if player abruptly closes the game, safely finish the activity
    //-----------------------------------------------------------------------
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }
}