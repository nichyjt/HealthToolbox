package com.gmail.dev.nichyek.HealthToolbox;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class HeartRateTimerCountdownFragment extends Fragment {

    public HeartRateTimerCountdownFragment(int time) {
        // Required empty public constructor
        this.time = time;
    }

    TextView countdownText;
    TextView descriptionText;
    ImageView tapImage;
    ImageView restartButton;

    int time;
    int taps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_heart_rate_timer_countdown, container, false);
        tapImage = view.findViewById(R.id.HeartRateTimerCountdownIcon);
        countdownText = view.findViewById(R.id.HeartRateTimerCountdownText);
        descriptionText = view.findViewById(R.id.HeartRateTimerDescriptionText);
        restartButton = view.findViewById(R.id.HeartRateTimerCountdownRestartIcon);
        //Initial UI params
        tapImage.setVisibility(View.GONE);
        restartButton.setVisibility(View.INVISIBLE);
        descriptionText.setText("Get ready...");
        view.setClickable(false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++taps;
            }
        });
        prepareTimer(view).start();
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taps = 0;
                prepareTimer(view).start();
                descriptionText.setVisibility(View.VISIBLE);
                restartButton.setVisibility(View.INVISIBLE);
                descriptionText.setText("Get Ready...");
                view.setClickable(false);
            }
        });
        return view;
    }

    private CountDownTimer prepareTimer(final View view){
        return new CountDownTimer(3000, 100) {
            @Override
            public void onTick(long l) {
                int currTime = 1+Integer.parseInt(String.valueOf(l))/1000;
                countdownText.setText(""+currTime);
            }
            @Override
            public void onFinish() {
                tapImage.setVisibility(View.VISIBLE);
                tapImage.setImageResource(R.drawable.ic_tap);
                descriptionText.setText("Tap to your pulse.");
                mainTimer(view).start();
                view.setClickable(true);
            }
        };
    }

    private CountDownTimer mainTimer(final View mView){
        restartButton.setVisibility(View.INVISIBLE);
        return new CountDownTimer(time*1000, 100) {
            @Override
            public void onTick(long l) {
                int currTime = 1+Integer.parseInt(String.valueOf(l))/1000;
                countdownText.setText(""+currTime);
            }

            @Override
            public void onFinish() {

                mView.setClickable(false);
                int BPM = taps*(60/time);
                String output = BPM + " BPM";
                countdownText.setText(output);
                descriptionText.setVisibility(View.INVISIBLE);
                tapImage.setVisibility(View.GONE);
                restartButton.setVisibility(View.VISIBLE);
                restartButton.setClickable(true);
            }
        };
    }


}