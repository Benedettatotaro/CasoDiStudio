package com.example.casodistudio.game.gameviews.LViews;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.casodistudio.R;

public class Gem {
    Bitmap gem;
    int x,y;

    public Gem(Resources res,int screenX,int screenY){
        gem= BitmapFactory.decodeResource(res, R.drawable.moon_gem);
        gem=Bitmap.createScaledBitmap(gem,gem.getWidth()/2,gem.getHeight()/2,false);

    }

    Rect collisionShape(){return new Rect(x,y,x+gem.getWidth(),y+gem.getHeight());}
}
