package com.example.casodistudio.game.gameviews.LViews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.casodistudio.HallActivity;
import com.example.casodistudio.R;

public class ViewMars extends SurfaceView implements Runnable {

    private Thread thread;
    private Bitmap background_marte;
    private Bitmap ground_marte;
    private Bitmap rover;
    private Bitmap rover1;
    private Bitmap rover2;
    private Bitmap pause;
    protected int screenX, screenY;
    public static float screenRatioX, screenRatioY;
    private HallActivity hallactivity;
    private Paint paint;
    public boolean isPlaying;
    boolean isPressed=false;
    boolean isRight=true;
    boolean isJumping=false;
    boolean wasMirroredLeft=false,wasMirroredRight=true;
    boolean isSwitching=false;
    boolean isEndFirstLevel=false;
    boolean isEndTravelToMars=false, isEndTravelToMoon=false;
    private int counter;
    private int x;


    public ViewMars(HallActivity activity, int screenX, int screenY)
    {
        super(activity);
        this.hallactivity=activity;
        this.screenX=screenX;
        this.screenY=screenY;
        screenRatioX=1920f/screenX;
        screenRatioY=1080F/screenY;
        paint=new Paint();


        // ci sara da gestire la risoluzione del ground qui

        ground_marte = BitmapFactory.decodeResource(getResources(), R.drawable.ground_marte);
        background_marte = BitmapFactory.decodeResource(getResources(), R.drawable.background_marte);
        rover = BitmapFactory.decodeResource(getResources(), R.drawable.background_marte);
    }


    public void draw()
    {
        if(getHolder().getSurface().isValid())
        {
            Canvas canvas=getHolder().lockCanvas();
            canvas.drawBitmap(background_marte,0,0,paint);
            canvas.drawBitmap(ground_marte,0,0,paint);
            canvas.drawBitmap(getCharacter(),getX(),screenY-ground_marte.getHeight()-rover.getHeight(),paint);
        }
    }
    @Override
    public void run()
    {

    }

    public boolean onTouchEvent(MotionEvent event){

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(event.getX()>screenX-pause.getWidth()-10*screenRatioX&&event.getY()<10*screenRatioX+pause.getHeight()){
                    //se il tocco avviene nel quadrato in cui si trova il bottone di pausa
                    short flag=0;
                    //hallactivity.callManager(flag);
                    //activityLandscape.callPauseFragment(); dovrebbe chiamare il fragment di pausa del gioco ma non so se si possa fare
                }

                isJumping=true;
                break;
            case MotionEvent.AXIS_PRESSURE: //se l'utente sta tenendo premuto lo schermo
                if(event.getX()<screenX/2) {
                    isPressed=true;
                    isRight=false;  //imposta che il personaggio deve muoversi verso sinistra
                    return true;
                }
                else if(event.getX()>screenX/2){//quando questo succede il tocco arriva dalla parte destra
                    isPressed=true;
                    isRight=true;  //imposta che il personaggio deve muoversi verso destra
                    return true;
                }
            case MotionEvent.ACTION_UP:     //se l'utente ha lasciato lo schermo
                isJumping=false;
                isPressed=false;
                break;

        }

        return true;
    }




}
