package com.gmail.nichyekdev.healthtoolbox;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BodyFatPercentage extends AppCompatActivity {

    //ToolId = 5
    boolean isImperial = false;
    boolean fabMenuActive = false;
    Double bfpValue = null;
    FullReportCardFileManager dataMgr;
    Date date = Calendar.getInstance().getTime();
    EditText heightInput;
    EditText inchesInput;
    EditText waistInput;
    EditText hipInput;
    EditText neckInput;
    TextView outputText;
    TextInputLayout heightLayout;
    TextInputLayout inchesLayout;
    TextInputLayout waistLayout;
    TextInputLayout hipLayout;
    TextInputLayout necklayout;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_fat_percentage);
        setTitle("BFP Calculator");
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        constraintLayout = findViewById(R.id.bfpMainLayout);
        dataMgr = new FullReportCardFileManager(getApplicationContext());

        heightInput = findViewById(R.id.bfpHeightInput);
        inchesInput = findViewById(R.id.bfpHeightInchesInput);
        waistInput = findViewById(R.id.bfpWaistInput);
        hipInput = findViewById(R.id.bfpHipInput);
        neckInput = findViewById(R.id.bfpNeckInput);
        outputText = findViewById(R.id.outputText);
        heightInput.addTextChangedListener(new bfpTextWatcher(heightInput));
        waistInput.addTextChangedListener(new bfpTextWatcher(waistInput));
        hipInput.addTextChangedListener(new bfpTextWatcher(hipInput));
        neckInput.addTextChangedListener(new bfpTextWatcher(neckInput));

        heightLayout = findViewById(R.id.bfpHeightLayout);
        inchesLayout = findViewById(R.id.bfpHeightInchesLayout);
        waistLayout = findViewById(R.id.bfpWaistLayout);
        hipLayout = findViewById(R.id.bfpHipLayout);
        necklayout = findViewById(R.id.bfpNeckLayout);
        heightLayout.setHint("Height (cm)");
        inchesLayout.setHint("Inches (in)");
        inchesLayout.setVisibility(View.INVISIBLE);
        hipLayout.setVisibility(View.INVISIBLE);
        necklayout.setHint("Neck (cm)");
        hipLayout.setHint("Hip (cm)");
        waistLayout.setHint("Waist (cm)");

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            @Override
            public void onClick(View view) {
                if(getCurrentFocus()!=null){
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        });

        final RadioButton isMale = findViewById(R.id.maleButton);
        isMale.setChecked(true);
        final RadioButton isFemale = findViewById(R.id.femaleButton);
        RadioGroup genderGroup = findViewById(R.id.radioGroup);
        Button calculateButton = findViewById(R.id.bfpCalculateButton);

        FloatingActionButton fabMenu = findViewById(R.id.bfpFAB);
        final FloatingActionButton saveButton = findViewById(R.id.bfp_SaveButton);
        final FloatingActionButton loadButton = findViewById(R.id.bfp_LoadButton);
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bfpValue == null){
                    Snackbar.make(outputText, "Did you fill up everything yet?", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String fDate = sdf.format(date);
                String value = bfpValue + "%";
                FullReportCardItem item = new FullReportCardItem(fDate, value, FullReportCardItem.BFP_RECORD, null);
                dataMgr.saveHealthToolRecord(FullReportCardItem.BFP_RECORD, item, null);
                Snackbar snackbar = Snackbar.make(view, "Daily record saved!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FullReportCardActivity.class);
                intent.putExtra("type", FullReportCardItem.BFP_RECORD);
                startActivity(intent);
            }
        });

        //Imperial option makes imperial inputs visible and change some hints
        final Switch imperialOption = findViewById(R.id.bfpImperialSwitch);

        //Gender option should make stuff visible
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(isMale.isChecked()){
                    //Hide female specific inputs
                    hipLayout.setVisibility(View.INVISIBLE);
                }else if(isFemale.isChecked()){
                    //Show female specific inputs
                    hipLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        imperialOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isImperial = b;
                if(b){
                    heightLayout.setHint("Height (ft)");
                    inchesLayout.setVisibility(View.VISIBLE);
                    necklayout.setHint("Neck (in)");
                    hipLayout.setHint("Hip (in)");
                    waistLayout.setHint("Waist (in)");
                }else{
                    heightLayout.setHint("Height (cm)");
                    inchesLayout.setVisibility(View.INVISIBLE);
                    necklayout.setHint("Neck (cm)");
                    hipLayout.setHint("Hip (cm)");
                    waistLayout.setHint("Waist (cm)");
                }
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(heightInput.getText().toString().length()==0||neckInput.getText().toString().length()==0||waistInput.getText().toString().length()==0){
                    createAlertToast();
                    return;
                }
                double height = Double.parseDouble(heightInput.getText().toString());
                double neck = Double.parseDouble(neckInput.getText().toString());
                double waist = Double.parseDouble(waistInput.getText().toString());
                double inches = (inchesInput.getText().toString().length()==0)? 0:Double.parseDouble(inchesInput.getText().toString());

                if(isMale.isChecked()){
                    //Catch Math errors
                    // Waist<=Neck, Ht=0,
                    if((waist-neck)<=0){
                        Snackbar.make(outputText, "Math Error! Did you input everything correctly?", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    bfpValue = findMaleBFP(height, neck, waist, inches);
                }else{
                    if(hipInput.getText().toString().length()==0){
                        createAlertToast();
                        return;
                    }
                    double hip = Double.parseDouble(hipInput.getText().toString());
                    //Catch Math errors
                    // Waist+hip<Neck, Ht=0,
                    if((waist+hip-neck<=0)){
                        Snackbar.make(outputText, "Math Error! Did you input everything correctly?", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    bfpValue = findFemaleBFP(height, neck, waist, hip, inches);
                }

                //output text logic
                String output = bfpValue + "%";
                outputText.setText(output);
                Snackbar.make(view, "Calculated!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }//end of onCreateView

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }else{
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void createAlertToast(){
        Snackbar.make(outputText, "Did you fill up everything yet?", Snackbar.LENGTH_SHORT).show();
    }

    private double convertCmToInches(double cmInput){
        return cmInput/2.54;
    }

    //This method returns male BFP
    private double findMaleBFP(double height, double neck, double waist, double inches) {
        double nk = neck;
        double wst = waist;
        double totalHt = 12*height + inches;

        if (!isImperial) {
            totalHt = convertCmToInches(height);
            nk = convertCmToInches(nk);
            wst = convertCmToInches(wst);
        }
        //Bigdecimal used for accuracy.
        BigDecimal log10height = BigDecimal.valueOf(Math.log10(totalHt));
        BigDecimal log10abdomenMinusNeck = BigDecimal.valueOf(Math.log10(wst - nk));
        //Navy formula, where X = BFP raw, A = lg(Ab-Neck)*86.010, B = 70.041*lg(ht) , C = 36.76
        BigDecimal A = log10abdomenMinusNeck.multiply(BigDecimal.valueOf(86.01));
        BigDecimal B = log10height.multiply(BigDecimal.valueOf(70.041));
        BigDecimal C = BigDecimal.valueOf(36.76);
        BigDecimal X = A.subtract(B).add(C);
        Double x = X.doubleValue();
        return oneDecimalPlace(x);
    }

    //This method returns female BFP
    private double findFemaleBFP(double height, double neck, double waist, double hip, double inches){
        double totalHt = 12*height + inches;
        double nk = neck;
        double wst = waist;
        double hp = hip;
        if(!isImperial){
            totalHt = convertCmToInches(height);
            nk = convertCmToInches(neck);
            wst = convertCmToInches(waist);
            hp = convertCmToInches(hip);
        }
        //Use bigdecimal for accuracy.
        BigDecimal log10height = BigDecimal.valueOf(Math.log10(totalHt));
        BigDecimal log10abdomenPlusHipMinusNeck = BigDecimal.valueOf(Math.log10(wst + hp - nk));
        //Navy formula, where X = BFP raw, A = lg(Ab+Hip-Neck)*163.205, B = 97.684*lg(ht) , C = -78.387
        BigDecimal A = log10abdomenPlusHipMinusNeck.multiply(BigDecimal.valueOf(163.205));
        BigDecimal B = log10height.multiply(BigDecimal.valueOf(97.684));
        BigDecimal C = BigDecimal.valueOf(78.387);
        //Added in an intermediary Y because I suspect calling multiple .subtract() causes the wrong value to be subtracted.
        BigDecimal Y = A.subtract(B);
        BigDecimal X = Y.subtract(C);
        Double x = X.doubleValue();
        return oneDecimalPlace(x);
    }

    private double oneDecimalPlace(Double i){
        double value = 0.0;
        DecimalFormat oneDp = new DecimalFormat("##.#");
        if(i>0 && i<100){
            value = Double.parseDouble(oneDp.format(i));
        } return value;
    }

    private static class bfpTextWatcher implements android.text.TextWatcher{
        EditText editText;
        public bfpTextWatcher(EditText editText){
            this.editText = editText;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length()==0||Double.parseDouble(editable.toString())==0){
                editText.setText("1");
                editText.selectAll();
            }
        }
    }
}