package com.example.casodistudio;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.casodistudio.ingress.Player;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RankingFragment extends Fragment {

    FirebaseFirestore db;
    DocumentReference docRef;
    ValueEventListener valueEventListener;
    Player player;
    long totGems = 0;
    //TO DO: GESTIRE LA CLASSIFICA


    // SELECT POINTS FROM USERS

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getActiveNetwork() != null)
        {


            List<Player> list= new ArrayList<Player>();
            db = FirebaseFirestore.getInstance();
            db.collection("gems").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {

                        for(QueryDocumentSnapshot document : task.getResult()) {

                            totGems = (long)(document.getData()).get("marsGem") + (long)(document.getData()).get("moonGem");
                            list.add(new Player(document.getId(), totGems));

                        }
                        Collections.sort(list);


                    }else
                    {
                        Toast.makeText(getContext(),"unable to fetch data from database",Toast.LENGTH_SHORT).show();
                    }
                }
            });




        }else
        {
            Toast.makeText(getContext(),"Ranking unavailable, connection missing",Toast.LENGTH_SHORT).show();
        }




        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }



}