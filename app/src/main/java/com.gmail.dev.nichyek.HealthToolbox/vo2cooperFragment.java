package com.gmail.nichyekdev.healthtoolbox;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;

public class vo2cooperFragment extends Fragment {

    public vo2cooperFragment() {
        // Required empty public constructor
    }

    Switch imperialSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout and set up some extra UI for this fragment
        final View cooperView = inflater.inflate(R.layout.fragment_vo2cooper, container, false);
        final TextInputLayout layout = cooperView.findViewById(R.id.cooperLayout);
        EditText cooperInput = cooperView.findViewById(R.id.cooperInput);
        Button calculateButton = cooperView.findViewById(R.id.vo2CalculateButton);
        //The following code checks if distance input is filled and calculates the VO2Max.

        cooperView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(getContext(), cooperView);
            }
        });


        imperialSwitch = cooperView.findViewById(R.id.vo2ImperialSwitch);
        imperialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    layout.setHint("Distance (mi)");
                }else{
                    layout.setHint("Distance (m)");
                }
            }
        });
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText cooperInput = cooperView.findViewById(R.id.cooperInput);
                if(cooperInput.getText().toString().length() == 0){
                    Snackbar.make(view, "Did you put in a distance yet?", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                double distanceRan = Double.parseDouble(cooperInput.getText().toString());
                if(imperialSwitch.isChecked()){
                    distanceRan *= 1609.34;
                }
                double rawVO2 = (distanceRan - 504.9)/44.73;
                DecimalFormat twoDecimalPlaces = new DecimalFormat("##.##");
                double VO2Max = Double.parseDouble(twoDecimalPlaces.format(rawVO2));
                String val = "VO2 Max: " + VO2Max;
                ((VO2Calculator)getContext()).output.setText(val);
                Snackbar.make(view, "VO2 Calculated!", Snackbar.LENGTH_SHORT).show();
            }
        });

        cooperInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    cooperView.findViewById(R.id.vo2cooperFragmentId).performClick();
                }
                return false;
            }
        });
        return  cooperView;
    }

    //Code to hide soft keyboard
    public static void hideKeyboard(@Nullable Context context, View view) {
        if(context != null){
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



}