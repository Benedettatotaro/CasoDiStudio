package com.example.casodistudio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PauseFragment extends Fragment {

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