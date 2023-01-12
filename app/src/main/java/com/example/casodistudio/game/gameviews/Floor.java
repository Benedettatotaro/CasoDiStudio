package com.example.casodistudio.game.gameviews;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.casodistudio.R;

public class Floor {

    int x=0,y=0,width,height;
    Bitmap floor;
    ViewLandscape view;

    Floor(ViewLandscape view, Resources res, short flag, int screenX,int screenY){  //il costruttore riceve in input delle risorse e un flag per capire quale pavimento caricare

        this.view=view;

        floor= BitmapFactory.decodeResource(res, R.drawable.pavimento_museo);
        /*switch (flag){
            case 0: //se il flag è zero carica il pavimento del museo
                floor= BitmapFactory.decodeResource(res, R.drawable.pavimento_museo);
                break;
            case 1: //se il flag è uno carica il pavimento della luna
                floor= BitmapFactory.decodeResource(res, R.drawable.ground_luna);
                break;
            case 2:  //se il flag è due carica il pavimento di marte
                floor= BitmapFactory.decodeResource(res, R.drawable.ground_marte);
                break;
        }*/

        width=floor.getWidth();
        height=floor.getHeight();

        float density=Resources.getSystem().getDisplayMetrics().density;
        //width*=(int) Resources.getSystem().getDisplayMetrics().density;
        //height*=(int) Resources.getSystem().getDisplayMetrics().density;

        floor=Bitmap.createScaledBitmap(floor,screenY*2,height,false);  //crea una bitmap luga quanto il doppio del pavimemnto e alta scalata rispetto alla risoluzione dello schermo

        //y-=height;
    }
}
