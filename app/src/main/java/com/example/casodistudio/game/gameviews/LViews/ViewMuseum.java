package com.example.casodistudio.game.gameviews.LViews;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.casodistudio.GameActivityLandscape;
import com.example.casodistudio.GameActivityPortrait;
import com.example.casodistudio.HallActivity;
import com.example.casodistudio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;


public class ViewMuseum extends SurfaceView implements Runnable {

    private Thread thread;
    private Bitmap museum_background = BitmapFactory.decodeResource(getResources(), R.drawable.background_museo);
    private Bitmap floor;
    private Bitmap pause,apollo11,roverMars;
    private Bitmap ranking;
    public int x=0;
    public int screenX, screenY;
    int distance;
    private HallActivity hallactivity;
    public static float screenRatioX, screenRatioY;
    public boolean isPlaying;
    private Paint paint;
    boolean isSwitching;
    private Bitmap interfaceBackground;
    private SharedPreferences prefs;
    private Bitmap gem;
    private long gemTot;
    private Bitmap login;
    Rect rectGround;
    Character hiroki;
    FirebaseFirestore db;
    private String email;
    Toast toast;


    public ViewMuseum(HallActivity activity, int screenX, int screenY) {
        super(activity);
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        prefs=activity.getSharedPreferences("game",activity.MODE_PRIVATE);


        //prefs.edit().clear().commit();  //per pulire il prefs



        email = prefs.getString("email", "").toString();

        this.hallactivity=activity;
        this.screenX=screenX;
        this.screenY=screenY;
        screenRatioX=1920F/screenX;
        screenRatioY=1080F/screenY;
        paint=new Paint();
        hiroki = new Character(2,getResources(),screenX,screenY,screenRatioX,screenRatioY);



        if( prefs.getString("email", "") != "")
        {
            if(connectivityManager.getActiveNetwork() != null )
            {

                db = FirebaseFirestore.getInstance();
                Map<String, Object> appoggio = (Map<String, Object>) prefs.getAll();
                appoggio.remove("email","");
                db.collection("gems").document(prefs.getString("email", "")).set(appoggio).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                         toast = Toast.makeText(getContext(), R.string.Failed_update, Toast.LENGTH_SHORT);
                         toast.show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        toast = Toast.makeText(getContext(), R.string.Account_succ, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });



            }else
            {
                toast = Toast.makeText(getContext(), R.string.Off, Toast.LENGTH_SHORT);
                toast.show();

            }


        }else
        {
             toast = Toast.makeText(getContext(), R.string.Guest, Toast.LENGTH_SHORT);
            toast.show();


        }

        gemTot = prefs.getLong("moonGem", 0) + prefs.getLong("marsGem", 0);

            int prova1 = prefs.getInt("gameCounter", 0);
            int prova2 = prefs.getInt("xPosition",0);
            int prova3 = prefs.getInt("flagLevel", -1);
            int prova = prefs.getInt("",0);







        museum_background = Bitmap.createScaledBitmap(museum_background,screenX,screenY,false); //lo screen x deve essere uguale allo schermo

        isSwitching=false;

        floor= BitmapFactory.decodeResource(getResources(), R.drawable.pavimento_museo);
        floor=Bitmap.createScaledBitmap(floor,screenX+2,floor.getHeight(),false);

        pause=BitmapFactory.decodeResource(getResources(),R.drawable.pause);
        pause=Bitmap.createScaledBitmap(pause,pause.getWidth()/45,pause.getHeight()/45,false);

        login=BitmapFactory.decodeResource(getResources(),R.drawable.login);
        login=Bitmap.createScaledBitmap(login,login.getWidth()/45,login.getHeight()/45,false);

        apollo11=BitmapFactory.decodeResource(getResources(),R.drawable.apollo11);
        roverMars=BitmapFactory.decodeResource(getResources(),R.drawable.teca_rover);
        apollo11=Bitmap.createScaledBitmap(apollo11,apollo11.getWidth()/2,apollo11.getHeight()/2,false);
        roverMars=Bitmap.createScaledBitmap(roverMars,roverMars.getWidth()/2,roverMars.getHeight()/2,false);

