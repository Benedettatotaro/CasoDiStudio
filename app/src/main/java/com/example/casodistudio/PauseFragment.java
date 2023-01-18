package com.example.casodistudio;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PauseFragment extends Fragment {

    private short flag;

    public PauseFragment(short flag){
        this.flag=flag;
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
                if(flag==0){   //la pausa è stata chiamata dall'activity viaggio
                    Intent i=new Intent(getActivity(),HallActivity.class);
                    //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                else if(flag==1){  //la pausa è stata chiamata dall'activity museo
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