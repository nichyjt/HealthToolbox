package com.gmail.dev.nichyek.HealthToolbox;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class BmiToolActivity extends AppCompatActivity {
    //ToolId = 1;

    int cat;
    FullReportCardFileManager dataMgr;
    Double bmiValue = null;
    boolean isImperial = false;
    boolean fabOn = false;
    TextSwitcher outputSwitcher;
    TextSwitcher descriptionSwitcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataMgr = new FullReportCardFileManager(getApplicationContext());
        setContentView(R.layout.activity_bmi_tool);
        setTitle("BMI Tool");

        //Create top toolbar
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        final EditText heightInput = findViewById(R.id.heightInput);
        final TextInputLayout heightLayout = findViewById(R.id.heightInputLayout);
        final EditText heightInches = findViewById(R.id.heightInputInches);
        heightInches.setVisibility(View.GONE);
        final TextInputLayout heightInchesLayout = findViewById(R.id.heightInputLayoutInches);
        heightLayout.setHint("Height (cm)");
        heightInchesLayout.setHint("Inches (in)");
        final EditText weightInput = findViewById(R.id.weightInput);
        final TextInputLayout weightInputLayout = findViewById(R.id.bmiWeightInputLayout);
        weightInputLayout.setHint("Weight (kg)");
        FloatingActionButton fabMenu = findViewById(R.id.bmiFAB);
        final FloatingActionButton saveBtn = findViewById(R.id.BMI_SaveButton);
        final FloatingActionButton loadBtn = findViewById(R.id.BMI_LoadButton);
        saveBtn.setVisibility(View.INVISIBLE);
        loadBtn.setVisibility(View.INVISIBLE);

        //Switcher Logic
        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_fast);
        Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out_fast);
        outputSwitcher = findViewById(R.id.bmiOutputTextSwitcher);
        descriptionSwitcher = findViewById(R.id.bmiDescriptionTextSwitcher);
        outputSwitcher.setInAnimation(in);
        outputSwitcher.setOutAnimation(out);
        outputSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getApplicationContext());
                textView.setTextSize(70);
                textView.setGravity(Gravity.CENTER);
                return textView;
            }
        });
        descriptionSwitcher.setInAnimation(in);
        descriptionSwitcher.setOutAnimation(out);
        descriptionSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getApplicationContext());
                textView.setTextSize(24);
                textView.setGravity(Gravity.CENTER);
                return textView;
            }
        });

        final CardView outputCardView = findViewById(R.id.outputCardView);

        ((Switch)findViewById(R.id.bmiImperialSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isImperial = true;
                    heightInchesLayout.setVisibility(View.VISIBLE);
                    heightInches.setVisibility(View.VISIBLE);
                    heightLayout.setHint("Feet (ft)");
                    weightInputLayout.setHint("Weight (lbs)");
                }else{
                    isImperial = false;
                    heightInchesLayout.setVisibility(View.INVISIBLE);
                    heightInches.setVisibility(View.INVISIBLE);
                    heightLayout.setHint("Height (cm)");
                    weightInputLayout.setHint("Weight (kg)");
                }
            }
        });

        final View view = findViewById(R.id.bmiToolActivity);
        view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Code to hide keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if(getCurrentFocus()!=null){
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            }
        });

        weightInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    view.performClick();
                }
                return false;
            }
        });

        //Save-Load functions
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bmiValue==null){
                    Snackbar.make(view, "Did you calculate a BMI yet?", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String fDate = sdf.format(date);
                FullReportCardItem bmiRecord = new FullReportCardItem(fDate, String.valueOf(bmiValue), FullReportCardItem.BMI_RECORD, cat);
                dataMgr.saveHealthToolRecord(FullReportCardItem.BMI_RECORD, bmiRecord, null);
                Snackbar snackbar = Snackbar.make(view, "Daily Record Saved!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FullReportCardActivity.class);
                intent.putExtra("type", FullReportCardItem.BMI_RECORD);
                startActivity(intent);
            }
        });

        //Calculate btn
        findViewById(R.id.bmiCalculateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(!isImperial){
                    if(weightInput.getText().toString().trim().length() != 0 && heightInput.getText().toString().trim().length() != 0){
                        double weight = Double.parseDouble(weightInput.getText().toString());
                        double height = Double.parseDouble(heightInput.getText().toString());
                        calculateBMI(height, weight);
                        setBMIOutputUI();
                        Snackbar.make(view, "BMI Calculated!", Snackbar.LENGTH_SHORT).show();
                    }else{
                        //error message
                        Snackbar.make(view, "Is everything filled up yet?", Snackbar.LENGTH_SHORT).show();
                    }
                }else{
                    if(weightInput.getText().toString().trim().length() != 0 &&
                            (heightInput.getText().toString().trim().length()!=0 || heightInches.getText().toString().trim().length()!=0)){
                        //calculate bmi
                        Double feet = (heightInput.getText().toString().length()==0)? 0:Double.parseDouble(heightInput.getText().toString());
                        Double inches = (heightInches.getText().toString().length()==0)? 0:Double.parseDouble(heightInches.getText().toString());
                        double weight = convertWeightToMetric(Double.parseDouble(weightInput.getText().toString()));
                        double height = convertHeightToMetric(feet, inches);
                        calculateBMI(height, weight);
                        setBMIOutputUI();
                    }else{
                        Snackbar.make(view, "Is everything filled up yet?", Snackbar.LENGTH_SHORT).show();
                    }
                }

                if(outputCardView.getVisibility()==View.INVISIBLE){
                    outputCardView.setVisibility(View.VISIBLE);
                }



            }
        });

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


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private String[] getBmiParams(double bmi){
        //output contains output text + textColor
        String[] output = new String[2];
        int index = 0;
        //Other ranges will be added in next time, but for now we will use WHO definitions
        double[] whoBmiRange = {40,35,30,25,18.5,16,15,0};
        for(double bmiFlag: whoBmiRange){
            if(bmi>=bmiFlag){
                break;
            }
            index++;
        }
        cat = index;

        switch(index){
            case 0:
                output[0] = "Very Severely Obese";
                output[1] = "#FF0000";
                break;
            case 1:
                output[0] = "Severely Obese";
                output[1] = "#FF0000";
                break;
            case 2:
                output[0] = "Obese";
                output[1] = "#FF0000";
                break;
            case 3:
                output[0] = "Overweight";
                output[1] = "#FFA500";
                break;
            case 4:
                output[0] = "Healthy";
                output[1] = "#008000";
                break;
            case 5:
                output[0] = "Underweight";
                output[1] = "#FFA500";
                break;
            case 6:
                output[0] = "Severely Underweight";
                output[1] = "#FF0000";
                break;
            case 7:
                output[0] = "Very Severely Underweight";
                output[1] = "#FF0000";
                break;
        }
        return output;
    }

    private void calculateBMI(double height, double weight){
        double heightConverted = height/100;
        double htsq = (heightConverted*heightConverted);
        double unroundedBmiValue = weight/htsq;
        DecimalFormat twoDecimalPlaces = new DecimalFormat("##.##");
        bmiValue = Double.parseDouble(twoDecimalPlaces.format(unroundedBmiValue));
    }


    private double convertWeightToMetric(double pounds) {
        return pounds*0.453592;
    }

    //in cm
    private double convertHeightToMetric(double feet, double inches){
        return (feet*30.48)+(inches*2.54);
    }

    private void setBMIOutputUI(){
        String[] bmiParams = getBmiParams(bmiValue);
        //TextView bmiDescription = findViewById(R.id.bmiDescription);
        //TextView bmiValOutput = findViewById(R.id.bmiOutput);
        //bmiDescription.setText(bmiParams[0]);
        outputSwitcher.setText(String.valueOf(bmiValue));
        ((TextView)outputSwitcher.getCurrentView()).setTextColor(Color.parseColor(bmiParams[1]));
        //bmiDescription.setTextColor(Color.parseColor(bmiParams[1]));
        descriptionSwitcher.setText(String.valueOf(bmiParams[0]));
        ((TextView)descriptionSwitcher.getCurrentView()).setTextColor(Color.parseColor(bmiParams[1]));
        //bmiValOutput.setText(String.valueOf(bmiValue));
        //bmiValOutput.setTextColor(Color.parseColor(bmiParams[1]));
    }




}
