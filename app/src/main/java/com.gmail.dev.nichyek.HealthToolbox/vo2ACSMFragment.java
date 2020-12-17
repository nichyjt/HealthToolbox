package com.gmail.dev.nichyek.HealthToolbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;

public class vo2ACSMFragment extends Fragment {


    public vo2ACSMFragment(){
        //required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vo2BurgerInflated = inflater.inflate(R.layout.fragment_vo2burger, container, false);

        final NumberPicker burgerMin = vo2BurgerInflated.findViewById(R.id.burgerMin);
        final NumberPicker burgerSec = vo2BurgerInflated.findViewById(R.id.burgerSec);
        TextView source = vo2BurgerInflated.findViewById(R.id.sourceDescription);
        source.setText("Based on: ACSM's Complete Guide to Fitness & Health, 2011");
        burgerMin.setMaxValue(30);
        burgerMin.setMinValue(6);
        burgerSec.setMaxValue(59);

        Button calculateButton = vo2BurgerInflated.findViewById(R.id.vo2CalculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double time;
                int minuteInput = burgerMin.getValue();
                double secondInput = burgerSec.getValue();
                time = minuteInput + (secondInput/60);

                double rawVO2 = (483/time) + 3.5;
                DecimalFormat twoDecimalPlaces = new DecimalFormat("##.##");
                double VO2max = Double.parseDouble(twoDecimalPlaces.format(rawVO2));

                String val = "VO2 Max: " + VO2max;
                ((VO2Calculator)getContext()).output.setText(val);
                Snackbar.make(view, "VO2 Calculated!", Snackbar.LENGTH_SHORT).show();

            }
        });

        return vo2BurgerInflated;
    }

}
