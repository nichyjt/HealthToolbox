package com.gmail.dev.nichyek.HealthToolbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class CalorieBurnCalculatorActivity extends FragmentActivity {

    //WorkoutToolID=3

    //HashMaps used for retrieving and setting the exercises MET values, and their Names.
    //namemap for now is necessary. METCODE accesses exerciseName and METVAL for easier UI manipulation.
    //using arrays' positionIndex will be harder to maintain.
    HashMap<Integer, Double> metMap = new HashMap<>();
    HashMap<Integer, String> runNameMap = new HashMap<>();
    HashMap<Integer, String> cycleNameMap = new HashMap<>();
    HashMap<Integer, String> sportsNameMap = new HashMap<>();
    HashMap<Integer, String> trainingNameMap = new HashMap<>();
    HashMap<Integer, String> waterNameMap = new HashMap<>();
    HashMap<Integer, String> specialNameMap = new HashMap<>();

    //Unfortunately, all the METCODEs and METVALs are hardcoded.
    //Although vMETCODE[i] == vMETVAL[i], the following pairs are related by hashmap.
    //Lookup w HashMap for METVAL will be O(1). Using for(..) to find METCODE & METVAL will give O(n).
    int[] runMETCODE =  new int[]{12020,12040,12050,12060,12070,12080};
    double[] runMETVAL = new double[]{7.0,9.0,9.8,10.5,11.0,11.5};
    int[] cycleMETCODE = new int[]{1009,1020,1030,1040};
    double[] cycleMETVAL = new double[]{8.5,6.8,8.0,10.0};
    int[] sportsMETCODE = new int[]{15030,15055,15250,15255,15350,15562,15610,15660,15675,15720,15711};
    double[] sportsMETVAL = new double[]{5.5,6.5,8.0,4.8,7.8,6.3,7.0,4.0,7.3,3.0,6.0};
    int[] trainingMETCODE = new int[]{2020,2022,2024,2035,2040,2050,2054};
    double[] trainingMETVAL = new double[]{8.0,3.8,2.8,4.3,8.0,6.0,3.5};
    int[] specialMETCODE = new int[]{2105,2150,2160,2090,3031};
    double[] specialMETVAL = new double[]{3.0,2.5,4.0,6.0,7.8};
    int[] waterMETCODE = new int[]{18100,18130,18230,18240,18250,18260,18265,18270,18360,18366};
    double[] waterMETVAL = new double[]{5.0,4.5,9.8,5.8,9.5,10.3,5.3,13.8,10.0,9.8};

    double METVAL = 0;
    int activeMETCODE = 0;
    int exerciseTypePosition = 0;
    boolean customMET = false;
    boolean fabOn = false;
    int calBurnt = -1;

    FullReportCardFileManager fileManager;
    Date date = Calendar.getInstance().getTime();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_calorie_burn_calculator);
        fileManager = new FullReportCardFileManager(getApplicationContext());

        //UI Shenanigans
        final EditText weightInput = findViewById(R.id.calorieCalc_WeightInput);
        weightInput.setSelected(false);
        final EditText timeInput = findViewById(R.id.calorieCalc_TimeInput);
        final TextView outputText = findViewById(R.id.calorieCalc_OutputText);
        final TextView customExText =  findViewById(R.id.calorieCalc_customText);
        customExText.setVisibility(View.INVISIBLE);
        final EditText customMETInput = findViewById(R.id.calorieCalc_customMET);
        customMETInput.setVisibility(View.INVISIBLE);
        final CardView cardView = findViewById(R.id.calorieCalc_ExerciseListCardView);
        final TextView exerciseConfirmation = findViewById(R.id.calorieCalc_ExerciseConfirmationText);

        TextInputLayout weightLayout = findViewById(R.id.calorieCalc_WeightInputLayout);
        weightLayout.setHint("Weight (kg)");
        TextInputLayout timeLayout = findViewById(R.id.calorieCalc_TimeInputLayout);
        timeLayout.setHint("Duration (mins)");
        final TextInputLayout customLayout = findViewById(R.id.textInputLayout);
        customLayout.setVisibility(View.INVISIBLE);

        //OPTIONS UI
        final ListView listView = findViewById(R.id.calorieCalc_ExerciseListContainer);
        final ArrayList<CalorieBurnCalculator_ExerciseItem> exerciseList = new ArrayList<>();
        final ArrayAdapter<CalorieBurnCalculator_ExerciseItem> adapter = new CalorieBurnCalculator_ExerciseListAdapter(this, exerciseList);
        listView.setAdapter(adapter);

        //SPINNER UI
        final Spinner exerciseType = findViewById(R.id.calorieCalc_ExerciseType);
        String[] strlst = (getResources().getStringArray(R.array.calorieCalc_ExerciseTypes));
        final ArrayList<String> exerciseTypes = new ArrayList<>(Arrays.asList(strlst));
        final CalorieBurnSpinnerAdapter mAdapter =  new CalorieBurnSpinnerAdapter(getApplicationContext(), R.layout.spinner_dropdownitem, exerciseTypes);
        exerciseType.setAdapter(mAdapter);

        final View mView = findViewById(R.id.calorieCalc_LayoutContainer);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSoftKeyboard();
            }
        });


        final Button calculateBtn = findViewById(R.id.calorieCalculateButton);
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(customMET){
                    if(customMETInput.getText().toString().trim().length() == 0){
                        outputText.setText("Is everything filled in?");
                        Snackbar.make(view, "Is everything filled in?", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    METVAL = Double.parseDouble(customMETInput.getText().toString());
                }
                double weightInKg;
                double timeInMin;
                if(METVAL == 0 || weightInput.getText().toString().trim().length() == 0 || timeInput.getText().toString().trim().length() == 0){
                    outputText.setText("Is everything filled in?");
                    Snackbar.make(view, "Is everything filled in?", Snackbar.LENGTH_SHORT).show();
                }else{
                    weightInKg = Double.parseDouble(weightInput.getText().toString());
                    timeInMin = Double.parseDouble(timeInput.getText().toString());
                    calBurnt = (int) Math.round(METVAL*weightInKg*timeInMin/60);
                    String output = calBurnt + "kcal burnt.";
                    outputText.setText(output);
                }
            }
        });
        FloatingActionButton fabMenu = findViewById(R.id.calorieCalcFAB);
        final FloatingActionButton saveBtn = findViewById(R.id.calorieCalc_SaveButton);
        final FloatingActionButton loadBtn = findViewById(R.id.calorieCalc_LoadButton);
        saveBtn.setVisibility(View.INVISIBLE);
        loadBtn.setVisibility(View.INVISIBLE);
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabOn = !fabOn;
                if(fabOn){
                    saveBtn.show();
                    loadBtn.show();
                }else{
                    saveBtn.hide();
                    loadBtn.hide();
                }
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(calBurnt == -1){
                    outputText.setText("Is everything filled in?");
                    Snackbar.make(view, "Is everything filled in?", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String fDate = sdf.format(date);
                FullReportCardItem item = new FullReportCardItem(fDate, String.valueOf(calBurnt), FullReportCardItem.CALORIE_BURN_RECORD, null);
                String exerciseName = "";
                switch(exerciseTypePosition){
                    case 0:
                        exerciseName = runNameMap.get(activeMETCODE);
                        break;
                    case 1:
                        exerciseName = cycleNameMap.get(activeMETCODE);
                        break;
                    case 2:
                        exerciseName = sportsNameMap.get(activeMETCODE);
                        break;
                    case 3:
                        exerciseName = trainingNameMap.get(activeMETCODE);
                        break;
                    case 4:
                        exerciseName = waterNameMap.get(activeMETCODE);
                        break;
                    case 5:
                        exerciseName = specialNameMap.get(activeMETCODE);
                        break;
                    default:
                        exerciseName = "Other Exercise";
                }
                item.exerciseName = exerciseName;
                fileManager.saveHealthToolRecord(FullReportCardItem.CALORIE_BURN_RECORD, item, null);
                Snackbar.make(view, "Record saved!", Snackbar.LENGTH_SHORT).show();
            }
        });
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FullReportCardActivity.class);
                intent.putExtra("type", FullReportCardItem.CALORIE_BURN_RECORD);
                startActivity(intent);
            }
        });


        //Spinner Listener
        exerciseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                customMET = false;
                int arrID = 0;
                int optionsToAdd = 0;
                int[] currMETCODEarray = null;
                exerciseList.clear();
                cardView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
                customLayout.setVisibility(View.INVISIBLE);
                customExText.setVisibility(View.INVISIBLE);
                customMETInput.setVisibility(View.INVISIBLE);

                exerciseConfirmation.setVisibility(View.VISIBLE);
                exerciseTypePosition = position;
                switch (position) {
                    case 0: //Running
                        arrID = R.array.calorieCalc_Ex_RunNames;
                        optionsToAdd = runMETCODE.length;
                        currMETCODEarray = runMETCODE;
                        if (runNameMap.isEmpty()) {
                            setMapData(position, arrID);
                        }
                        break;
                    case 1: //Cycling
                        arrID = R.array.calorieCalc_Ex_CycleNames;
                        optionsToAdd = cycleMETCODE.length;
                        currMETCODEarray = cycleMETCODE;
                        if (cycleNameMap.isEmpty()) {
                            setMapData(position,arrID);
                        }
                        break;
                    case 2: //Sports
                        arrID = R.array.calorieCalc_Ex_SportsNames;
                        optionsToAdd = sportsMETCODE.length;
                        currMETCODEarray = sportsMETCODE;
                        if (sportsNameMap.isEmpty()) {
                            setMapData(position,arrID);
                        }
                        break;
                    case 3: //Training
                        arrID = R.array.calorieCalc_Ex_TrainingNames;
                        optionsToAdd = trainingMETCODE.length;
                        currMETCODEarray = trainingMETCODE;
                        if (trainingNameMap.isEmpty()) {
                            setMapData(position, arrID);
                        }
                        break;
                    case 4: //Water
                        arrID = R.array.calorieCalc_Ex_Water;
                        optionsToAdd = waterMETCODE.length;
                        currMETCODEarray = waterMETCODE;
                        if (waterNameMap.isEmpty()) {
                            setMapData(position, arrID);
                        }
                        break;
                    case 5: // Special
                        arrID = R.array.calorieCalc_Ex_SpecialNames;
                        optionsToAdd = specialMETCODE.length;
                        currMETCODEarray = specialMETCODE;
                        if (specialNameMap.isEmpty()) {
                            setMapData(position, arrID);
                        }
                        break;
                    case 6: //Custom MET Value
                        cardView.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.INVISIBLE);
                        customExText.setVisibility(View.VISIBLE);
                        customMETInput.setVisibility(View.VISIBLE);
                        customLayout.setVisibility(View.VISIBLE);
                        exerciseConfirmation.setVisibility(View.INVISIBLE);
                        customMET = true;
                        closeSoftKeyboard();
                        return;
                }
                //Populate the ListView
                final String[] mArr = getResources().getStringArray(arrID);
                for(int i=0; i<optionsToAdd; i++){
                    //final int currMETCODE = currentExerciseMap.get(mArr[i]);
                    final int currMETCODE = currMETCODEarray[i];
                    CalorieBurnCalculator_ExerciseItem item = new CalorieBurnCalculator_ExerciseItem(mArr[i], currMETCODE);
                    exerciseList.add(item);
                }
                adapter.notifyDataSetChanged();
                // for debug: outputText.setText(metMap.toString());
                closeSoftKeyboard();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mView.performClick();
            }
        });


    }


    //This fn only builds parts of the hmap when called.
    // Hopefully this reduces memory load, compared to building everything from the start
    private void setMapData(int exerciseType, int myArrID){
        HashMap<Integer, String> chosenMap;
        int[] referredMETCODE;
        double[] metValues;
        switch(exerciseType){
            case 0: //Running (Have to hardcode the METCODE values)
                referredMETCODE = runMETCODE;
                chosenMap = runNameMap;
                metValues = runMETVAL;
                break;
            case 1: //Cycling
                referredMETCODE = cycleMETCODE;
                chosenMap = cycleNameMap;
                metValues = cycleMETVAL;
                break;
            case 2: //Sports
                referredMETCODE = sportsMETCODE;
                chosenMap = sportsNameMap;
                metValues = sportsMETVAL;
                break;
            case 3: //Training
                referredMETCODE = trainingMETCODE;
                chosenMap = trainingNameMap;
                metValues = trainingMETVAL;
                break;
            case 4: //Water
                referredMETCODE = waterMETCODE;
                chosenMap = waterNameMap;
                metValues = waterMETVAL;
                break;
            case 5: //Special
                referredMETCODE = specialMETCODE;
                chosenMap = specialNameMap;
                metValues = specialMETVAL;
                break;
            default:
                //Machine generated; If exerciseType !{0..4}, error is thrown
                throw new IllegalStateException("Unexpected value: " + exerciseType);
        }
        String[] arr = getResources().getStringArray(myArrID);
        for(int i=0; i<arr.length; i++){
            //Populate exerciseTypeMap and metValuesMap
            chosenMap.put(referredMETCODE[i], arr[i]);
            metMap.put(referredMETCODE[i], metValues[i]);
        }
    }

    public void setMETCODE(int metcode){
        activeMETCODE = metcode;
        METVAL = metMap.get(metcode);
    }

    private void closeSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(getCurrentFocus()!= null){
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
