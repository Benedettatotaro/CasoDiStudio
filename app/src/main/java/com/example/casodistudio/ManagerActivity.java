package com.example.casodistudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

public class ManagerActivity extends AppCompatActivity {

    private short flagActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        short flag=getIntent().getExtras().getShort("flag");
        flagActivity=getIntent().getExtras().getShort("flagActivity");
        //questa activity è solo un conenitore del fragment di pausa
        if(flag==0){  //se il flag è zero chiama il fragment di pausa
            if (flagActivity == 0) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            else if(flagActivity==1){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            PauseFragment fragment = new PauseFragment();
            Bundle pauseFragmentFlag = new Bundle(1);
            pauseFragmentFlag.putShort("flagActivity", flag);
            fragment.setArguments(pauseFragmentFlag);
            ft.replace(R.id.container_manager,fragment);


            ft.commit();
        }
        else if(flag==1){  //altrimenti chiama il fragment delle classifiche
            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_manager, new RankingFragment());
            ft.commit();
        }/*else if (flag ==2){

        }
        */
    }
}