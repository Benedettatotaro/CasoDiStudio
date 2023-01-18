package com.example.casodistudio.game.gameviews.LViews;

import static com.example.casodistudio.game.gameviews.LViews.ViewMuseum.screenRatioX;
import static com.example.casodistudio.game.gameviews.LViews.ViewMuseum.screenRatioY;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.casodistudio.GameActivityLandscape;

public class ViewLandscape extends SurfaceView implements Runnable {

    private GameActivityLandscape gameActivityLandscape;
    private short flagPlanet;
    private boolean isPlaying=true;
    private Paint paint;
    int screenX;
    int screenY;
    Thread thread;
    private Background background;
    boolean isEndBg;
    public ViewLandscape(GameActivityLandscape gameActivityLandscape,short flag,int screenX,int screenY) {

        super(gameActivityLandscape);
        this.gameActivityLandscape = gameActivityLandscape;

        this.flagPlanet = flag;
        this.screenX =screenX;
        this.screenY =screenY;

        paint =new Paint();

        background = new Background(getResources(),screenX,screenY,flagPlanet);








    }

    private void update(){

    }



    private void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background.background,background.x,background.y,paint);
            getHolder().unlockCanvasAndPost(canvas);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()){
            case MotionEvent.AXIS_PRESSURE:
                if(event.getX()<screenX/2 && background.x + 100*screenRatioX < 0 ){
                    background.x += 100*screenRatioX;
                }else if(event.getX()>screenX/2 && background.x+ background.background.getWidth() - 100*screenRatioX> screenX){
                    background.x -= 100*screenRatioX;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }


        return true;
    }

    public void resume(){
        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause(){
        try {
            isPlaying = false;
            thread.join();

        }catch (InterruptedException e){
            gameActivityLandscape.finish();
        }

    }

    private void sleep(){
        try {
            thread.sleep(36);
        }catch(InterruptedException e){
            gameActivityLandscape.finish();
        }
    }
    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            sleep();

        }
    }



}
