package com.example.casodistudio.game.gameviews.LViews;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.casodistudio.R;

import java.util.Random;

public class Floor {

    public Bitmap floor;
    int x,y ;

    public Floor(Resources res, int screenX, int screenY,short flagPlanet){
        //gestisci flagPlanet
        floor = BitmapFactory.decodeResource(res, R.drawable.ground_luna);
        x = 0;
        y = screenY - floor.getHeight();

    }




}
