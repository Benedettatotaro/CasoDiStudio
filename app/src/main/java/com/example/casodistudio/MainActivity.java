package com.example.casodistudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.casodistudio.game.gameviews.Main;
import com.example.casodistudio.ingress.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private Main main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        main=new Main(MainActivity.this);

    }

    public void callHall(){
        Intent i=new Intent(MainActivity.this,HallActivity.class);
        startActivity(i);
    }

    public void callFragment(){
        setContentView(R.layout.activity_main);
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new HomeFragment());
        ft.commit();
    }

}