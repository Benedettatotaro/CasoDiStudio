package com.example.casodistudio.game.gameviews;

import static com.example.casodistudio.game.gameviews.LViews.ViewMuseum.screenRatioX;
import static com.example.casodistudio.game.gameviews.LViews.ViewMuseum.screenRatioY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.casodistudio.game.gameviews.PViewClass.Background;
import com.example.casodistudio.game.gameviews.PViewClass.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ViewTravel extends SurfaceView implements Runnable, SensorEventListener {

    private Thread thread;
    private boolean isPlaying=true;
    private GameActivityPortrait gameActivityPortrait;
    private Paint paint;
    private SensorManager sManager;
    public int screenX,screenY;
    private Sensor accelerometer;
    private boolean isPressed=false;
    public Random random;
    public List<Bullet> bullets;
    public List<Enemy> enemies;
    boolean isGameOver;
    private Bitmap pause;
    public int gameCounter=0;
    public int gemCounter=0;
    private Paint text;
    private int enemyCounter;
    public Background background_1,background_2;
    public Ship ship;
    private SharedPreferences prefs;
    short flagPlanet;
    //private Update update;

    public ViewTravel(GameActivityPortrait activity, short flag,int screenY,int screenX) {

        super(activity);
        this.flagPlanet =flag;
        this.gameActivityPortrait=activity;
        this.screenX=screenX;
        this.screenY=screenY;

        prefs=activity.getSharedPreferences("game",activity.MODE_PRIVATE); // scrive nel DB del telefono

        paint=new Paint();

        text=new Paint();
        text.setColor(Color.WHITE);
        text.setTextSize(50);
        text.setTextAlign(Paint.Align.CENTER);

        //update=new Update(ViewTravel.this);

        background_1=new Background(getResources(),screenX,screenY);
        background_2=new Background(getResources(),screenX,screenY);

        pause=BitmapFactory.decodeResource(getResources(),R.drawable.pause);
        pause=Bitmap.createScaledBitmap(pause,pause.getWidth()/45,pause.getHeight()/45,false);

        background_2.y=-screenY;  //inizializza il secondo background sopra il primo

        ship=new Ship(getResources(),screenX,screenY,flag);

        bullets=new ArrayList<>();

        enemies=new ArrayList<>();

        random=new Random();

        // imposta accelerometro per input
        sManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    public void run() {
            while (isPlaying){   //il gioco si ferma all'incirca dopo 1 minuto e mezzo di gioco
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
        if(!ship.isPowerup){  //se non ci sono altri power up sullo schermo
            int i=random.nextInt(10)+1;
            if(i==5){
                ship.isPowerup=true; //se la random genera 5 in un range da 1 a 10 genera il powerup
                ship.yPowerup=0;
                ship.xPoweup=random.nextInt(screenX-ship.powerup.getWidth());
            }
        }
    }

    private void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas=getHolder().lockCanvas();
            canvas.drawBitmap(background_1.background,background_1.x,background_1.y,paint);
            canvas.drawBitmap(background_2.background,background_2.x,background_2.y,paint);
            if(ship.isPowerup){
                canvas.drawBitmap(ship.powerup,ship.xPoweup,ship.yPowerup,paint);
            }
            canvas.drawText(""+gemCounter,canvas.getWidth()/2,30*screenRatioY,text);
            canvas.drawBitmap(pause,screenX-pause.getWidth()-10*screenRatioX,10*screenRatioX,paint);
            for(Enemy enemy:enemies){
                canvas.drawBitmap(enemy.enemy,enemy.x,enemy.y,paint);
                if(enemy.gemShot){
                    canvas.drawBitmap(enemy.gem,enemy.xGem,enemy.yGem,paint);
                }
            }

            canvas.drawBitmap(ship.getShip(),ship.xShip,ship.yShip,paint);
            if(!bullets.isEmpty()){
                for(Bullet bullet:bullets){
                    canvas.drawBitmap(bullet.bullet,bullet.x,bullet.y,paint);
                }
            }


            if(gameCounter>5){  //se il gioco finisce salva i dati delle gemme
                //SCRIVERE SE E' LOGGATO E IN QUEL CASO SALVARE I DATI E SE è CONNESSO AD INTERNET SCRIVERLI ANCHE SU FIREBASE
                SharedPreferences.Editor editor= prefs.edit();
                editor.putInt("moonGem",gemCounter);
                editor.putBoolean("moonFinished",true); //imposta che il livello della luna è finito
                editor.apply();
                //sleep(10000);
                getHolder().unlockCanvasAndPost(canvas);
                gameActivityPortrait.callPlanet(flagPlanet);
                return;

                //TO DO: CHIAMARE L'ACTIVITY DELLA LUNA
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
        background_1.y+=10*screenRatioY;
        background_2.y+=10*screenRatioY;
        if(background_1.y>screenY){ //se il background è sceso fuori dallo schermo
            background_1.y=-screenY;
        }
        if(background_2.y>screenY){ //se il background è sceso fuori dallo schermo
            background_2.y=-screenY;
        }

        if(gameCounter%100==0){
            generateEnemies();
            generatePowerup();
        }

        List<Bullet> trash= new ArrayList<>();
        List<Enemy> enemiesTrash=new ArrayList<>();
        if(!bullets.isEmpty()) {
            for (Bullet bullet : bullets) {
                if (bullet.y < 0)   //questo vuol dire che il proiettile è nello schermo va aggiunto alla lista dei proiettili trash
                    trash.add(bullet);
                bullet.y -= 50 * screenRatioY;  //muovere il proiettile di 50 pixel

                if (!enemies.isEmpty()) {
                    for (Enemy enemy : enemies) {
                        if (Rect.intersects(enemy.getCollisionShape(), bullet.getCollisionShape())) { //se un proiettile colpisce l'asteroide
                            if (enemy.isGem) {
                                enemy.gemShot = true;
                                enemy.xGem = enemy.x + enemy.enemy.getWidth() / 2 - enemy.gem.getWidth() / 2;
                                enemy.yGem = enemy.y + enemy.enemy.getHeight() / 2 - enemy.gem.getHeight() / 2;
                            }
                            enemy.y -= screenY + 500; //l'asteroide si sposta di 500+screenY
                            bullet.y = -500;
                        }
                    }
                }

            }
        }
        for(Bullet bullet:trash){
            bullets.remove(bullet);
        }

        if(ship.isPowerup&&ship.yPowerup<=screenY){   //controllo per la velocità casuale del power up
            ship.yPowerup+=ship.speedPowerup;
            int bound=(int)(30*screenRatioY);
            ship.speedPowerup=random.nextInt(bound);
            if(ship.speedPowerup<10*screenRatioY){
                ship.speedPowerup=(int) (10*screenRatioY);
            }
            if(Rect.intersects(ship.getCollisionShapePowerUp(),ship.getCollisionShape())){  //se la navicella colpisce il powerup
                ship.isPoweringup=true; //cambia la bitmap della navicella
                ship.yPowerup+=screenY;
                ship.counterPouwerup=gameCounter; //inizializza il contatore del powerup
            }
        }

        if(ship.isPoweringup&&gameCounter>=ship.counterPouwerup+200){  //se il contatore di gioco incrementa di 833 cioè all'incirca 30 secondi
            ship.isPoweringup=false;  //termina il power up
        }

        else if(ship.isPowerup&&ship.yPowerup>=screenY){  //se il power up esce dallo schermo la variabile cambia
            ship.isPowerup=false;
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
                    enemiesTrash.add(enemy);
                }
                if(Rect.intersects(enemy.getCollisionShape(),ship.getCollisionShape())){
                    if(ship.isPoweringup){  //se è il powerup non perdi
                        if(enemy.isGem){
                            enemy.gemShot=true;
                            enemy.xGem=enemy.x+enemy.enemy.getWidth()/2-enemy.gem.getWidth()/2;
                            enemy.yGem=enemy.y+enemy.enemy.getHeight()/2-enemy.gem.getHeight()/2;
                        }
                        enemy.y-=screenY+500; //l'asteroide si sposta di 500+screenY
                    }
                    else{
                        isGameOver=true;
                        return;
                    }
                }
                if(enemy.isGem&&enemy.gemShot&&enemy.yGem<=screenY){ //se l'elemento dell'array list era un asteroide con gemma ed è stato colpito quindi c'è la gemma sullo schermo e non è ancora scomparsa
                    enemy.yGem+=enemy.speed;
                    if(Rect.intersects(enemy.getCollisionShapeGem(),ship.getCollisionShape())){
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
            ship.xShip=(int)(ship.xShip-sensorEvent.values[0]-sensorEvent.values[0]);
            if(ship.xShip+sensorEvent.values[0]+ship.getShip().getWidth()>screenX-10*screenRatioX){  //la navicella si sposta verso destra se rimane in questo range
                ship.xShip=(int)(screenX-10*screenRatioX-ship.getShip().getWidth());
            }
            else if(ship.xShip-sensorEvent.values[0]<0){ //la navicella si sposta verso sinistra se rimane in questo range
                ship.xShip=(int)(10*screenRatioX);
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

    public void setBullet()
    {

        Bullet bullet=new Bullet(getResources());
        bullet.x=ship.xShip+ship.shipShoot.getWidth()/2-bullet.bullet.getWidth()/2;
        bullet.y=screenY-ship.shipShoot.getHeight()-bullet.bullet.getHeight();
        bullets.add(bullet);

    }
}
