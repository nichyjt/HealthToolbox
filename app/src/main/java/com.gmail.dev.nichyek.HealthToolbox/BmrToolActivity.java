package com.gmail.dev.nichyek.HealthToolbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BmrToolActivity extends AppCompatActivity {

    //ToolId = 2
    boolean fabMenuActive = false;
    boolean isImperial = false;
    Double bmrValue = null;
    FullReportCardFileManager dataMgr;
    Date date = Calendar.getInstance().getTime();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmr_tool);
        setTitle("BMR Tool");
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        View view = findViewById(R.id.bmrToolLayout);
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



        //Instantiate UI components
        final EditText weightInput = findViewById(R.id.bmrWeightInput);
        final TextInputLayout weightLayout = findViewById(R.id.bmrWeightLayout);
        weightLayout.setHint("Weight (kg)");
        final EditText heightInput = findViewById(R.id.bmrHeightInput);
        final TextInputLayout heightLayout= findViewById(R.id.bmrHeightLayout);
        heightLayout.setHint("Height (cm)");
        final EditText heightInchesInput = findViewById(R.id.bmrHeightInchesInput);
        final TextInputLayout heightInchesLayout = findViewById(R.id.bmrHeightInchesLayout);
        heightInchesLayout.setHint("Inches (in)");
        heightInchesInput.setVisibility(View.INVISIBLE);
        heightInchesLayout.setVisibility(View.INVISIBLE);
        final EditText ageInput = findViewById(R.id.bmrAgeInput);
        TextInputLayout ageLayout = findViewById(R.id.bmrAgeLayout);
        ageLayout.setHint("Age");
        final RadioButton maleRadio = findViewById(R.id.maleButton);
        final RadioButton femaleRadio = findViewById(R.id.femaleButton);
        final Button calculateButton = findViewById(R.id.bmrCalculateButton);
        final TextView outputText = findViewById(R.id.outputText);
        FloatingActionButton fabMenu = findViewById(R.id.bmrFAB);
        final FloatingActionButton saveButton = findViewById(R.id.BMR_SaveButton);
        final FloatingActionButton loadButton = findViewById(R.id.BMR_LoadButton);
        saveButton.setVisibility(View.INVISIBLE);
        loadButton.setVisibility(View.INVISIBLE);
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabMenuActive = !fabMenuActive;
                if(fabMenuActive){
                    saveButton.show();
                    loadButton.show();
                }else{
                    saveButton.hide();
                    loadButton.hide();
                }
            }
        });
        dataMgr = new FullReportCardFileManager(getApplicationContext());
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bmrValue == null){
                    Snackbar.make(view, "Have you calculated a value yet?", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String fDate = sdf.format(date);
                FullReportCardItem bmrRecord = new FullReportCardItem(fDate, String.valueOf(bmrValue), FullReportCardItem.BMR_RECORD, null);
                dataMgr.saveHealthToolRecord(FullReportCardItem.BMR_RECORD, bmrRecord, null);
                Snackbar snackbar = Snackbar.make(view, "Daily Record saved!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FullReportCardActivity.class);
                intent.putExtra("type", FullReportCardItem.BMR_RECORD);
                startActivity(intent);
            }
        });


        Switch imperialSwitch = findViewById(R.id.bmrImperialSwitch);
        imperialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isImperial = b;
                if(b){ //Imperial on
                    heightInchesInput.setVisibility(View.VISIBLE);
                    heightInchesLayout.setVisibility(View.VISIBLE);
                    heightLayout.setHint("Feet (ft)");
                    weightLayout.setHint("Weight (lbs)");
                }else{ //Imperial off
                    heightInchesInput.setVisibility(View.INVISIBLE);
                    heightInchesLayout.setVisibility(View.INVISIBLE);
                    heightLayout.setHint("Height (cm)");
                    weightLayout.setHint("Weight (kg)");
                }
            }
        });


       calculateButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               boolean genderCheck = false;
               if(maleRadio.isChecked() || femaleRadio.isChecked()){
                   genderCheck = true;
               }

               if(isImperial){
                   if(weightInput.getText().toString().length()!=0&&ageInput.getText().toString().length()!=0
                   &&genderCheck&&(heightInchesInput.getText().toString().length()!=0||heightInput.getText().toString().length()!=0)){
                       Double weight = convertToKg(Double.parseDouble(weightInput.getText().toString()));
                       Double height = convertToCm(Double.parseDouble(heightInput.getText().toString()),Double.parseDouble(heightInchesInput.getText().toString()));
                       calculateBMR(weight, height, Integer.parseInt(ageInput.getText().toString()), maleRadio.isChecked());
                       outputText.setText(getOutputText());
                       Snackbar.make(view, "Calculated!", Snackbar.LENGTH_SHORT).show();
                       return;
                   }
               }else{
                   if(weightInput.getText().toString().length()!=0&&ageInput.getText().toString().length()!=0
                           &&genderCheck&&heightInput.getText().toString().length()!=0){
                       Double weight = Double.parseDouble(weightInput.getText().toString());
                       Double height = Double.parseDouble(heightInput.getText().toString());
                       calculateBMR(weight, height, Integer.parseInt(ageInput.getText().toString()), maleRadio.isChecked());
                       outputText.setText(getOutputText());
                       Snackbar.make(view, "Calculated!", Snackbar.LENGTH_SHORT).show();
                       return;
                   }
               }
               Snackbar.make(view, "Is everything filled in yet?", Snackbar.LENGTH_SHORT).show();
           }
       });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }else{
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    //calculate BMR (Mifflin St Jeor Eqn)
    private void calculateBMR(double weight, double height, int age, boolean male){
        DecimalFormat twodp = new DecimalFormat("#####.##");
        int genderConst = (male)? 5:-161;
        Double val = 10*weight+6.25*height-5*age+genderConst;
        bmrValue = Double.parseDouble(twodp.format(val));
    }

    private double convertToKg(double weight){
        return weight*0.453592;
    }
    private double convertToCm(double feet, double inches){
        return (feet*30.48)+(inches*2.54);
    }

    private String getOutputText(){
        return bmrValue + " kcal/day";
    }

}
