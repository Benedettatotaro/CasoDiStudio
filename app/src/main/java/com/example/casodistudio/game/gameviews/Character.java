package com.example.casodistudio.game.gameviews;

import static com.example.casodistudio.game.gameviews.ViewLandscape.screenRatioX;
import static com.example.casodistudio.game.gameviews.ViewLandscape.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.casodistudio.R;

public class Character {
    int x,y,width,height;
    public int idCharacterStop;
    //public int getIdCharacterMoving;
    Bitmap characterStop;
    ViewLandscape view;
    Floor floor;
    //Bitmap characterMoving;
    Character(ViewLandscape view,Resources res, short flag,int screenY,int screenX){

        this.view=view;

        characterStop= BitmapFactory.decodeResource(res, R.drawable.hirooki_fermo);
        //characterMoving=BitmapFactory.decodeResource(res, getIdCharacterMoving);

        width=characterStop.getWidth();
        height=characterStop.getHeight();

        width*=(int) screenRatioX;  //non va bene
        height*=(int) screenRatioY;  //non va bene

        characterStop=Bitmap.createScaledBitmap(characterStop,screenX,screenY,false);

        //y=-floor.height;
        //y=(int) (64*Resources.getSystem().getDisplayMetrics().density);  //non va bene
        //x=(int) (64/Resources.getSystem().getDisplayMetrics().density);  //non va bene

        y= floor.floor.getHeight();  //non funziona
        x=(int) (64*screenRatioX);

    }
    //Rect getCollisionShape(){   //definisco i margini di collisione dell'immagine
       // return new Rect(x,y,x+width,y+height);
    //}
}
