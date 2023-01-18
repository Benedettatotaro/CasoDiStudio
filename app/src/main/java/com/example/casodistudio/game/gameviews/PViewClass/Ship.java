package com.example.casodistudio.game.gameviews.PViewClass;

import static com.example.casodistudio.game.gameviews.LViews.ViewMuseum.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.casodistudio.R;

import java.util.Random;

public class Ship {

    public Bitmap shipPowerup,shipShootPowerup;
    public Bitmap powerup;
    public int xPoweup,yPowerup,speedPowerup=20;
    public int counterPouwerup;
    public Bitmap shipShoot;
    public int xShip,yShip;
    public Bitmap ship;
    public boolean isPressed=false;
    public boolean isPowerup=false,isPoweringup=false;
    private Random random;
    private int screenX, screenY;

    public Ship(Resources res, int screenX, int screenY,short flag){
        random=new Random();
        this.screenX=screenX;
        this.screenY=screenY;

        if(flag==0){
            ship= BitmapFactory.decodeResource(res, R.drawable.ship_astronaut);
            shipShoot=BitmapFactory.decodeResource(res,R.drawable.ship_shoot);
            shipPowerup=BitmapFactory.decodeResource(res,R.drawable.ship_powerup);
            shipShootPowerup=BitmapFactory.decodeResource(res,R.drawable.ship_shoot_powerup);
        }
        else if(flag==1){
            //TO DO: CARICARE LA BITMAP DELLA NAVICELLA CON IL ROVER
        }

        ship=Bitmap.createScaledBitmap(ship,ship.getWidth()/7,ship.getHeight()/7,false);
        shipShoot=Bitmap.createScaledBitmap(shipShoot,shipShoot.getWidth()/7,shipShoot.getHeight()/7,false);
        shipPowerup=Bitmap.createScaledBitmap(shipPowerup,shipPowerup.getWidth()/7,shipPowerup.getHeight()/7,false);
        shipShootPowerup=Bitmap.createScaledBitmap(shipShootPowerup,shipShootPowerup.getWidth()/7,shipShootPowerup.getHeight()/7,false);
        xShip=(screenX/2)-(ship.getWidth()/2);
        yShip=(int)(screenY-10*screenRatioY-ship.getHeight());

        powerup=BitmapFactory.decodeResource(res,R.drawable.powerup);
        powerup=Bitmap.createScaledBitmap(powerup,powerup.getWidth()/2,powerup.getHeight()/2,false);

    }

    public Bitmap getShip(){
        if(isPoweringup){
            if(isPressed){
                return shipShootPowerup;
            }
            return shipPowerup;
        }
        if(isPressed){
            return shipShoot;
        }
        return ship;
    }

    public void generatePowerup(){
        if(!isPowerup){  //se non ci sono altri power up sullo schermo
            int i=random.nextInt(10)+1;
            if(i==5){
                isPowerup=true; //se la random genera 5 in un range da 1 a 10 genera il powerup
                yPowerup=0;
                xPoweup=random.nextInt(screenX-powerup.getWidth());
            }
        }
    }

    public Rect getCollisionShape(){   //definisco i margini di collisione dell'immagine
        return new Rect(xShip,yShip,xShip+getShip().getWidth(),yShip+getShip().getHeight());
    }

    public Rect getCollisionShapePowerUp(){   //definisco i margini di collisione dell'immagine
        return new Rect(xPoweup,yPowerup,xPoweup+powerup.getWidth(),yPowerup+powerup.getHeight());
    }
}