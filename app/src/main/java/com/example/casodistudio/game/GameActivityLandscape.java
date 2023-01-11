package com.example.casodistudio.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.casodistudio.R;
import com.example.casodistudio.game.gameviews.ViewLandscape;

public class GameActivityLandscape extends AppCompatActivity {

    private ViewLandscape viewLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //imposta l'orientamento in orizzontale come obbligatorio

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        short flag=getIntent().getExtras().getShort("flag");
        //imposta lo schermo in full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //ottiene le dimensioni dello schermo a run time
        Point point=new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        //e le passa al costruttore della view
        viewLandscape=new ViewLandscape(this,point.x,point.y,flag);

        //setta la view del gioco
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
}