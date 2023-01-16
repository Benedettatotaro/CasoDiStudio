package com.example.casodistudio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.casodistudio.game.gameviews.ViewTravel;

public class GameActivityPortrait extends AppCompatActivity {

    private short flag;
    private ViewTravel viewTravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        flag=getIntent().getExtras().getShort("flag");
        //imposta lo schermo in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //ottiene le dimensioni dello schermo a run time
        Point point=new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        //e le passa al costruttore della view

        viewTravel = new ViewTravel(this,flag,point.y,point.x);

        //setta la view del gioco

        setContentView(viewTravel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewTravel.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewTravel.pause();
    }

    public void callManager(short flag){
        Bundle bundle=new Bundle(1);
        bundle.putShort("flag", flag); //setta il flag nel boundle uguale a 0 perchè sta chiamando il museo
        Intent i = new Intent(GameActivityPortrait.this, ManagerActivity.class);
        i.putExtras(bundle);
        this.startActivity(i);
    }
}