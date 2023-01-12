package com.example.casodistudio.ingress;

import androidx.annotation.RequiresApi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.casodistudio.MainActivity;
import com.example.casodistudio.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SingUpFragment extends Fragment {

    //definizione variabili che conterranno le informazioni provenienti dalla view
    //fragment_singup
    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText password;
    private EditText conPassword;
    private Button singupBtn;

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-d0f2d-default-rtdb.firebaseio.com/");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_sing_up, container, false);

        name=v.findViewById(R.id.name);
        surname=v.findViewById(R.id.surname);
        email=v.findViewById(R.id.email);
        password=v.findViewById(R.id.password);
        conPassword=v.findViewById(R.id.conPassword);
        singupBtn=v.findViewById(R.id.singupBtn);


            singupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTxt=name.getText().toString();
                String surnameTxt=surname.getText().toString();
                String emailtxt=email.getText().toString();
                String passwordTxt=password.getText().toString();
                String conPasswordTxt=conPassword.getText().toString();
                Toast toast;

                if (nameTxt.isEmpty()||surnameTxt.isEmpty()||emailtxt.isEmpty()||passwordTxt.isEmpty()||conPasswordTxt.isEmpty()){

                    toast=Toast.makeText(getActivity(), "compila tutti i campi", Toast.LENGTH_SHORT);
                    toast.show();

                }
                //funziona ma non si può inserire l'email nel db perché crasha
                /*else if(!(Patterns.EMAIL_ADDRESS.matcher(emailtxt).matches())){

                    toast=Toast.makeText(getActivity(), "inserisci un email valida", Toast.LENGTH_SHORT);
                    toast.show();

                }*/
                else if(!passwordTxt.equals(conPasswordTxt)){ //se le due password non coincidono

                    toast=Toast.makeText(getActivity(), "le due password devono coincidere", Toast.LENGTH_SHORT);
                    toast.show();

                }
                else{

                    //controlla se il device è connesso ad internet
                    ConnectivityManager connectivityManager =  (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                    if(connectivityManager.getActiveNetwork()==null){  //se il metodo get Active NetWork ritorna null vuol dire che il dispositivo non è connesso ad internet
                        Toast.makeText(getActivity(), "Il dispositivo non è connesso ad internet", Toast.LENGTH_SHORT).show();
                    }

                    else {  //se il dispositivo è connesso ad internet invia dati al db

                        databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //controlla se l'email è già presente nel db
                                if (snapshot.hasChild(emailtxt)) {
                                    Toast toast = Toast.makeText(getActivity(), "email già registrata", Toast.LENGTH_SHORT);
                                    toast.show();
                                } else {
                                    //usando l'email come chiave primaria del db inserisco tutti i campi
                                    databaseReference.child("user").child(emailtxt).child("name").setValue(nameTxt);
                                    databaseReference.child("user").child(emailtxt).child("surname").setValue(surnameTxt);
                                    databaseReference.child("user").child(emailtxt).child("password").setValue(passwordTxt);

                                    //se tutto va a buon fine compare un messaggio di avvenuta registrazione
                                    Toast toast = Toast.makeText(getActivity(), "registrazione avvenuta con successo", Toast.LENGTH_SHORT);
                                    toast.show();

                                    //e chiede all'utente di loggarsi caricando il fragment del login
                                    /*AppCompatActivity activity = (MainActivity) getActivity();

                                    toast = Toast.makeText(getActivity(), "Effettua il login", Toast.LENGTH_SHORT);
                                    toast.show();

                                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.container, new LoginFragment());
                                    ft.addToBackStack(null);
                                    ft.commit();*/
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });

        return v;
    }
}