package com.example.casodistudio.game.gameviews;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.Button;

import com.example.casodistudio.R;
import com.example.casodistudio.game.GameActivityLandscape;

public class ViewLandscape extends SurfaceView implements Runnable{

    public boolean isPlaying;
    private Thread thread;
    public static float screenRatioX, screenRatioY;
    private int screenX, screenY;
    private Backgrounds background;
    private Floor floor;
    private Paint paint;
    private Character character;
    private Button apollo11, roverMars;
    private GameActivityLandscape activityLandscape;

    public ViewLandscape(GameActivityLandscape activity, int screenX, int screenY, short flag) {
        super(activity);

        this.activityLandscape=activity;
        this.screenX=screenX;
        this.screenY=screenY;
        screenRatioX=1920f/screenX;
        screenRatioY=1080F/screenY;
        //screenRatioY=1080f/screenY;

        background=new Backgrounds(getResources(),screenX,screenY,flag);
        floor=new Floor(this,getResources(),flag,screenX,screenY);
        //character=new Character(this, getResources(),flag,screenY,screenX);

        paint=new Paint();

        //character.y-=floor.floor.getHeight();

        /*int width,height;

        Bitmap apollo11bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.apollo_11);
        width=apollo11bitmap.getWidth();
        height=apollo11bitmap.getHeight();

        width*=(int) Resources.getSystem().getDisplayMetrics().density;
        height*=(int) Resources.getSystem().getDisplayMetrics().density;
        apollo11bitmap=Bitmap.createScaledBitmap(apollo11bitmap,width,height,false);
        BitmapDrawable bdrawable = new BitmapDrawable(getResources(),apollo11bitmap);

        apollo11=new Button(activity);
        roverMars=new Button(activity);

        apollo11.setBackground(bdrawable);*/
    }

   @Override
    public void run() {
        while (isPlaying){
            draw();
            sleep();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch(event.getAction()){
            case MotionEvent.AXIS_PRESSURE: //se l'utente sta tenendo premuto lo schermo
                if(event.getX()<screenX/2){   //se l'utente preme sulla parte sinistra dello schermo
                    if(background.x+10*screenRatioX>0){  //se il background è completamente fuori dallo schermo
                        break;
                    }
                    else{
                        background.x+=10*screenRatioX; //altrimenti sposta il background a sinistra
                        floor.x+=10*screenRatioX; //e il pavimento
                        return true;
                    }
                }
                else if(event.getX()>screenX/2){//quando questo succede il tocco arriva dalla parte destra
                    if(background.x+background.background.getWidth()/2-10*screenRatioX<=0){  //se il background è fuori dallo schermo non fa niente
                       break;
                    }
                    else{
                        background.x-=10*screenRatioX; //altrimenti sposta il background a destra
                        floor.x-=10*screenRatioX; //e il pavimento
                        return true;
                    }
                }
            case MotionEvent.ACTION_UP:     //se l'utente ha lasciato lo schermo
               //non fa nulla
                break;
        }

        return true;
    }

    private void draw(){
        if(getHolder().getSurface().isValid()){  //se il nostro oggetto surface è stato correttamente istanziato
            Canvas canvas=getHolder().lockCanvas(); //blocca il canva per poterlo usare
            canvas.drawBitmap(background.background,background.x,background.y,paint);
            canvas.drawBitmap(floor.floor,floor.x,floor.y,paint);
            //canvas.drawBitmap(character.characterStop,character.x,character.y,paint);
            getHolder().unlockCanvasAndPost(canvas);  //dopo aver disegnato le bitmap sblocca il canvas
        }
    }

    private void sleep(){
        try {
            Thread.sleep(16);  //setta la pausa del thread ogni 36 milli secondi così che il gioco runni a 36fps
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void resume(){  //quando il gioco riprende viene richiamato

        isPlaying=true;
        thread=new Thread(this);
        thread.start();  //il run del thread

    }
    public void pause(){

        isPlaying=false;
        try{
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
