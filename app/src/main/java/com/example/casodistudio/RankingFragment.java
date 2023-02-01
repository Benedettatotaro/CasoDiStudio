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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
    ListView rankList;
    ArrayAdapter<String> arr;
    String[] appoggio;
    Button go_back;
    //TO DO: GESTIRE LA CLASSIFICA


    // SELECT POINTS FROM USERS

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ranking, container, false);
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getActiveNetwork() != null)
        {
           //rankList = getView().findViewById(R.id.list);
            rankList = v.findViewById(R.id.list);
            go_back = v.findViewById(R.id.go_back);

            go_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
            ArrayList<Player> list= new ArrayList<Player>();
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
                        appoggio = new String[list.size()];

                       // for(int i= 0; i< list.size(); i++)
                      // {
                        //    player = list.get(i);
                       //     appoggio[i] = player.getName() + player.getPunteggio();


                      // }
                        CustomAdapter myCustomAdapter = new CustomAdapter(v.getContext(),list);
                        rankList.setAdapter(myCustomAdapter);

                        //arr = new ArrayAdapter<String>(v.getContext(),R.layout.fragment_ranking,appoggio);
                        //rankList.setAdapter(arr);




                    }else
                    {
                        Toast.makeText(getContext(),R.string.FetchErr,Toast.LENGTH_SHORT).show();
                    }
                }
            });




        }else
        {
            Toast.makeText(getContext(),R.string.ConnRankErr,Toast.LENGTH_SHORT).show();
        }




        return v;
    }



}