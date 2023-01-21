package com.example.casodistudio.ingress;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.casodistudio.HallActivity;
import com.example.casodistudio.MainActivity;
import com.example.casodistudio.GameActivityLandscape;

import com.example.casodistudio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class HomeFragment extends Fragment {

    //bottoni del fragment d'ingresso che servono per fare login, registrazione e entrare come ospite

    private Button loginButton;
    private Button singupButton;
    private TextView account;
    private SharedPreferences prefs;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        loginButton = v.findViewById(R.id.loginBtn);
        singupButton = v.findViewById(R.id.singupBtn);
        account = v.findViewById(R.id.textView3);

        prefs=getActivity().getSharedPreferences("game",getActivity().MODE_PRIVATE);
        account.setText("Account :" + prefs.getString("email",""));



        loginButton.setOnClickListener(v14 -> {
            AppCompatActivity activity = (MainActivity) getActivity();
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new LoginFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        singupButton.setOnClickListener(v13 -> {
            AppCompatActivity activity = (MainActivity) getActivity();
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new SingUpFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}