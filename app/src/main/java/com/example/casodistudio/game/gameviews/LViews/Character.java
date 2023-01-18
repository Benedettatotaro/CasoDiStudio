package com.example.casodistudio.game.gameviews.LViews;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.casodistudio.R;

public class Character {
    private Bitmap stopAnimation;
    private Bitmap moveAnimation1;
    private Bitmap moveAnimation2;
    boolean isRight=true;
    boolean wasMirroredLeft=false;
    boolean wasMirroredRight=true;
    boolean isPressed=false;
    boolean isJumping=false;
    int counter=1;
    int charSpeed=0;
    int viewIdentificator;
    float density;
    private int screenX, screenY;
    private float screenRatioX, screenRatioY;


    public Character(int viewIdentificator, Resources res, int screenX, int screenY , float screenRatioX, float screenRatioY)
    {
        this.viewIdentificator = viewIdentificator;
        setAnimation(1, res);
        this.screenX = screenX;
        this.screenY = screenY;
        this.screenRatioX = screenRatioX;
        this.screenRatioY = screenRatioY;
       // setBitmapResolution(res, viewIdentificator);



    }

    public Bitmap getStopAnimation()
    {
        return stopAnimation;
    }
    public Bitmap getMoveAnimation1()
    {
        return moveAnimation1;
    }
    public Bitmap getMoveAnimation2()
    {
        return moveAnimation2;
    }

    private void setAnimation(int flag, Resources res) {

        if(flag == 1) // museo , metodo di prova
        {
          this.stopAnimation = BitmapFactory.decodeResource(res, R.drawable.hirooki_fermo);
          this.moveAnimation1 = BitmapFactory.decodeResource(res, R.drawable.hirooki1);
          this.moveAnimation2 = BitmapFactory.decodeResource(res, R.drawable.hirooki2);

          this.stopAnimation = Bitmap.createScaledBitmap(stopAnimation,stopAnimation.getWidth()/7, stopAnimation.getHeight()/7,false);
          this.moveAnimation1= Bitmap.createScaledBitmap(moveAnimation1,moveAnimation1.getWidth()/7, moveAnimation1.getHeight()/7,false);
          this.moveAnimation2 =Bitmap.createScaledBitmap(moveAnimation2,moveAnimation2.getWidth()/7, moveAnimation2.getHeight()/7,false);
        }

    }


    public void setIsRight(boolean isRight)
    {
        this.isRight = isRight;
    }
    public void setIsPressed(boolean isPressed)
    {
        this.isPressed = isPressed;
    }
    public void setWasMirroredLeft(boolean wasMirroredLeft)
    {
        this.wasMirroredLeft = wasMirroredLeft;
    }
    public void setWasMirroredRight(boolean wasMirroredRight)
    {
        this.wasMirroredRight = wasMirroredRight;
    }
    public void setIsJumping(boolean isJumping)
    {
        this.isJumping = isJumping;
    }



    public Bitmap charOrientation(){
        if(isRight&&wasMirroredLeft){ //se l'utente sta premendo per spostarsi verso destra e la bitmap è flippata restituisce la bitmap normale
            stopAnimation=Bitmap.createScaledBitmap(stopAnimation,-(stopAnimation.getWidth()),stopAnimation.getHeight(),false);
            moveAnimation1=Bitmap.createScaledBitmap(moveAnimation1,-(moveAnimation1.getWidth()),moveAnimation1.getHeight(),false);
            moveAnimation2=Bitmap.createScaledBitmap(moveAnimation2,-(moveAnimation2.getWidth()),moveAnimation2.getHeight(),false);
            wasMirroredRight=true;
            wasMirroredLeft=false;
        }
        else if((!isRight)&&wasMirroredRight){  //altrimenti l'utente sta premendo per spostarsi verso sinistra e la bitmap è normale la restituisce flippata
            stopAnimation=Bitmap.createScaledBitmap(stopAnimation,-(stopAnimation.getWidth()),stopAnimation.getHeight(),false);
            moveAnimation1=Bitmap.createScaledBitmap(moveAnimation1,-(moveAnimation1.getWidth()),moveAnimation1.getHeight(),false);
            moveAnimation2=Bitmap.createScaledBitmap(moveAnimation2,-(moveAnimation2.getWidth()),moveAnimation2.getHeight(),false);
            wasMirroredLeft=true;
            wasMirroredRight=false;
        }
        if(isPressed){
            if(counter==1){
                counter++;
                return moveAnimation1;
            }
            if(counter==2){
                counter++;
                return moveAnimation2;
            }
            counter--;
        }
        return stopAnimation;
    }

    public float charMovement()
    {
        if(isPressed){  //se il background è arrivato alla fine
            if(isRight&&(charSpeed<screenX-stopAnimation.getWidth()-10*screenRatioX)){ //se l'utente sta premendo verso destra //e se x è minore di questo punto sull'asse delle x
                charSpeed+= (int) (10*screenRatioX); //aumenta ancora la x finchè il personaggio non arriva alla fine dello schermo quindi isEnd diventa false
            }
            else if((!isRight)&&(charSpeed>10*screenRatioX)){ //se l'utente sta premendo verso sinistra
                charSpeed-= (int) (10*screenRatioX); //diminuisce ancora la x finchè il personaggio non arriva alla fine dello schermo quindi isEnd diventa false
            }
        }
        return charSpeed;

    }
    public float charJump()
    {
        float charY = stopAnimation.getHeight();
        if(isJumping)
        {
            int i;
            for(i=0;i<3;i++){
                charY+=10*screenRatioX;
            }
            for(int j=i;j>=0; j--){
                charY-=10*screenRatioX;
            }
        }

        return charY;
    }


   /* public void setBitmapResolution(Resources res, int viewIdentificator)
    {
        if(viewIdentificator == 0) // view luna
        {
            float density = res.getDisplayMetrics().density;

            if (density >= 4.0)
            {
                // qui andranno tutte le bitmap che servono al museo
                floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
                Character char = new Character();

            }else if (density >= 3.0) {

                floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
            }else if (density >= 2.0) {
                floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
            }else if (density >= 1.5) {
                floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
            }else if (density >= 1.0) {
                floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);

            }else{ floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);}

        }else if(viewIdentificator == 1) // view marte
        {
            if (density >= 4.0)
            {
                // qui andranno tutte le bitmap che servono al museo
                floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
                Character char = new Character();

            }else if (density >= 3.0) {

                floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
            }else if (density >= 2.0) {
                floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
            }else if (density >= 1.5) {
                floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
            }else if (density >= 1.0) {
                floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);

            }else{ floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);}
        }else
        {
            if (density >= 4.0)
            {
                // qui andranno tutte le bitmap che servono al museo
                floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);

                Character char = new Character();

            }else if (density >= 3.0) {

                floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
            }else if (density >= 2.0) {
                floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
            }else if (density >= 1.5) {
                floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
            }else if (density >= 1.0) {
                floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);

            }else{ floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);}

        }



        if (density >= 4.0)
        {
            // qui andranno tutte le bitmap che servono al museo
            floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
            Character char = new Character();

        }else if (density >= 3.0) {

            floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
        }else if (density >= 2.0) {
            floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
        }else if (density >= 1.5) {
            floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
        }else if (density >= 1.0) {
            floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);

        }else{ floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);}

    }*/
}