        interfaceBackground=BitmapFactory.decodeResource(getResources(),R.drawable.history_background);
        interfaceBackground=Bitmap.createScaledBitmap(interfaceBackground,-screenX,-screenY,false);

        gem=BitmapFactory.decodeResource(getResources(),R.drawable.gem);
        gem=Bitmap.createScaledBitmap(gem,gem.getWidth()/2,gem.getHeight()/2,false);

        ranking = BitmapFactory.decodeResource(getResources(), R.drawable.ranking);
        ranking = Bitmap.createScaledBitmap(ranking,ranking.getWidth()/10,ranking.getHeight()/10,false);


        //charY= screenY - floor.getHeight() - character.getHeight();
        //charX = 60;
        rectGround = new Rect(0,screenX-floor.getHeight(),screenX, screenY);
    }




    protected void draw(){
        if(getHolder().getSurface().isValid()){  //se il nostro oggetto surface è stato correttamente istanziato
            Canvas canvas=getHolder().lockCanvas(); //blocca il canva per poterlo usare
            canvas.drawBitmap(museum_background,0,0,paint);
            canvas.drawBitmap(roverMars,canvas.getWidth()/2+300,screenY-floor.getHeight()-roverMars.getHeight()+5,paint);
            canvas.drawBitmap(floor,0,screenY-floor.getHeight(),paint);
            canvas.drawBitmap(apollo11,canvas.getWidth()/2-300-apollo11.getWidth(),0,paint);
            distance = canvas.getWidth();
            canvas.drawBitmap(hiroki.charOrientation(),hiroki.charMovement(),screenY-floor.getHeight()-hiroki.getStopAnimation().getHeight(),paint); //screenY-floor.getHeight()-character.getHeight()
            //canvas.drawBitmap(ranking,screenX-ranking.getWidth()-10*screenRatioX,10*screenRatioY,paint);
            canvas.drawBitmap(ranking,canvas.getWidth()/2-ranking.getWidth()/2,0,paint);
            canvas.drawBitmap(pause,screenX-pause.getWidth()-10*screenRatioX,10*screenRatioX,paint);
            canvas.drawBitmap(login,10*screenRatioX,screenY-10*screenRatioY-login.getHeight(),paint);
            canvas.drawBitmap(gem,10*screenRatioX,10*screenRatioY,paint);
            Paint text=new Paint();
            text.setColor(Color.WHITE);
            text.setTextSize(gem.getHeight());
            canvas.drawText(gemTot+"",10*screenRatioX+gem.getWidth()+10,gem.getHeight()+5*screenRatioX,text);
            if(isSwitching){  //se sta passando all'activity portrait chiama il fragment con la storia
                short c=0;
                canvas.drawBitmap(interfaceBackground,0,0,paint);
                TextPaint textPaint = new TextPaint();
                //textPaint.setTextAlign(Paint.Align.CENTER);

                textPaint.setColor(Color.WHITE);
                textPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
                String message="Durante gli anni della guerra fredda, tra stati uniti e " +  //da aggiustare FA SCHIFO!!!!
                        "unione sovietica uno dei tanti obiettivi contesi era il primo viaggio con equipaggio verso la Luna. " +
                        "Il giorno 20 luglio 1969 tre astronauti statunitensi sono partiti per la missione spaziale Apollo 11 per poi " +
                        "atterrare sul suolo lunare il giorno dopo. Aiuta la navicella a raggiungere la luna come l’equipaggio fece quel giorno.";
                StaticLayout.Builder builder=StaticLayout.Builder.obtain(message,0,message.length(), textPaint,700)
                        .setAlignment(Layout.Alignment.ALIGN_NORMAL);

                StaticLayout staticLayout=builder.build();


                int xPos = (canvas.getWidth() / 2) - (staticLayout.getWidth()/2);
                int yPos = (int) ((canvas.getHeight() / 2) - (staticLayout.getHeight()/2));
                canvas.save();
                canvas.translate(xPos, yPos);
                staticLayout.draw(canvas);
                canvas.restore();
                getHolder().unlockCanvasAndPost(canvas);  //dopo aver disegnato le bitmap sblocca il canvas
                sleep(7000);  //imposta questo tempo per far comparire l'interfaccia con la storia e poi passare all'activity per il viaggio
                hallactivity.callTravel(c); //passa 0 come flag perché sta andando verso la luna
                isSwitching = false;
                return;
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
            hallactivity.finish();
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
            hallactivity.finish();
            e.printStackTrace();
        }

    }

    public void checkLevelStatus( short flag){

        if(prefs.getInt("gameCounter", 0) != 0)// controllo se si sta riferendo al portrait
        {
                hallactivity.callTravel(flag); //passa il flag per chiamare il viaggo
                return;

        }else if(prefs.getInt("xPosition",0) != 0)// o landscape
        {
                Bundle bundle=new Bundle(1);
                bundle.putShort("flagPlanet", flag); //setta il flag nel boundle per chiamare il pianeta
                Intent i = new Intent(getContext(), GameActivityLandscape.class);
                i.putExtras(bundle);
                getContext().startActivity(i);
                return;
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(event.getX()>10*screenRatioX&&event.getX()<10*screenRatioX+login.getWidth()&&event.getY()<screenY-10*screenRatioX&&event.getY()>screenY-10*screenRatioY-login.getHeight()){
                    hallactivity.callLogin();
                }
                if(event.getX()>screenX-pause.getWidth()-10*screenRatioX&&event.getY()<10*screenRatioX+pause.getHeight()){
                    //se il tocco avviene nel quadrato in cui si trova il bottone di pausa
                    short flag=0,flagActivity=1;
                    hallactivity.callManager(flag,flagActivity);
                }
                if((distance/2-ranking.getWidth()/2 < event.getX() && event.getX() < distance/2 + ranking.getWidth()/2)
                        &&
                     event.getY() < ranking.getHeight())
                {
                    short flag=1,flagActivity=1;// flagActivity in questo caso e inutile
                    hallactivity.callManager(flag,flagActivity);


                }

                if(event.getX()>distance/2-200-apollo11.getWidth()&&event.getX()<distance/2-200&&event.getY()>0&&event.getY()<apollo11.getHeight()){

                    if(prefs.getInt("flagLevel",-1) == 0 && prefs.getInt("flagLevel",-1) != 1) {
                        checkLevelStatus((short) 0);
                    }else {
                        isSwitching = true;
                    }

                    //quando tocchi sulla teca dell'apollo 11 richiamare l'activity portrait
                }
                if((event.getX()>distance/2+200)&&(event.getX()<distance/2+200+roverMars.getWidth())&&event.getY()>screenY-floor.getHeight()-roverMars.getHeight()+5&&event.getY()<screenY-floor.getHeight()+5)
                {
                    if( gemTot>= 50)
                    {
                        if(prefs.getInt("flagLevel",-1) == 1 && prefs.getInt("flagLevel",-1) != 0) {
                            checkLevelStatus((short) 1);
                        }else{
                            hallactivity.callTravel((short)1);
                        }

                    }else
                    {

                        toast = Toast.makeText(getContext(), R.string.Gemmcap, Toast.LENGTH_SHORT);
                        toast.show();

                    }



                }


                break;
            case MotionEvent.AXIS_PRESSURE: //se l'utente sta tenendo premuto lo schermo
                if(event.getX()<screenX/2) {
                    hiroki.setIsPressed(true);
                    hiroki.setIsRight(false);
                    return true;
                }
                else if(event.getX()>screenX/2){//quando questo succede il tocco arriva dalla parte destra
                    hiroki.setIsPressed(true);
                    hiroki.setIsRight(true);
                    return true;
                }
            case MotionEvent.ACTION_UP:     //se l'utente ha lasciato lo schermo
                hiroki.setIsPressed(false);
                break;

        }

        return true;
    }


}
