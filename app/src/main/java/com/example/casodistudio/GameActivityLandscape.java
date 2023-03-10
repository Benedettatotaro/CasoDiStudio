package com.example.casodistudio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.casodistudio.game.gameviews.LViews.ViewLandscape;
import com.example.casodistudio.game.gameviews.LViews.ViewMuseum;

public class GameActivityLandscape extends AppCompatActivity {

    private ViewLandscape viewLandscape;  //TO DO: CAMBIARE IN VIEW MARTE E LUNA E FARE IL SET CONTENT VIEW
    private short flagPlanet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //imposta l'orientamento in orizzontale come obbligatorio

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

         flagPlanet=getIntent().getExtras().getShort("flagPlanet");
        //imposta lo schermo in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //ottiene le dimensioni dello schermo a run time
        Point point=new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        //e le passa al costruttore della view

        viewLandscape= new ViewLandscape(GameActivityLandscape.this,flagPlanet,point.y,point.x);
        setContentView(viewLandscape);

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewLandscape.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewLandscape.pause();
    }

    public void callManager(short flag, short flagActivity){
        Bundle bundle=new Bundle(2);
        bundle.putShort("flag", flag); //setta il flag nel boundle uguale a 0 perchè sta chiamando il fragment di pausa
        bundle.putShort("flagActivity",flagActivity);
        Intent i = new Intent(GameActivityLandscape.this, ManagerActivity.class);
        i.putExtras(bundle);
        this.startActivity(i);
    }

}