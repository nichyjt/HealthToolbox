package com.gmail.dev.nichyek.HealthToolbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;


public class HeartRateTimerSetupFragment extends Fragment {

    RadioGroup group;
    RadioButton fifteenSecs;
    RadioButton thirtySecs;
    RadioButton sixtySecs;
    ImageView startButton;

    public HeartRateTimerSetupFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_heart_rate_timer_setup, container, false);
        group = view.findViewById(R.id.HeartRateTimerOptionRow);
        fifteenSecs = view.findViewById(R.id.HeartRateTimer15s);
        thirtySecs = view.findViewById(R.id.HeartRateTimer30s);
        sixtySecs = view.findViewById(R.id.HeartRateTimer60s);
        startButton = view.findViewById(R.id.HeartRateTimerStartTimerButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            int time;
            @Override
            public void onClick(View view) {
                if(fifteenSecs.isChecked()) time = 15;
                if(thirtySecs.isChecked()) time = 30;
                if(sixtySecs.isChecked()) time = 60;
                ((HeartRateActivity)getActivity()).startHeartRateTimerCountdownFragment(time);
            }
        });

        return view;
    }
}