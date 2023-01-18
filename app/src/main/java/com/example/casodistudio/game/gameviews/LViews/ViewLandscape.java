package com.example.casodistudio.game.gameviews.LViews;

import static com.example.casodistudio.game.gameviews.LViews.ViewMuseum.screenRatioX;
import static com.example.casodistudio.game.gameviews.LViews.ViewMuseum.screenRatioY;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.casodistudio.GameActivityLandscape;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

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
    private List<Floor> floors;
    private int gameCounter = 0;
    private Random random;
    private int seed = 100;
    private Floor floor;

    public ViewLandscape(GameActivityLandscape gameActivityLandscape,short flag,int screenX,int screenY) {

        super(gameActivityLandscape);
        this.gameActivityLandscape = gameActivityLandscape;

        this.flagPlanet = flag;
        this.screenX =screenX;
        this.screenY =screenY;

        random = new Random();

        paint =new Paint();

        background = new Background(getResources(),screenX,screenY,flagPlanet);

        floors = new ArrayList<>();


    }

    private void update(){
        if(gameCounter % 50 == 0){
            generateFloor();
        }



    }



    private void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background.background,background.x,background.y,paint);
            for(Floor floor:floors) {
                canvas.drawBitmap(floor.floor,floor.x,floor.y,paint);
            }

            getHolder().unlockCanvasAndPost(canvas);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()){
            case MotionEvent.AXIS_PRESSURE:
                if(event.getX()<screenX/2 && background.x + 100*screenRatioX < 0 ){
                    background.x += 100*screenRatioX;
                    for(Floor floor:floors){
                        floor.x +=100*screenRatioX;
                    }
                }else if(event.getX()>screenX/2 && background.x+ background.background.getWidth() - 100*screenRatioX> screenX){
                    background.x -= 100*screenRatioX;
                    for(Floor floor:floors){
                        floor.x -=100*screenRatioX;
                    }
                    isPlaying =false;

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
            gameCounter++;

        }
    }


    public void generateFloor(){
        for(int i=0;i< floors.size();i++){
            Floor floor = new Floor(getResources(),screenX,screenY,flagPlanet);

            if(i>=1){
                floors.get(i).x = random.nextInt(seed) + 50 + floors.get(i-1).x;
            }

            floors.add(floor);
        }
    }


}
