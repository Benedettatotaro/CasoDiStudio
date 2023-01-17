package com.example.casodistudio.ingress;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.casodistudio.HallActivity;
import com.example.casodistudio.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    private EditText email;
    private EditText password;
    private Button loginBtn;

    //oggetto che si collega al database creato su Firebase
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-d0f2d-default-rtdb.firebaseio.com/");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        if(databaseReference==null){
            Toast toast=Toast.makeText(getActivity(), "ehm c'è qualquadra che non cosa", Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            email=v.findViewById(R.id.email);
            password=v.findViewById(R.id.password);
            loginBtn=v.findViewById(R.id.loginBtn);

            loginBtn.setOnClickListener(view -> {

                String emailtxt=email.getText().toString();
                String passwordtxt=password.getText().toString();

                if(emailtxt.isEmpty()){ //se il campo email è vuoto

                    //richiama l'activity chiamante e gli fa stampare la stringa per inserire l'email

                    Toast toast=Toast.makeText(getActivity(), "inserisci l'email", Toast.LENGTH_SHORT);
                    toast.show();

                }

                else if(passwordtxt.isEmpty()){ //se il campo passoword è vuoto

                    //richiama l'activity chiamante e gli fa stampare la stringa per inserire l'email

                    Toast toast=Toast.makeText(getActivity(), "inserisci la password", Toast.LENGTH_SHORT);
                    toast.show();

                }
                //funziona ma non si può inserire l'email nel db perché crasha
            /*else if(!(Patterns.EMAIL_ADDRESS.matcher(emailtxt).matches())){  //se è stata inserita un email invalida

                Toast toast=Toast.makeText(getActivity(), "inserisci un email valida", Toast.LENGTH_SHORT);
                toast.show();  //stampa il messaggio di errore

            }*/

                else{ //altrimenti se entrambi i campi sono pieni e validi

                    //controlla se il device è connesso ad internet
                    ConnectivityManager connectivityManager =  (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                    if(connectivityManager.getActiveNetwork()==null){  //se il metodo get Active NetWork ritorna null vuol dire che il dispositivo non è connesso ad internet
                        Toast.makeText(getActivity(), "Il dispositivo non è connesso ad internet", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //se il dispositivo è connesso ad internet invia dati al database
                        databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                //controlla se esiste un account con questa email
                                if (snapshot.hasChild(emailtxt)) {

                                    //ora prende la password dal db
                                    final String passworddb = snapshot.child(emailtxt).child("password").getValue(String.class);
                                    if (passworddb.equals(passwordtxt)) {  //e controlla se la password inserita dall'utente è uguale a quella presente nel db
                                        Toast.makeText(getActivity(), "login avvenuto con successo", Toast.LENGTH_SHORT).show();
                                        //visto che il login è avvenuto con successo parte l'activity hall per il museo
                                        startActivity(new Intent(getActivity(), HallActivity.class)); //apre l'activity landscape
                                        getActivity().finish(); //e chiude quella in cui è contenuto il fragment

                                    } else {
                                        Toast.makeText(getActivity(), "password errata", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "non esiste un account legato a questa email", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }


            });
        }

        return v;
    }
}