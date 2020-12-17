package com.gmail.dev.nichyek.HealthToolbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;


public class vo2beepFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View beepView =  inflater.inflate(R.layout.fragment_vo2beep, container, false);
        Button calculateButton = beepView.findViewById(R.id.vo2CalculateButton);
        final NumberPicker beepLevel = beepView.findViewById(R.id.beepLevel);
        final NumberPicker beepShuttles = beepView.findViewById(R.id.beepShuttles);
        beepLevel.setMaxValue(21);
        beepLevel.setMinValue(4);
        beepShuttles.setMaxValue(9);
        beepShuttles.setMinValue(1);

        //Dynamically change max number of shuttles based on the Level selected.
        beepLevel.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                switch(i1){
                    case 4:
                    case 5:
                        beepShuttles.setMaxValue(9);
                        break;
                    case 6:
                    case 7:
                        beepShuttles.setMaxValue(10);
                        break;
                    case 8:
                    case 9:
                    case 10:
                        beepShuttles.setMaxValue(11);
                        break;
                    case 11:
                    case 12:
                        beepShuttles.setMaxValue(12);
                        break;
                    case 13:
                    case 14:
                    case 15:
                        beepShuttles.setMaxValue(13);
                        break;
                    case 16:
                    case 17:
                        beepShuttles.setMaxValue(14);
                        break;
                    case 18:
                    case 19:
                        beepShuttles.setMaxValue(15);
                        break;
                    case 20:
                    case 21:
                        beepShuttles.setMaxValue(16);
                        break;
                }
            }
        });

        //Arrays that store the VO2 values relating to the Beep Test.
        //Normative data based on Loughborough University's VO2max table.
        Double[] dataLevel4 = {26.8, 26.8, 27.6, 28.3, 29.5};
        Double[] dataLevel5 = {29.5, 30.2, 31.0, 31.8, 32.9};
        Double[] dataLevel6 = {32.9, 33.6, 34.3, 35.0, 35.7, 36.4};
        Double[] dataLevel7 = {36.4, 37.1, 37.8, 38.5, 39.2, 39.9};
        Double[] dataLevel8 = {39.9, 40.5, 41.1, 41.8, 42.4, 43.3};
        Double[] dataLevel9 = {43.3, 43.9, 44.5, 45.2, 45.8, 46.8};
        Double[] dataLevel10 = {46.8, 47.4, 48.0, 48.7, 49.3, 50.2};
        Double[] dataLevel11 = {50.2, 50.8, 51.4, 51.9, 52.5, 53.1, 53.7};
        Double[] dataLevel12 = {53.7, 54.3, 54.8, 55.4, 56.0, 56.5, 57.1};
        Double[] dataLevel13 = {57.1, 57.6, 58.2, 58.7, 59.3, 59.8, 60.6};
        Double[] dataLevel14 = {60.6, 61.1, 61.7, 62.2, 62.7, 63.2, 64.0};
        Double[] dataLevel15 = {64.0, 64.6, 65.1, 65.6, 66.2, 66.7, 67.5};
        Double[] dataLevel16 = {67.5, 68.0, 68.5, 69.0, 69.5 ,69.9, 70.5, 70.9};
        Double[] dataLevel17 = {70.9, 71.4, 71.9, 72.4, 72.9, 73.4, 73.9, 74.4};
        Double[] dataLevel18 = {74.4, 74.8, 75.3, 75.8, 76.2, 76.7, 77.2, 77.9};
        Double[] dataLevel19 = {77.9, 78.3, 78.8, 79.2, 79.7, 80.2, 80.6, 81.3};
        Double[] dataLevel20 = {81.3, 81.8, 82.2, 82.6, 83.0, 83.5, 83.9, 84.3, 84.8};
        Double[] dataLevel21 = {84.8, 85.2, 85.6, 86.1, 86.5, 86.9, 87.4, 87.8, 88.2};
        final Double[][] dataStorage =
                {dataLevel4, dataLevel5, dataLevel6, dataLevel7, dataLevel8, dataLevel9,
                 dataLevel10, dataLevel11, dataLevel12, dataLevel13, dataLevel14, dataLevel15,
                 dataLevel16, dataLevel17, dataLevel18, dataLevel19, dataLevel20, dataLevel21};

        //Code to calculate and output the VO2 max on a button click
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int bLevel = beepLevel.getValue();
                int bShuttle = beepShuttles.getValue();
                double VO2Max = 0;

                //Loop to avoid redundant, repetitive code for accessing VO2 data.
                //This loop checks which bLevel is selected and draws the correct array set.
                for(int i=4; i<22; i++){
                    if(bLevel == i){
                        //Choose the data set array corresponding to i
                        int dataArrayPosition = i - 4;
                        Double[] workingArray = dataStorage[dataArrayPosition];

                        //There are 8 possible cases to extract VO2 data based on bShuttle,
                        //Because every 2-3 stages has a different number of max shuttles.
                        int vo2DataPosition = 0;
                        //Code to determine specific data location.
                        switch(bLevel){
                            case 4:
                            case 5:
                                if(bShuttle == 1){
                                    vo2DataPosition = 0;
                                }else if (bShuttle >= 2 && bShuttle < 4){
                                    vo2DataPosition = 1;
                                }else if (bShuttle >=4 && bShuttle < 6){
                                    vo2DataPosition = 2;
                                }else if (bShuttle >= 6 && bShuttle < 9){
                                    vo2DataPosition  = 3;
                                }else { //implies that bShuttle == 9
                                    vo2DataPosition = 4;
                                }
                                break;
                            case 6:
                            case 7:
                                if(bShuttle == 1){
                                    vo2DataPosition = 0;
                                }else if (bShuttle >= 2 && bShuttle < 4){
                                    vo2DataPosition = 1;
                                }else if (bShuttle >= 4 && bShuttle < 6){
                                    vo2DataPosition = 2;
                                }else if (bShuttle >= 6 && bShuttle < 8){
                                    vo2DataPosition = 3;
                                }else if (bShuttle >= 8 && bShuttle < 10){
                                    vo2DataPosition = 4;
                                }else{ //implies that bShuttle == 10
                                    vo2DataPosition = 5;
                                }
                                break;
                            case 8:
                            case 9:
                            case 10:
                                if(bShuttle == 1){
                                    vo2DataPosition = 0;
                                }else if (bShuttle >= 2 && bShuttle < 4){
                                    vo2DataPosition = 1;
                                }else if (bShuttle >= 4 && bShuttle < 6){
                                    vo2DataPosition = 2;
                                }else if (bShuttle >= 6 && bShuttle < 8){
                                    vo2DataPosition = 3;
                                }else if (bShuttle >= 8 && bShuttle < 11){
                                    vo2DataPosition = 4;
                                }else{ //implies that bShuttle == 11
                                    vo2DataPosition = 5;
                                }
                                break;
                            case 11:
                            case 12:
                                if(bShuttle == 1){
                                    vo2DataPosition = 0;
                                }else if (bShuttle >= 2 && bShuttle < 4){
                                    vo2DataPosition = 1;
                                }else if (bShuttle >= 4 && bShuttle < 6){
                                    vo2DataPosition = 2;
                                }else if (bShuttle >= 6 && bShuttle < 8){
                                    vo2DataPosition = 3;
                                }else if (bShuttle >= 8 && bShuttle < 10){
                                    vo2DataPosition = 4;
                                }else if(bShuttle >= 10 && bShuttle < 12){
                                    vo2DataPosition = 5;
                                }else{ //implies that bShuttle == 12
                                    vo2DataPosition = 6;
                                }
                                break;
                            case 13:
                            case 14:
                            case 15:
                                if(bShuttle == 1){
                                    vo2DataPosition = 0;
                                }else if (bShuttle >= 2 && bShuttle < 4){
                                    vo2DataPosition = 1;
                                }else if (bShuttle >= 4 && bShuttle < 6){
                                    vo2DataPosition = 2;
                                }else if (bShuttle >= 6 && bShuttle < 8){
                                    vo2DataPosition = 3;
                                }else if (bShuttle >= 8 && bShuttle < 10){
                                    vo2DataPosition = 4;
                                }else if(bShuttle >= 10 && bShuttle < 13){
                                    vo2DataPosition = 5;
                                }else{ //implies that bShuttle == 13
                                    vo2DataPosition = 6;
                                }
                                break;
                            case 16:
                            case 17:
                                if(bShuttle == 1){
                                    vo2DataPosition = 0;
                                }else if (bShuttle >= 2 && bShuttle < 4){
                                    vo2DataPosition = 1;
                                }else if (bShuttle >= 4 && bShuttle < 6){
                                    vo2DataPosition = 2;
                                }else if (bShuttle >= 6 && bShuttle < 8){
                                    vo2DataPosition = 3;
                                }else if (bShuttle >= 8 && bShuttle < 10){
                                    vo2DataPosition = 4;
                                }else if(bShuttle >= 10 && bShuttle < 12){
                                    vo2DataPosition = 5;
                                }else if(bShuttle >= 12 && bShuttle < 14){
                                    vo2DataPosition = 6;
                                }else{ //implies that bShuttle == 14
                                    vo2DataPosition = 7;
                                }
                                break;
                            case 18:
                            case 19:
                                if(bShuttle == 1){
                                    vo2DataPosition = 0;
                                }else if (bShuttle >= 2 && bShuttle < 4){
                                    vo2DataPosition = 1;
                                }else if (bShuttle >= 4 && bShuttle < 6){
                                    vo2DataPosition = 2;
                                }else if (bShuttle >= 6 && bShuttle < 8){
                                    vo2DataPosition = 3;
                                }else if (bShuttle >= 8 && bShuttle < 10){
                                    vo2DataPosition = 4;
                                }else if(bShuttle >= 10 && bShuttle < 12){
                                    vo2DataPosition = 5;
                                }else if(bShuttle >= 12 && bShuttle < 15){
                                    vo2DataPosition = 6;
                                }else{ //implies that bShuttle == 15
                                    vo2DataPosition = 7;
                                }
                                break;
                            case 20:
                            case 21:
                                if(bShuttle == 1){
                                    vo2DataPosition = 0;
                                }else if (bShuttle >= 2 && bShuttle < 4){
                                    vo2DataPosition = 1;
                                }else if (bShuttle >= 4 && bShuttle < 6){
                                    vo2DataPosition = 2;
                                }else if (bShuttle >= 6 && bShuttle < 8){
                                    vo2DataPosition = 3;
                                }else if (bShuttle >= 8 && bShuttle < 10){
                                    vo2DataPosition = 4;
                                }else if(bShuttle >= 10 && bShuttle < 12){
                                    vo2DataPosition = 5;
                                }else if(bShuttle >= 12 && bShuttle < 14){
                                    vo2DataPosition = 6;
                                }else if(bShuttle >= 14 && bShuttle < 16){
                                    vo2DataPosition = 7;
                                }else{ //implies that bShuttle == 16
                                    vo2DataPosition = 8;
                                }
                                break;
                        }//End of switch statement
                        //Code to return the VO2 data
                        VO2Max = workingArray[vo2DataPosition];
                        Snackbar.make(view, "VO2 Calculated!", Snackbar.LENGTH_SHORT).show();
                    }else{
                        //Do nothing for this instance of i
                    }
                } //End of the loop

                String val = "VO2 Max: " + VO2Max;
                ((VO2Calculator)getActivity()).output.setText(val);

            }
        });

        return beepView;
    }
}