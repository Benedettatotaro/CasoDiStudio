package com.example.casodistudio.game.gameviews;

import static com.example.casodistudio.game.gameviews.LViews.ViewMuseum.screenRatioX;
import static com.example.casodistudio.game.gameviews.LViews.ViewMuseum.screenRatioY;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.casodistudio.GameActivityPortrait;
import com.example.casodistudio.HallActivity;
import com.example.casodistudio.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ViewTravel extends SurfaceView implements Runnable, SensorEventListener {

    private Thread thread;
    private boolean isPlaying=true;
    private GameActivityPortrait gameActivityPortrait;
    private Bitmap background1, background2;
    private int xBackground1=0,yBackground1=0,xBackground2=0,yBackground2;
    private int xShip,yShip;
    private Bitmap ship;
    private Paint paint;
    private SensorManager sManager;
    private int screenX,screenY;
    private Sensor accelerometer;
    private boolean isPressed=false;
    private Bitmap shipShoot;
    private Random random;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    boolean isGameOver;
    private Bitmap pause;
    private int gameCounter=0;
    private int gemCounter=0;
    private Paint text;
    private int enemyCounter;
    private boolean isPowerup=false,isPoweringup=false;
    private Bitmap shipPowerup,shipShootPowerup;
    private Bitmap powerup;
    private int xPoweup,yPowerup,speedPowerup=20;

    public ViewTravel(GameActivityPortrait activity, short flag,int screenY,int screenX) {

        super(activity);
        this.gameActivityPortrait=activity;
        this.screenX=screenX;
        this.screenY=screenY;

        //timerEnemies=new TimerEnemies(ViewTravel.this);
        paint=new Paint();

        text=new Paint();
        text.setColor(Color.WHITE);
        text.setTextSize(50);
        text.setTextAlign(Paint.Align.CENTER);

        background1= BitmapFactory.decodeResource(getResources(), R.drawable.travel_background);
        background2= BitmapFactory.decodeResource(getResources(), R.drawable.travel_background);

        background1=Bitmap.createScaledBitmap(background1,screenX,screenY,false);
        background2=Bitmap.createScaledBitmap(background2,screenX,screenY,false);

        powerup=BitmapFactory.decodeResource(getResources(),R.drawable.powerup);
        powerup=Bitmap.createScaledBitmap(powerup,powerup.getWidth()/2,powerup.getHeight()/2,false);

        pause=BitmapFactory.decodeResource(getResources(),R.drawable.pause);
        pause=Bitmap.createScaledBitmap(pause,pause.getWidth()/45,pause.getHeight()/45,false);

        yBackground2=-screenY;  //inizializza il secondo background sopra il primo

        if(flag==0){
            ship=BitmapFactory.decodeResource(getResources(),R.drawable.ship_astronaut);
            shipShoot=BitmapFactory.decodeResource(getResources(),R.drawable.ship_shoot);
            //shipPowerup=BitmapFactory.decodeResource(getResources(),R.drawable.ship_powerup);
            //shipShootPowerup=BitmapFactory.decodeResource(getResources(),R.drawable.ship_shoot_powerup);
        }
        else if(flag==1){
            //TO DO: CARICARE LA BITMAP DELLA NAVICELLA CON IL ROVER
        }

        ship=Bitmap.createScaledBitmap(ship,ship.getWidth()/7,ship.getHeight()/7,false);
        shipShoot=Bitmap.createScaledBitmap(shipShoot,shipShoot.getWidth()/7,shipShoot.getHeight()/7,false);
        //shipPowerup=Bitmap.createScaledBitmap(shipPowerup,shipPowerup.getWidth()/2,shipPowerup.getHeight()/2,false);
        //shipShootPowerup=Bitmap.createScaledBitmap(shipShootPowerup,shipShootPowerup.getWidth()/7,shipShootPowerup.getHeight()/7,false);
        xShip=(screenX/2)-(ship.getWidth()/2);
        yShip=(int)(screenY-10*screenRatioY-ship.getHeight());

        bullets=new ArrayList<>();

        enemies=new ArrayList<>();

        random=new Random();

        // imposta accelerometro per input
        sManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    public void run() {
        while (isPlaying&&gameCounter<2166){   //il gioco si ferma all'incirca dopo 1 minuto e mezzo di gioco
            //timer.schedule(new TimerEnemies(ViewTravel.this),0,20000);
            update();
            draw();
            sleep(36);
            gameCounter++;
        }
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void generateEnemies(){
        enemyCounter=random.nextInt(4)+1; //sceglie casualmente quanti nemici aggiungere allo schermo

        for(int i=0; i<enemyCounter;i++){
            Enemy enemy=new Enemy(getResources(),screenX);
            enemy.getEnemy(); //genera casualmente un nemico per ogni elemento dell'array
            enemy.y=0;
            enemy.x=random.nextInt(screenX-enemy.width); //l'asse delle x viene scelto randomicamente
            enemies.add(enemy);
        }

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

    private void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas=getHolder().lockCanvas();
            canvas.drawBitmap(background1,xBackground1,yBackground1,paint);
            canvas.drawBitmap(background2,xBackground2,yBackground2,paint);
            if(isPowerup){
                canvas.drawBitmap(powerup,0,0,paint);
            }
            canvas.drawText(""+gemCounter,canvas.getWidth()/2,30*screenRatioY,text);
            canvas.drawBitmap(pause,screenX-pause.getWidth()-10*screenRatioX,10*screenRatioX,paint);
            for(Enemy enemy:enemies){
                canvas.drawBitmap(enemy.enemy,enemy.x,enemy.y,paint);
                if(enemy.gemShot){
                    canvas.drawBitmap(enemy.gem,enemy.xGem,enemy.yGem,paint);
                }
            }
            canvas.drawBitmap(getShip(),xShip,yShip,paint);
            for(Bullet bullet:bullets){
                canvas.drawBitmap(bullet.bullet,bullet.x,bullet.y,paint);
            }
            if(gameCounter>2166){
                //TO DO:CALLING VIEW LUNA
                getHolder().unlockCanvasAndPost(canvas);
            }
            if(isGameOver){ //se hai perso
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
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void waitBeforeExiting(){
        try {
            Thread.sleep(3000);
            gameActivityPortrait.startActivity(new Intent(gameActivityPortrait, HallActivity.class));
            gameActivityPortrait.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void update(){
        yBackground1+=10*screenRatioY;
        yBackground2+=10*screenRatioY;
        if(yBackground1>screenY){ //se il background è sceso fuori dallo schermo
            yBackground1=-screenY;
        }
        if(yBackground2>screenY){ //se il background è sceso fuori dallo schermo
            yBackground2=-screenY;
        }

        if(gameCounter%100==0){
            generateEnemies();
            generatePowerup();
        }

        List<Bullet> trash= new ArrayList<>();
        List<Enemy> enemiesTrash=new ArrayList<>();

        for(Bullet bullet:bullets) {
            if (bullet.y < 0)   //questo vuol dire che il proiettile è nello schermo va aggiunto alla lista dei proiettili trash
                trash.add(bullet);
            bullet.y -= 50 * screenRatioY;  //muovere il proiettile di 50 pixel

            if(!enemies.isEmpty()){
                for(Enemy enemy:enemies){
                    if(Rect.intersects(enemy.getCollisionShape(),bullet.getCollisionShape())){ //se un proiettile colpisce l'asteroide
                        if(enemy.isGem){
                            enemy.gemShot=true;
                            enemy.xGem=enemy.x+enemy.enemy.getWidth()/2-enemy.gem.getWidth()/2;
                            enemy.yGem=enemy.y+enemy.enemy.getHeight()/2-enemy.gem.getHeight()/2;
                        }
                        enemy.y-=screenY+500; //l'asteroide si sposta di 500+screenY
                        bullet.y=-500;
                    }
                }
            }

        }

        for(Bullet bullet:trash){
            bullets.remove(bullet);
        }

        if(isPowerup&&yPowerup<=screenY){   //controllo per la velocità casuale del power up
            yPowerup+=speedPowerup;
            int bound=(int)(30*screenRatioY);
            speedPowerup=random.nextInt(bound);
            if(speedPowerup<10*screenRatioY){
                speedPowerup=(int) (10*screenRatioY);
            }
        }
        else if(yPowerup>=screenY){
            isPowerup=false;
        }

        if(!enemies.isEmpty()){
            for(Enemy enemy:enemies){ //per ogni asteroide
                enemy.y+=enemy.speed; //assegna alla y una certa velocità
                if(enemy.y<screenY){  //finchè l'asteroide rimane sullo schermo aumenta la sua posizione lungo l'asse delle y
                    int bound=(int) (30*screenRatioY);
                    enemy.speed=random.nextInt(bound);
                    if(enemy.speed<10*screenRatioY){
                        enemy.speed=(int)(10*screenRatioY);
                    }
                }
                else{
                    //enemy.y=0; //l'asse delle y rimane lo stesso da cui spaunare
                    //enemy.x=random.nextInt(screenX-enemy.width); //l'asse delle x viene scelto randomicamente
                    enemiesTrash.add(enemy);
                }
                if(Rect.intersects(enemy.getCollisionShape(),getCollisionShape())){
                    isGameOver=true;
                    return;
                }
                if(enemy.isGem&&enemy.gemShot&&enemy.yGem<=screenY){ //se l'elemento dell'array list era un asteroide con gemma ed è stato colpito quindi c'è la gemma sullo schermo e non è ancora scomparsa
                    enemy.yGem+=enemy.speed;
                    if(Rect.intersects(enemy.getCollisionShapeGem(),getCollisionShape())){
                        gemCounter++;
                        enemy.yGem+=screenY; //fa scomparire la gemma
                    }
                }
                else{  //quando la gemma scompare dallo schermo la variabile torna falsa così da non farla disegnare più
                    enemy.gemShot=false;
                }

            }
        }

        if(!enemiesTrash.isEmpty()){
            for(Enemy enemy:enemiesTrash){
                enemies.remove(enemy);
            }
        }
    }

    Rect getCollisionShape(){   //definisco i margini di collisione dell'immagine
        return new Rect(xShip,yShip,xShip+getShip().getWidth(),yShip+getShip().getHeight());
    }

    public void resume(){
        accelerometerReset();
        isPlaying=true;
        thread=new Thread(this);
        thread.start();
    }

    public void pause(){
        accelerometerOut();
        try {
            isPlaying=false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){  //se l'input proviene dall'accellerometro
            xShip=(int)(xShip-sensorEvent.values[0]-sensorEvent.values[0]);
            if(xShip+sensorEvent.values[0]+ship.getWidth()>screenX-10*screenRatioX){  //la navicella si sposta verso destra se rimane in questo range
                xShip=(int)(screenX-10*screenRatioX-ship.getWidth());
            }
            else if(xShip-sensorEvent.values[0]<0){ //la navicella si sposta verso sinistra se rimane in questo range
                xShip=(int)(10*screenRatioX);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }
    //stacca l'acelerometro, dopo che va in onPause
    public void accelerometerOut() {
        sManager.unregisterListener(this);
    }

    // ri-attacca l'acelerometro, dopo che va in onResume
    public void accelerometerReset() {
        sManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(event.getX()>screenX-pause.getWidth()-10*screenRatioX&&event.getY()<10*screenRatioX+pause.getHeight()){
                    //se il tocco avviene nel quadrato in cui si trova il bottone di pausa
                    short flag=0,flagActivity=0;
                    gameActivityPortrait.callManager(flag,flagActivity);
                }
                isPressed=true;
                setBullet();
                break;
            case MotionEvent.ACTION_UP:
                isPressed=false;
                break;
        }
        return true;
    }

    private Bitmap getShip(){
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

    public void setBullet(){

        Bullet bullet=new Bullet(getResources());
        bullet.x=xShip+shipShoot.getWidth()/2-bullet.bullet.getWidth()/2;
        bullet.y=screenY-shipShoot.getHeight()-bullet.bullet.getHeight();
        bullets.add(bullet);

    }
}
