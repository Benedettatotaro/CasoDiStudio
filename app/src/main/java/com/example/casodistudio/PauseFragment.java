package com.example.casodistudio;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.casodistudio.game.gameviews.ViewTravel;

public class PauseFragment extends Fragment {

    private short flag;
   // private SharedPreferences prefs = getActivity().getSharedPreferences("game",getActivity().MODE_PRIVATE);
    private SharedPreferences.Editor editor;


    public PauseFragment()
    {
        //this.flag=flag;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        this.flag = args.getShort("flagActivity");

    }

    private boolean isMute=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_pause, container, false);

        v.findViewById(R.id.playCtrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ritornare all'activity precedente distruggendo questa
                getActivity().finish();

            }
        });

        v.findViewById(R.id.homeCtrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag==0||flag==2){   //la pausa è stata chiamata dall'activity viaggio o uno dei pianeti
                    SharedPreferences prefs = getActivity().getSharedPreferences("game",getActivity().MODE_PRIVATE);
                    editor = prefs.edit();

                    editor.putInt("tempGems", 0);
                    editor.putInt("gameCounter", 0);
                    editor.putInt("flagLevel", -1);
                    // prefs=getActivity().getSharedPreferences("game",getActivity().MODE_PRIVATE);
                    // int tempGem = prefs.getInt("tempoGems",0);
                    // int gameCounter = prefs.getInt("gameCounter", 0);

                    Intent i=new Intent(getActivity(),HallActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    //getActivity().onBackPressed(); //va indietro nella pila di chiamate
                    getActivity().finish(); //e chiude l'activity manager
                }
                else if(flag==1){  //la pausa è stata chiamata dall'activity museo
                    NavUtils.navigateUpFromSameTask(getActivity());
                    getActivity().finish();
                }
            }
        });

        ImageView volumeCtrl=v.findViewById(R.id.volumeCtrl);

        if(isMute)
            volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_off_24);
        else
            volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_up_24);

        volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMute=!isMute;
                if(isMute)
                    volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_off_24);
                else
                    volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_up_24);
            }
        });

        return v;
    }
}