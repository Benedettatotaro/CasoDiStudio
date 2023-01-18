package com.example.casodistudio;

import androidx.appcompat.app.AppCompatActivity;

import com.example.casodistudio.game.gameviews.LViews.ViewMuseum;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

public class HallActivity extends AppCompatActivity {

    private ViewMuseum viewMuseum;
    private short flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //imposta l'orientamento in orizzontale come obbligatorio

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //imposta lo schermo in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //ottiene le dimensioni dello schermo a run time
        Point point=new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        //e le passa al costruttore della view
        viewMuseum = new ViewMuseum(this,point.x,point.y);

        //setta la view del gioco
        setContentView(viewMuseum);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewMuseum.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewMuseum.pause();
    }
    public void callTravel(short flag){
        Bundle bundle=new Bundle(1);
        bundle.putShort("flag", flag); //setta il flag nel boundle uguale a quello che gli arriva dalla view per capire se sta andando su marte o sulla luna
        Intent i = new Intent(HallActivity.this, GameActivityPortrait.class);
        i.putExtras(bundle);
        this.startActivity(i);
    }
    public void callManager(short flag, short flagActivity){
        Bundle bundle=new Bundle(2);
        bundle.putShort("flag", flag); //setta il flag nel boundle uguale a 0 perchè sta chiamando il fragment di pausa
        bundle.putShort("flagActivity",flagActivity);
        Intent i = new Intent(HallActivity.this, ManagerActivity.class);
        i.putExtras(bundle);
        this.startActivity(i);
    }

    public void callLogin(){  //chiama l'activity login
        Intent i=new Intent(HallActivity.this,MainActivity.class);
        this.startActivity(i);
    }

}