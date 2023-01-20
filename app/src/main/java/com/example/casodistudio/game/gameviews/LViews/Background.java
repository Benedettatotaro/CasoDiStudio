package com.example.casodistudio.game.gameviews.LViews;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.casodistudio.R;

public class Background {

    public int screenX,screenY;
    public Bitmap background;
    public int x=0,y=0;

    public Background(Resources res,int screenX, int screenY,short flagPlanet){
        if(flagPlanet==0) {
            background = BitmapFactory.decodeResource(res, R.drawable.moon_background_grosso);
            background = Bitmap.createScaledBitmap(background, screenX*5, screenY, false);
        }else{
            background = BitmapFactory.decodeResource(res, R.drawable.background_mars);
            background = Bitmap.createScaledBitmap(background, screenX*5, screenY, false);
        }

    }





}
