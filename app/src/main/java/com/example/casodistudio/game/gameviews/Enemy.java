package com.example.casodistudio.game.gameviews;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.casodistudio.R;

import java.util.Random;

public class Enemy {
    public int speed=20;
    int x=0,y=0,width,height;
    private Bitmap asteroid,asteroidGem;
    private int asteroidCounter=1;
    private Random random;
    public boolean isGem=false;
    int bound=5;
    private Bitmap alienShip;
    public Bitmap enemy;
    public Bitmap gem;
    public boolean gemShot=false;
    int xGem=0,yGem=0;
    int widthGem,heightGem;
    Enemy(Resources res, int screenX){
        asteroid= BitmapFactory.decodeResource(res, R.drawable.asteroid);
        asteroid=Bitmap.createScaledBitmap(asteroid,asteroid.getWidth()/3,asteroid.getHeight()/3,false);

        alienShip=BitmapFactory.decodeResource(res, R.drawable.alien_ship);
        alienShip=Bitmap.createScaledBitmap(alienShip,alienShip.getWidth()/2,alienShip.getHeight()/2,false);

        asteroidGem=BitmapFactory.decodeResource(res,R.drawable.gemasteroid);
        asteroidGem=Bitmap.createScaledBitmap(asteroidGem,asteroidGem.getWidth()/3,asteroidGem.getHeight()/3,false);

        gem=BitmapFactory.decodeResource(res,R.drawable.gem);
        gem=Bitmap.createScaledBitmap(gem,gem.getWidth()/2,gem.getHeight()/2,false);

        widthGem=gem.getWidth();
        heightGem=gem.getHeight();

        random=new Random();
        x=screenX+width;
    }
    Rect getCollisionShape(){  //definisco i margini di collisione dell'immagine
        return new Rect(x,y,x+width,y+height);
    }
    Rect getCollisionShapeGem(){  //definisco i margini di collisione dell'immagine
        return new Rect(xGem,yGem,xGem+widthGem,yGem+heightGem);
    }
    public void getEnemy(){

        int r=random.nextInt(bound);
        if(r==2||r==4){
            height=alienShip.getHeight();
            width=alienShip.getWidth();
            enemy=alienShip;
            isGem=false;
        }
        else if(r==1){
            isGem=true;
            height=asteroidGem.getHeight();
            width=asteroidGem.getWidth();
            enemy=asteroidGem;
        }
        else{
            height=asteroid.getHeight();
            width=asteroid.getWidth();
            enemy=asteroid;
            isGem=false;
        }

    }
}
