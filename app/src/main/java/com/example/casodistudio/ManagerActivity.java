package com.example.casodistudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class ManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        short flag=getIntent().getExtras().getShort("flag");
        //questa activity è solo un conenitore del fragment di pausa
        if(flag==0){  //se il flag è zero chiama il fragment di pausa
            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_manager, new PauseFragment());
            ft.commit();
        }
        else if(flag==1){  //altrimenti chiama il fragment delle classifiche
            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_manager, new RankingFragment());
            ft.commit();
        }
    }
}