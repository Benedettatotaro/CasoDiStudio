package com.example.casodistudio.game.gameviews;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import com.example.casodistudio.MainActivity;

public class Main {
    private SharedPreferences prefs;
    public Main(MainActivity activity){
        prefs=activity.getSharedPreferences("game",MODE_PRIVATE);
        String email;
        email=prefs.getString("email",null);
        if(email!=null){ //se l'utente è già registrato con il sistema chiama direttamente l'activity di gioco
            activity.callHall();
        }
        else{
            activity.callFragment();
        }
    }
}
