package com.example.casodistudio.game.gameviews.LViews;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.Toast;

import com.example.casodistudio.HallActivity;
import com.example.casodistudio.R;


public class ViewMuseum extends SurfaceView implements Runnable {

    private Thread thread;
    private Bitmap museum_background = BitmapFactory.decodeResource(getResources(), R.drawable.background_museo);
    private Bitmap floor;
    private Bitmap pause,apollo11,roverMars;
    private Toast toast;
    private int width,height;
    private int widthF,heightF;// altezza e lunghezza del pavimento
    public int x=0;
    protected int screenX, screenY;
    private HallActivity hallactivity;
    public static float screenRatioX, screenRatioY;
    public boolean isPlaying;
    private Paint paint;
    private Bitmap character; // hiroki fermo
    private Bitmap hiroki1;
    private Bitmap hiroki2;
    int counter=1;
    boolean isPressed=false;
    boolean isRight=true;
    boolean isJumping=false;
    boolean wasMirroredLeft=false,wasMirroredRight=true;
    boolean isSwitching=false;
    boolean isEndFirstLevel=false;
    boolean isEndTravelToMars=false, isEndTravelToMoon=false;
    private Bitmap interfaceBackground;

    private float charX, charY; // Dove viene spawnato il personaggio


    Rect rectGround;


