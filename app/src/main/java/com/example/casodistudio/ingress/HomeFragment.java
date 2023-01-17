package com.example.casodistudio.ingress;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.casodistudio.HallActivity;
import com.example.casodistudio.MainActivity;
import com.example.casodistudio.GameActivityLandscape;

import com.example.casodistudio.R;

public class HomeFragment extends Fragment {

    //bottoni del fragment d'ingresso che servono per fare login, registrazione e entrare come ospite

    private Button loginButton;
    private Button singupButton;
    private Button hostButton;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        loginButton = v.findViewById(R.id.loginBtn);
        singupButton = v.findViewById(R.id.singupBtn);
        hostButton = v.findViewById(R.id.hostBtn);

        loginButton.setOnClickListener(v14 -> {
            AppCompatActivity activity = (MainActivity) getActivity();
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new LoginFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        //quando si clicca sul bottone per entrare come ospiti viene chiamato
        //un'intent che chiama l'activity landscape
        hostButton.setOnClickListener(v13 -> {
            Intent i = new Intent(getActivity(), HallActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(i);
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