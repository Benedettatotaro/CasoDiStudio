package com.example.casodistudio.ingress;



import androidx.annotation.RequiresApi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.casodistudio.HallActivity;
import com.example.casodistudio.MainActivity;
import com.example.casodistudio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SingUpFragment extends Fragment {

    //definizione variabili che conterranno le informazioni provenienti dalla view
    //fragment_singup
    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText password;
    private EditText checkPassword;
    private Button singupBtn;
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private FirebaseAuth auth;
    FirebaseFirestore db;
    Toast toast;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_sing_up, container, false);


        email=v.findViewById(R.id.email);
        password=v.findViewById(R.id.password);
        checkPassword=v.findViewById(R.id.conPassword);
        singupBtn=v.findViewById(R.id.singupBtn);



        prefs=getActivity().getSharedPreferences("game",getActivity().MODE_PRIVATE);



        singupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_name = name.getText().toString();
                String txt_surname = surname.getText().toString();
                String txt_checkPassword = checkPassword.getText().toString();
                auth = FirebaseAuth.getInstance();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_email))
                {
                    toast=Toast.makeText(getActivity(), "Fill all the credentials to login ", Toast.LENGTH_SHORT);
                    toast.show();
                }else if (txt_password.length() < 6){
                    toast=Toast.makeText(getActivity(), "password too short, min.6 character", Toast.LENGTH_SHORT);
                    toast.show();

                } else if(txt_password.compareTo(txt_checkPassword) != 0  ) // sono uguali
                {
                    toast=Toast.makeText(getActivity(), "password and confirm password are different", Toast.LENGTH_SHORT);
                    toast.show();
                }else {

                    ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                    if(connectivityManager.getActiveNetwork() != null )
                    {
                        registerUser(txt_email,txt_password);


                    }else
                    {
                        toast=Toast.makeText(getActivity(), "check your connection and retry", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }




            }
        });


        return v;
    }

    private void registerUser(String txt_email, String txt_password) {
        auth.createUserWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    toast.makeText(getActivity(), "Registering user successful",Toast.LENGTH_SHORT).show();

                    editor= prefs.edit();
                    editor.putString("email", txt_email);
                    editor.putLong("moonGem", 0);
                    editor.putLong("marsGem", 0);
                    editor.commit();

                    Log.d("1",prefs.getAll().toString());
                    db = FirebaseFirestore.getInstance();


                    Map<String, Integer> gems = new HashMap<>();
                    gems.put("moonGem", 0);
                    gems.put("marsGem", 0);

                    db.collection("gems").document(txt_email).set(gems);
                    Intent i = new Intent(getActivity(), HallActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                }else
                {
                    toast.makeText(getActivity(), "Registration failed",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
}