    public ViewMuseum(HallActivity activity, int screenX, int screenY) {
        super(activity);

        //super.background = new Backgrounds(getResources(),super.screenX,super.screenY);
        this.hallactivity=activity;
        this.screenX=screenX;
        this.screenY=screenY;
        screenRatioX=1920f/screenX;
        screenRatioY=1080F/screenY;
        paint=new Paint();
        //rectBackground = new Rect(0,0,screenX,screenY);



        width = museum_background.getWidth();
        height = museum_background.getHeight();

        museum_background = Bitmap.createScaledBitmap(museum_background,screenX,screenY,false); //lo screen x deve essere uguale allo schermo

        float density = getResources().getDisplayMetrics().density;
        if (density >= 4.0)
        {
            floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
        }else if (density >= 3.0) {
            floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
        }else if (density >= 2.0) {
            floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
        }else if (density >= 1.5) {
            floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);
        }else if (density >= 1.0) {
            floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);

        }else{ floor= BitmapFactory.decodeResource(getResources(), R.drawable.floor_museo);}



        floor= BitmapFactory.decodeResource(getResources(), R.drawable.pavimento_museo);
        floor=Bitmap.createScaledBitmap(floor,screenX+2,floor.getHeight(),false);

        character = BitmapFactory.decodeResource(getResources(), R.drawable.hirooki_fermo);
        hiroki1 = BitmapFactory.decodeResource(getResources(), R.drawable.hirooki1);
        hiroki2 = BitmapFactory.decodeResource(getResources(), R.drawable.hirooki2);

        character = Bitmap.createScaledBitmap(character,character.getWidth()/7, character.getHeight()/7,false);
        hiroki1 = Bitmap.createScaledBitmap(hiroki1,hiroki1.getWidth()/7, hiroki1.getHeight()/7,false);
        hiroki2 = Bitmap.createScaledBitmap(hiroki2,hiroki2.getWidth()/7, hiroki2.getHeight()/7,false);
        pause=BitmapFactory.decodeResource(getResources(),R.drawable.pause);
        pause=Bitmap.createScaledBitmap(pause,pause.getWidth()/50,pause.getHeight()/50,false);

        apollo11=BitmapFactory.decodeResource(getResources(),R.drawable.apollo11);
        roverMars=BitmapFactory.decodeResource(getResources(),R.drawable.teca_rover);
        apollo11=Bitmap.createScaledBitmap(apollo11,apollo11.getWidth()/2,apollo11.getHeight()/2,false);
        roverMars=Bitmap.createScaledBitmap(roverMars,roverMars.getWidth()/2,roverMars.getHeight()/2,false);

        interfaceBackground=BitmapFactory.decodeResource(getResources(),R.drawable.sfondo_interfacce);
        interfaceBackground=Bitmap.createScaledBitmap(interfaceBackground,-screenX,-screenY,false);

        widthF= (int) (floor.getWidth() * screenRatioX);
        heightF=(int)(floor.getHeight() * screenRatioY);

        width*=(int) Resources.getSystem().getDisplayMetrics().density;
        height*=(int) Resources.getSystem().getDisplayMetrics().density;
        //floor = Bitmap.createScaledBitmap(floor,widthF,heightF,false); //serve solo per allungare il pavimento

       // character = Bitmap.createScaledBitmap(character,character.getWidth()/8, character.getHeight()/8,false);
        charY= screenY - floor.getHeight() - character.getHeight();
        charX = 60;
        rectGround = new Rect(0,screenX-floor.getHeight(),screenX, screenY);
    }


    protected void draw(){
        if(getHolder().getSurface().isValid()){  //se il nostro oggetto surface è stato correttamente istanziato
            Canvas canvas=getHolder().lockCanvas(); //blocca il canva per poterlo usare
            canvas.drawBitmap(museum_background,0,0,paint);
            canvas.drawBitmap(roverMars,screenX-roverMars.getWidth()-200*screenRatioX,screenY-floor.getHeight()-roverMars.getHeight()+5,paint);
            canvas.drawBitmap(floor,0,screenY-floor.getHeight(),paint);
            canvas.drawBitmap(apollo11,200*(int) screenRatioX,0,paint);
            canvas.drawBitmap(getCharacter(),getX(),screenY-floor.getHeight()-character.getHeight(),paint); //screenY-floor.getHeight()-character.getHeight()
            canvas.drawBitmap(pause,screenX-pause.getWidth()-10*screenRatioX,10*screenRatioX,paint);
            if(isSwitching){
                canvas.drawBitmap(interfaceBackground,0,0,paint);
            }
            getHolder().unlockCanvasAndPost(canvas);  //dopo aver disegnato le bitmap sblocca il canvas
        }
    }

    @Override
    public void run() {
        while (isPlaying){
            draw();
            sleep(36); //setta la pausa del thread ogni 36 milli secondi così che il gioco runni a 36fps
        }
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
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


    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(event.getX()>screenX-pause.getWidth()-10*screenRatioX&&event.getY()<10*screenRatioX+pause.getHeight()){
                    //se il tocco avviene nel quadrato in cui si trova il bottone di pausa
                    short flag=0;
                    hallactivity.callManager(flag);
                    //activityLandscape.callPauseFragment(); dovrebbe chiamare il fragment di pausa del gioco ma non so se si possa fare
                }
               if(event.getX()>200*(int) screenRatioX&&event.getX()<200*(int) screenRatioX+apollo11.getWidth()&&event.getY()>0&&event.getY()<apollo11.getHeight()){
                   if(!isEndTravelToMoon){ //se non si è ancora completati il viaggio verso la luna parte direttamente l'activity portrait
                       short c=0;
                       //isSwitching=true;  //DA RIVEDERE PERCHE'SE L'UTENTE CLICCA INDIETRO SI BLOCCA
                       //sleep(4000);  //imposta questo tempo per far comparire l'interfaccia con la storia e poi passare all'activity per il viaggio
                       //isSwitching=false;
                       hallactivity.callTravel(c); //passa 0 come flag perché sta andando verso la luna
                       //quando tocchi sulla teca dell'apollo 11 richiamare l'activity poltrait
                   }
                   else{
                       //TO DO: IMPOSTARE LA SCELTA TRA IL VIAGGIO VERSO LA LUNA E ARRIVARE DIRETTAMENTE SULLA LUNA
                   }

                }
               if((event.getX()>screenX-roverMars.getWidth()-200*screenRatioX)&&(event.getX()<screenX-200*screenRatioX)&&event.getY()>screenY-floor.getHeight()-roverMars.getHeight()+5&&event.getY()<screenY-floor.getHeight()+5){ //
                   if(!isEndFirstLevel){ //se il primo livello non è ancora finito compare la scritta che il livello è bloccato
                       Toast.makeText(hallactivity, "Livello bloccato", Toast.LENGTH_SHORT).show();
                   }
                   else{
                       if(!isEndTravelToMars){
                           short c=1;
                           hallactivity.callTravel(c);
                       }
                       else{
                           //TO DO: IMPOSTARE LA SCELTA TRA IL VIAGGIO VERSO LA MARTE E ARRIVARE DIRETTAMENTE SU MARTE
                       }
                   }

                   /*short c=1;
                   activityLandscape.callTravel(c);*/ //passa 1 come flag perché sta andando verso Marte
               }
                //fare l'if per il tocco sul sulla teca di marte
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


    public Bitmap getCharacter(){
        if(isRight&&wasMirroredLeft){ //se l'utente sta premendo per spostarsi verso destra e la bitmap è flippata restituisce la bitmap normale
            character=Bitmap.createScaledBitmap(character,-(character.getWidth()),character.getHeight(),false);
            hiroki1=Bitmap.createScaledBitmap(hiroki1,-(hiroki1.getWidth()),hiroki1.getHeight(),false);
            hiroki2=Bitmap.createScaledBitmap(hiroki2,-(hiroki2.getWidth()),hiroki2.getHeight(),false);
            wasMirroredRight=true;
            wasMirroredLeft=false;
        }
        else if((!isRight)&&wasMirroredRight){  //altrimenti l'utente sta premendo per spostarsi verso sinistra e la bitmap è normale la restituisce flippata
            character=Bitmap.createScaledBitmap(character,-(character.getWidth()),character.getHeight(),false);
            hiroki1=Bitmap.createScaledBitmap(hiroki1,-(hiroki1.getWidth()),hiroki1.getHeight(),false);
            hiroki2=Bitmap.createScaledBitmap(hiroki2,-(hiroki2.getWidth()),hiroki2.getHeight(),false);
            wasMirroredLeft=true;
            wasMirroredRight=false;
        }
        if(isPressed){
            if(counter==1){
                counter++;
                return hiroki1;
            }
            if(counter==2){
                counter++;
                return hiroki2;
            }
            counter--;
        }
        return character;
    }


    public float getX(){
        if(isPressed){  //se il background è arrivato alla fine
            if(isRight&&(x<screenX-character.getWidth()-10*screenRatioX)){ //se l'utente sta premendo verso destra //e se x è minore di questo punto sull'asse delle x
                x+= (int) (10*screenRatioX); //aumenta ancora la x finchè il personaggio non arriva alla fine dello schermo quindi isEnd diventa false
            }
            else if((!isRight)&&(x>10*screenRatioX)){ //se l'utente sta premendo verso sinistra
                x-= (int) (10*screenRatioX); //diminuisce ancora la x finchè il personaggio non arriva alla fine dello schermo quindi isEnd diventa false
            }
        }
        return x;
    }
    /*public void getY(){
        if(isJumping){
            int i;
            for(i=0;i<3;i++){
                charY+=10*screenRatioX;
            }
            for(int j=i;j>=0; j--){
                charY-=10*screenRatioX;
            }
        }
        //return charY;
    }*/
}
