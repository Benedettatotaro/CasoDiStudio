package com.example.casodistudio.game.gameviews.PViewClass;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.casodistudio.R;

public class Background {

    public Bitmap background;
    public int x=0,y=0;

    public Background(Resources res, int screenX, int screenY){

        background= BitmapFactory.decodeResource(res, R.drawable.travel_background);
        //background=Bitmap.createScaledBitmap(background,screenX,screenY,false);

    }
}
