package com.gmail.nichyekdev.healthtoolbox;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;


public class vo2burgerFragment extends Fragment {


    public vo2burgerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vo2BurgerInflated = inflater.inflate(R.layout.fragment_vo2burger, container, false);

        final NumberPicker burgerMin = vo2BurgerInflated.findViewById(R.id.burgerMin);
        final NumberPicker burgerSec = vo2BurgerInflated.findViewById(R.id.burgerSec);
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

                double rawVO2 = 85.95 - (3.079*time);
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
