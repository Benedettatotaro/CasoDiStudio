package com.example.casodistudio.game.gameviews;

import static com.example.casodistudio.game.gameviews.ViewLandscape.screenRatioX;
import static com.example.casodistudio.game.gameviews.ViewLandscape.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.casodistudio.R;

public class Backgrounds {
    private int width,height;

    public int x=0,y=0;

    Bitmap background; //oggetto che conterrà lo sfondo

    Backgrounds(Resources res,int screenX, int screenY, short flag){
       background= BitmapFactory.decodeResource(res, R.drawable.background_museo_vero);
        switch (flag){
            case 0: //caricara il background del museo
                //TO DO:background= BitmapFactory.decodeResource(res, R.drawable.background_museo);
                break;
            case 1: //caricara il background della Luna
                //TO DO:background=BitmapFactory.decodeResource(res, R.drawable.background_luna);
                break;
            case 2: //caricara il background di Marte
                //TO DO:background=BitmapFactory.decodeResource(res,R.drawable.background_marte);
                break;
        }
        width=background.getWidth();
        height=background.getHeight();
        background= Bitmap.createScaledBitmap(background,screenX*2,screenY,false); //lo screen x deve essere due volte più lungo dello schermo
    }

    Rect getCollisionShape(){   //definisco i margini di collisione dell'immagine
        return new Rect(x,y,x+width,y+height);
    }
}
