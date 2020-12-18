package com.gmail.nichyekdev.healthtoolbox;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class OneRepMaxSetup extends Fragment {

    //Params if any to be placed here
    int eqnChosen = 0;
    int numRepValue = 1;

    public OneRepMaxSetup() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mLayout = inflater.inflate(R.layout.fragment_one_rep_max_setup, container, false);
        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                if(getActivity().getCurrentFocus() != null){
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }
            }
        });


        //Spinner Logic
        Spinner eqnMenu = mLayout.findViewById(R.id.oneRM_EqnPicker);
        String[] strs = getResources().getStringArray(R.array.oneRM_Equations);
        ArrayList<String> equations = new ArrayList<>(Arrays.asList(strs));
        OneRepMaxSpinnerAdapter adapter =  new OneRepMaxSpinnerAdapter(this.getContext(), R.layout.spinner_dropdownitem, equations);
        eqnMenu.setAdapter(adapter);

        //Other UI Logic
        Button calculate = mLayout.findViewById(R.id.oneRM_calculateBtn);
        final TextView eqnDescription = mLayout.findViewById(R.id.oneRM_EqnDescription);
        final TextView numRepWarning = mLayout.findViewById(R.id.oneRM_numRepWarning);
        final TextView numRepIndicator = mLayout.findViewById(R.id.oneRM_numRepIndicator);
        numRepIndicator.setText(numRepValue + " Reps");
        numRepIndicator.setTextSize(24);
        final EditText weightInput = mLayout.findViewById(R.id.oneRM_WeightInput);

        SeekBar numRepBar = mLayout.findViewById(R.id.oneRM_numRepInput);
        numRepBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                numRepValue = i;
                String descr;
                String warning = "Warning: 1RM won't be as accurate!";
                numRepWarning.setText(warning);
                numRepWarning.setTextColor(Color.parseColor("#B22222"));
                descr = numRepValue + " Reps";
                numRepIndicator.setText(descr);
                if(i<=10){
                    numRepWarning.setVisibility(View.INVISIBLE);
                }else{
                    numRepWarning.setVisibility(View.VISIBLE);
                }
              }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });



        //Equation Selection Logic
        eqnMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                eqnChosen = position;
                String string;
                switch(position){
                    case 0:
                        string = "Choose an equation!\nLess reps give more accurate results.";
                        eqnDescription.setText(string);
                        break;
                    case 1:
                        string = "Brzycki Equation selected.\nThe most widely used formula.";
                        eqnDescription.setText(string);
                        break;
                    case 2:
                        string = "Epley Equation selected.\nWorks best with Squats.";
                        eqnDescription.setText(string);
                        break;
                    case 3:
                        string = "Mayhew Equation selected.\nWorks best with Bench Press.";
                        eqnDescription.setText(string);
                        break;
                    case 4:
                        string = "Wathen Equation selected.\nWorks best with Deadlifts.";
                        eqnDescription.setText(string);
                        break;
                    case 5:
                        string = "O'Connor Equation selected.\nWorks best with Chest Press.";
                        eqnDescription.setText(string);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        //Compile, calculate and bundle up for the new fragment to use
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double oneRepMax;
                if(eqnChosen==0){
                    Snackbar.make(view, "Did you choose an equation?", Snackbar.LENGTH_SHORT).show();
                    return;
                }else if(weightInput.getText().toString().length()==0 || Double.parseDouble(weightInput.getText().toString()) == 0){
                    Snackbar.make(view, "Did you input a weight yet?", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                double weight = Double.parseDouble(weightInput.getText().toString());
                if(numRepValue==1){
                    oneRepMax = weight;
                }else{
                    oneRepMax = findOneRM(eqnChosen, weight, numRepValue);
                }

                ((OneRepMaxActivity)getContext()).setOrmValue(oneRepMax);
                ((OneRepMaxActivity)getContext()).startOneRMOutput();
            }
        });

        return mLayout;
    }


    //1RM equation logic
    private double findOneRM(int eqnUsed, double weight, int reps){
        double oneRM;
        switch(eqnUsed){
            case 1: //Brzycki Eqn
                oneRM = 36*weight;
                oneRM /= (37-reps);
                break;
            case 2: //Epley Eqn
                oneRM = weight *reps/30;
                oneRM += weight;
                break;
            case 3: //Mayhew Eqn
                oneRM = 100*weight;
                double inter = 52.2 + 41.9*Math.pow(Math.E,-0.055*reps);
                oneRM /= inter;
                break;
            case 4: //Wathen Eqn
                oneRM = 100*weight;
                double mid = 48.8 + 53.8*Math.pow(Math.E, -0.075*reps);
                oneRM /= mid;
                break;
            case 5: //O'Connor Eqn
                oneRM = (double) reps/40;
                oneRM += 1;
                oneRM *= weight;
                break;
            default:
                oneRM = 0;
                break;
        }

        DecimalFormat oneDP = new DecimalFormat("#.#");
        oneRM = Double.parseDouble(oneDP.format(oneRM));
        return oneRM;
    }

}