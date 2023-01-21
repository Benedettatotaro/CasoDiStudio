package com.example.casodistudio;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RankingFragment extends Fragment {


    DatabaseReference databaseReference; //variabile per la referenza al nostro database
    FirebaseDatabase firebaseDatabase; // variabile per il nostro database ??
    ValueEventListener valueEventListener;
    //TO DO: GESTIRE LA CLASSIFICA


    // SELECT POINTS FROM USERS

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").getParent();



        databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.addListenerForSingleValueEvent(valueEventListener);

        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }
}