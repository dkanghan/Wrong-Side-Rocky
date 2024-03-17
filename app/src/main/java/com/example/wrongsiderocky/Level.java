package com.example.wrongsiderocky;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.ArrayList;

public class Level {

    public SurfaceHolder ourHolder;
    public Paint paint;
    public Canvas canvas;

    public ArrayList<trafficCars> cars;
    public Bitmap roadbitmap;

    public Level(){

        paint = new Paint();
        canvas = new Canvas();
    }
    public void startLevel() {

    }



    public Bitmap getBitmap() {

        return null;
    }
}
