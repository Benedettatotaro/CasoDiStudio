package com.example.casodistudio.game.gameviews.LViews;

import static com.example.casodistudio.game.gameviews.LViews.ViewMuseum.screenRatioX;
import static com.example.casodistudio.game.gameviews.LViews.ViewMuseum.screenRatioY;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.casodistudio.GameActivityLandscape;
import com.example.casodistudio.HallActivity;
import com.example.casodistudio.R;

import java.util.ArrayList;
import java.util.List;
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
    private Floor[] floors;
    private int gameCounter = 0;
    private Random random;
    private int seed = 300;
    private Character character;
    private List<Character> aliens;
    private List<Gem> gems;
    private boolean isGameOver;
    private int gemCounter;
    private Bitmap buttonJump;
    private Bitmap buttonLeft;
    private Bitmap buttonRight;
    private Bitmap flagEnd;
    private Bitmap powerup;
    private int xPowerup,yPowerup;
    int counterPowerup;
    boolean powerUpIsOnTheScreen=false;
    private int xFlag,yFlag;
    private boolean win=false;
    private Bitmap pause;
    private boolean isEndBgLeft;
    private boolean isEndBgRight;
    private boolean flagIsOnTheScreen=false;
    private Paint text;
    SharedPreferences prefs ;
    SharedPreferences.Editor editor;

    public ViewLandscape(GameActivityLandscape gameActivityLandscape,short flag,int screenY,int screenX) {

        super(gameActivityLandscape);
        this.gameActivityLandscape = gameActivityLandscape;
        prefs = getContext().getSharedPreferences("game",getContext().MODE_PRIVATE);
        this.flagPlanet = flag;
        this.screenX =screenX;
        this.screenY =screenY;

        random = new Random();

        paint =new Paint();

        floors=new Floor[10];
        gemCounter = prefs.getInt("tempGems",0);

        background = new Background(getResources(),screenX,screenY,flagPlanet,prefs.getInt("xPosition",0));

        for(int i=0; i<10; i++){
            Floor floor = new Floor(getResources(),screenX,screenY,flagPlanet);

            if(i==7){
                floor.x=10+floors[i-1].x+floors[i-1].floor.getWidth();
                floors[i]=floor;
            }
            else if(i==0){
                floor.x=0;
                floors[i]=floor;
            }
            else{
                floor.x=random.nextInt(seed) + 200 +floors[i-1].x+floors[i-1].floor.getWidth();
                floors[i]=floor;
            }

        }

        character=new Character(flag,getResources(),screenX,screenY,screenRatioX,screenRatioY);
        character.y=screenY-floors[0].floor.getHeight()-character.getStopAnimation().getHeight()+1;//inizializza la posizione del personaggio sull'asse delle y

        isGameOver=false;

        text=new Paint();
        text.setColor(Color.WHITE);
        text.setTextSize(50);
        text.setTextAlign(Paint.Align.LEFT);

        aliens=new ArrayList<>();

        gems=new ArrayList<>();
        if(flag==0){
            //luna
            flagEnd= BitmapFactory.decodeResource(getResources(), R.drawable.american_flag);
            flagEnd=Bitmap.createScaledBitmap(flagEnd,flagEnd.getWidth()/2,flagEnd.getHeight()/2,false);
            powerup=BitmapFactory.decodeResource(getResources(),R.drawable.powerup_astronaut);
            powerup=Bitmap.createScaledBitmap(powerup,powerup.getWidth()/2,powerup.getHeight()/2,false);
        }
        else if(flag==1){
            flagEnd= BitmapFactory.decodeResource(getResources(), R.drawable.plant);
            flagEnd=Bitmap.createScaledBitmap(flagEnd,flagEnd.getWidth()/2,flagEnd.getHeight()/2,false);
            powerup=BitmapFactory.decodeResource(getResources(),R.drawable.powerup_rover);
            powerup=Bitmap.createScaledBitmap(powerup,powerup.getWidth()/2,powerup.getHeight()/2,false);
        }



        buttonJump=BitmapFactory.decodeResource(getResources(),R.drawable.button_jump);
        buttonJump=Bitmap.createScaledBitmap(buttonJump,buttonJump.getWidth()/30,buttonJump.getHeight()/30,false);

        buttonLeft=BitmapFactory.decodeResource(getResources(),R.drawable.left_button);
        buttonLeft=Bitmap.createScaledBitmap(buttonLeft,buttonLeft.getWidth()/30,buttonLeft.getHeight()/30,false);

        buttonRight=BitmapFactory.decodeResource(getResources(),R.drawable.right_button);
        buttonRight=Bitmap.createScaledBitmap(buttonRight,buttonRight.getWidth()/30,buttonRight.getHeight()/30,false);

        xPowerup=random.nextInt((int) screenX);
        yPowerup=random.nextInt((int) screenY-floors[0].floor.getHeight()-powerup.getHeight()-5);
        powerUpIsOnTheScreen=true;

        pause=BitmapFactory.decodeResource(getResources(),R.drawable.pause);
        pause=Bitmap.createScaledBitmap(pause,pause.getWidth()/45,pause.getHeight()/45,false);
    }

    Rect collisonShapePowerUp(){return new Rect(xPowerup,yPowerup,xPowerup+powerup.getWidth(),yPowerup+powerup.getHeight());}

    Rect collisonShapeFlag(){return new Rect(xFlag,yFlag,xFlag+flagEnd.getWidth(),yFlag+flagEnd.getHeight());}
    private void update(){

        if(gameCounter%50==0&&character.isPressed){
            Character alien=new Character(4,getResources(),screenX,screenY,screenRatioX,screenRatioY);
            alien.charSpeed=random.nextInt(screenX*5)+screenX;   //genera casualmente una posizione lungo l'asse delle x dell'alieno
            alien.isPressed=true;  //inizia a far muovere l'alieno
            alien.isRight=false;
            alien.y=screenY-floors[0].floor.getHeight()-alien.getStopAnimation().getHeight()+1;
            aliens.add(alien); //lo aggiunge all'array list
            Gem gem=new Gem(getResources(),screenX,screenY);
            gem.x=random.nextInt((int) screenX);
            gem.y=random.nextInt((int) screenY-floors[0].floor.getHeight()-gem.gem.getHeight()-5);
            gems.add(gem);
        }


        if(Rect.intersects(character.collisionShape(),collisonShapePowerUp())&&powerUpIsOnTheScreen){
            character.isPoweringUp=true;
            powerUpIsOnTheScreen=false;
            character.setPoweringUp();
            counterPowerup=gameCounter;
        }

        if(character.isPoweringUp&&gameCounter>counterPowerup+50){
            character.resetPoweringUp();
            character.isPressed=false;
            character.isPoweringUp=false;
        }

        List<Character> alienTrash=new ArrayList<>();
        List<Gem> gemTrash=new ArrayList<>();

        for(Gem gem:gems){
            if(Rect.intersects(gem.collisionShape(),character.collisionShape())){
                gemCounter++;
                gemTrash.add(gem);
            }
        }

        if(!gemTrash.isEmpty()){
            for(Gem gem:gemTrash){
                gems.remove(gem);
            }
        }


        if(!isEndBgRight&&!isEndBgLeft){ //se non è finito il background da destra e sinistra si è raggiunti una certa distanza la x del personaggio non aumenta più
            if(character.isRight&&character.charSpeed>screenX/2-character.getStopAnimation().getWidth()){
                character.charSpeed=screenX/2-character.getStopAnimation().getWidth();
            }
            else if(!character.isRight&&character.charSpeed<screenX/2){
                character.charSpeed=screenX/2-character.getStopAnimation().getWidth();
            }
        }

        for(Character alien:aliens){
            if(alien.charSpeed<=0){
                alienTrash.add(alien);
            }
            if(Rect.intersects(character.collisionShape(),alien.collisionShape())){
                //se l'astronuta si scontra con uno degli alieni
                isGameOver=true; //perdi
            }
        }
        //rimuove tutti gli alieni usciti dallo schermo
        for (Character alien:alienTrash){
            aliens.remove(alien);
        }

        int i;
        for(i=0;i<10;i++){
            if(Rect.intersects(character.collisionShape(),floors[i].collisionShape())){
                floors[i].isOnTheFloor=true;
                break; //se il personaggio è su uno dei pavimenti imposta la variabile a true
            }
            else if(!Rect.intersects(character.collisionShape(),floors[i].collisionShape())&&floors[i].isOnTheFloor){ //se il personaggio non è più sul pavimento
                floors[i].isOnTheFloor=false; //mette la variabile a falso
            }
        }


        int counter=0;



        if(Rect.intersects(collisonShapeFlag(),character.collisionShape())&&flagIsOnTheScreen){
            win=true;
        }


        for(i=0;i<10;i++){
            if(!floors[i].isOnTheFloor){
                counter++;
            }
        }

        if(counter==10&&character.y>=screenY-floors[0].floor.getHeight()-character.getStopAnimation().getHeight()+1){  //se non è più su nessun pavimento
            isGameOver=true; //perdi
        }

        if(character.y<0&&!character.isPoweringUp){
            character.y=0;
        }
        if(character.isPoweringUp){
            character.isPressed=true;
            character.y=0;
        }

        if(character.y>=screenY-floors[0].floor.getHeight()-character.getStopAnimation().getHeight()+1){
            character.y=screenY-floors[0].floor.getHeight()-character.getStopAnimation().getHeight()+1;
        }

        if(character.isJumping)
        {

            character.y-=20;

        }
        else{
            if(flagPlanet==0){
                character.y+=20;
            }
            else{
                character.y+=10;
            }

        }

    }


    private void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            prefs = getContext().getSharedPreferences("game",getContext().MODE_PRIVATE);
            editor = prefs.edit();
            canvas.drawBitmap(background.background,background.x,background.y,paint);
            canvas.drawText(""+gemCounter,30*screenRatioY,30*screenRatioY,text);
            for(Floor floor:floors) {
                canvas.drawBitmap(floor.floor,floor.x,floor.y,paint);
            }
            canvas.drawBitmap(pause,screenX-pause.getWidth()-10*screenRatioX,10*screenRatioX,paint);

            if(isEndBg){
                xFlag=screenX-flagEnd.getWidth()-100; //posiziona la bandiera sull'ultimo pavimento
                yFlag=screenY-floors[0].floor.getHeight()-flagEnd.getHeight();
                canvas.drawBitmap(flagEnd,xFlag,screenY-floors[0].floor.getHeight()-flagEnd.getHeight(),paint);
                flagIsOnTheScreen=true;
            }
            if(powerUpIsOnTheScreen){
                canvas.drawBitmap(powerup,xPowerup,yPowerup,paint);
            }

            canvas.drawBitmap(buttonJump,10*screenRatioX,screenY-10*screenRatioX-buttonJump.getHeight(),paint);
            canvas.drawBitmap(buttonLeft,screenX-10*screenRatioX-buttonLeft.getWidth(),screenY-10*screenRatioX-buttonLeft.getHeight(),paint);
            canvas.drawBitmap(buttonRight,screenX-buttonRight.getWidth()-buttonLeft.getWidth()-20*screenRatioX,screenY-10*screenRatioX-buttonJump.getHeight(),paint);


            if(!gems.isEmpty()){
                for(Gem gem:gems){
                    canvas.drawBitmap(gem.gem,gem.x,gem.y,paint);
                }
            }

            if(!aliens.isEmpty()){
                for(Character alien: aliens){
                    canvas.drawBitmap(alien.charOrientation(),alien.alienMovement(),screenY-floors[0].floor.getHeight()-alien.getStopAnimation().getHeight()+1,paint);
                }
            }

            if(win){
                isPlaying=false; //il gioco si blocca
                Paint textPaint = new Paint();
                textPaint.setTextAlign(Paint.Align.CENTER);
                int xPos = (canvas.getWidth() / 2);
                int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
                textPaint.setColor(Color.WHITE);
                textPaint.setTextSize(100);
                canvas.drawText("Hai vinto!", xPos,yPos,textPaint);
                getHolder().unlockCanvasAndPost(canvas);
                waitBeforeExiting();
                long appoggio;
                if(flagPlanet == 0) // luna
                {
                    appoggio = prefs.getLong("moonGem", 0);
                    appoggio =+ gemCounter;
                    editor.putLong("moonGem", appoggio);
                    editor.commit();

                }else // marte
                {
                    appoggio = prefs.getLong("marsGem", 0);
                    appoggio =+ gemCounter;
                    editor.putLong("marsGem", appoggio);
                    editor.commit();
                }

                gemCounter = 0;
                background.x = 0;
                flagPlanet = -1;

                Intent i = new Intent(getContext(), HallActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(i);

            }

            if(isGameOver){ //se perdi
                gemCounter = 0;
                background.x = 0;
                //flagPlanet = -1;
                editor.putInt("flagLevel", -1);


                isPlaying=false; //il gioco si blocca
                Paint textPaint = new Paint();
                textPaint.setTextAlign(Paint.Align.CENTER);
                int xPos = (canvas.getWidth() / 2);
                int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
                textPaint.setColor(Color.WHITE);
                textPaint.setTextSize(100);
                canvas.drawText("Hai perso!", xPos,yPos,textPaint);
                getHolder().unlockCanvasAndPost(canvas);

                waitBeforeExiting();



                return;
            }
            canvas.drawBitmap(character.charOrientation(),character.charMovement(),character.y,paint);

            getHolder().unlockCanvasAndPost(canvas);

        }

    }

    private void waitBeforeExiting(){
        try {
            Thread.sleep(3000);
            gameActivityLandscape.startActivity(new Intent(gameActivityLandscape, HallActivity.class));
            gameActivityLandscape.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()){

            case MotionEvent.AXIS_PRESSURE:
                if(event.getX()>screenX-pause.getWidth()-10*screenRatioX&&event.getY()<10*screenRatioX+pause.getHeight()){
                    //se il tocco avviene nel quadrato in cui si trova il bottone di pausa
                    short flag=0,flagActivity=2;
                    gameActivityLandscape.callManager(flag,flagActivity);
                }
                if(event.getX()>10*screenRatioX&&event.getX()<10*screenRatioX+buttonJump.getWidth()&&event.getY()>screenY-10*screenRatioX-buttonJump.getHeight()&&event.getY()<screenY-10*screenRatioX){
                    character.isJumping=true;
                    break;
                }
                else if(event.getX()>screenX-buttonRight.getWidth()-buttonLeft.getWidth()-20*screenRatioX&&event.getX()<screenX-20*screenRatioX-buttonRight.getWidth()-10*screenRatioX&&event.getY()>screenY-10*screenRatioX-buttonJump.getHeight()&&event.getY()<screenY-10*screenRatioX){
                    //ha cliccato il bottone di sinistra
                    if(background.x + 2 < 0 ){
                        background.x += 5;
                        for(int i=0; i<10; i++){
                            floors[i].x+=5;
                        }
                        if(!gems.isEmpty()){
                            for(Gem gem:gems){
                                gem.x+=10;
                            }
                        }
                        if(powerUpIsOnTheScreen) {
                            xPowerup += 10;
                        }
                        isEndBg=false;
                        isEndBgLeft=false;
                        isEndBgRight=false;
                    }
                    else{
                        isEndBgLeft=true;
                        isEndBgRight=false;
                        isEndBg=false;
                    }
                    character.isJumping=false;
                    character.setIsPressed(true);
                    character.setIsRight(false);
                    break;
                }
                //se è stato il premuto il bottone di destra
                else if(event.getX()>screenX-10*screenRatioX-buttonLeft.getWidth()&&event.getX()<screenX-10*screenRatioX&&event.getY()>screenY-10*screenRatioX-buttonJump.getHeight()&&event.getY()<screenY-10*screenRatioX){
                    if(background.x+ background.background.getWidth() - 2> screenX){
                        background.x -= 5;
                        for(int i=0; i<10; i++){
                            floors[i].x-=5; //gli altri si muovono seguendo il riferimento del primo
                        }
                        if(!gems.isEmpty()){
                            for(Gem gem:gems){
                                gem.x-=10;
                            }
                        }
                        isEndBg=false;
                        isEndBgRight=false;
                        isEndBgLeft=false;
                        if(powerUpIsOnTheScreen) {
                            xPowerup -= 10;
                        }
                    }
                    else{
                        isEndBgRight=true;
                        isEndBgLeft=false;
                        isEndBg=true;
                    }
                    character.isJumping=false;
                    character.setIsPressed(true);
                    character.setIsRight(true);
                    //isPlaying =false;
                break;
                }            case MotionEvent.ACTION_UP:
                character.setIsPressed(false);
                character.isJumping=false;
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

        prefs = getContext().getSharedPreferences("game",getContext().MODE_PRIVATE);
        editor = prefs.edit();
        editor.putInt("tempGems",gemCounter);
        editor.putInt("xPosition", background.x);
        editor.putInt("flagLevel", flagPlanet);
        editor.commit();
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

}
