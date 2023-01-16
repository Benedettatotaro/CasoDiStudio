package com.example.casodistudio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.casodistudio.game.gameviews.LViews.ViewMuseum;

public class GameActivityLandscape extends AppCompatActivity {

    private ViewMuseum viewMuseo;  //TO DO: CAMBIARE IN VIEW MARTE E LUNA E FARE IL SET CONTENT VIEW
    private short flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //imposta l'orientamento in orizzontale come obbligatorio

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

         flag=getIntent().getExtras().getShort("flag");
        //imposta lo schermo in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //ottiene le dimensioni dello schermo a run time
        Point point=new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        //e le passa al costruttore della view

        switch (flag){
            //case 0: viewMuseo = new ViewMuseo(this,point.x,point.y);
        }
       // viewLandscape=new ViewLandscape(this,point.x,point.y);
        //viewMuseo = new ViewMuseo(this,point.x,point.y);

        //setta la view del gioco
        setContentView(viewMuseo);

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewMuseo.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewMuseo.pause();
    }

}