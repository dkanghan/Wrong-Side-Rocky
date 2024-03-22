package com.example.wrongsiderocky;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs;
        prefs = getSharedPreferences("HiScores", MODE_PRIVATE);


        final Button buttonPlay = findViewById(R.id.buttonPlay);
        final TextView textLongestDistance = findViewById(R.id.texthighScore);
        long LongestDistance = prefs.getLong("LongestDistance", 1000);
        textLongestDistance.setText("Longest Distance :" + LongestDistance + "m");
        // Listen for clicks
        buttonPlay.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }
}
