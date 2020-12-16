package com.gmail.dev.nichyek.HealthToolbox;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BloodPressureTracker extends AppCompatActivity {

    //Health Tool
    //This activity aims to assist in logging down of blood pressure (BP)
    //as well as give indication on healthy-unhealthy levels of BP

    Date date = Calendar.getInstance().getTime();
    int currentCat = 0;
    boolean fabActive = false;
    FullReportCardFileManager dataMgr;
    ActionBar actionBar;

    EditText upperInput;
    EditText lowerInput;
    TextSwitcher output;
    String BP_Value;
    Button calculateBtn;
    Button showChartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_tracker);
        final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        dataMgr = new FullReportCardFileManager(getApplicationContext());
        setTitle("Blood Pressure Tracker");
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        //UI
        upperInput = findViewById(R.id.bloodPressure_UpperInput);
        upperInput.setText("120");
        lowerInput = findViewById(R.id.bloodPressure_LowerInput);
        lowerInput.setText("80");
        upperInput.setSelectAllOnFocus(true);
        lowerInput.setSelectAllOnFocus(true);
        final FloatingActionButton save = findViewById(R.id.BP_SaveButton);
        final FloatingActionButton load = findViewById(R.id.BP_LoadButton);
        FloatingActionButton menu = findViewById(R.id.bpFAB);
        View view = findViewById(R.id.bloodPressure_layout);

        showChartBtn = findViewById(R.id.bloodPressure_helperBtn);
        //final CardView chartCard = findViewById(R.id.bloodPressureChartCard);
        //chartCard.setVisibility(View.INVISIBLE);
        showChartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_BloodPressureInfo dialog_bloodPressureInfo = new Dialog_BloodPressureInfo();
                dialog_bloodPressureInfo.show(getSupportFragmentManager(), "DIALOG_BPINFO");
            }
        });

        calculateBtn = findViewById(R.id.bp_checkReference);
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBPValue();
                if(BP_Value.equals("")){
                    Snackbar.make(view, "Is everything filled up properly?", Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(view, "Reference updated!", Snackbar.LENGTH_SHORT).show();
                }
                if(getCurrentFocus()!=null){
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        });

        save.setVisibility(View.INVISIBLE);
        load.setVisibility(View.INVISIBLE);

        //Output logic
        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_fast);
        Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out_fast);
        output = findViewById(R.id.bloodPressure_output);
        output.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getApplicationContext());
                textView.setTextSize(34);
                textView.setGravity(Gravity.TOP|Gravity.CENTER);
                return textView;
            }
        });
        output.setInAnimation(in);
        output.setOutAnimation(out);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getCurrentFocus()!=null){
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabActive = !fabActive;
                if(fabActive){
                    save.show();
                    load.show();
                }else{
                    save.hide();
                    load.hide();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBPValue();
                if(BP_Value.equals("")){
                    Snackbar.make(view, "Is everything filled up properly?", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String fDate = sdf.format(date);
                FullReportCardItem item = new FullReportCardItem(fDate, BP_Value, FullReportCardItem.BP_RECORD, currentCat);
                dataMgr.saveHealthToolRecord(FullReportCardItem.BP_RECORD, item, null);
                Snackbar snackbar = Snackbar.make(view, "Daily Record Saved!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FullReportCardActivity.class);
                intent.putExtra("type", FullReportCardItem.BP_RECORD);
                startActivity(intent);
            }
        });

    }//end of onCreate

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }else{
            super.onOptionsItemSelected(item);
        }
        return true;
    }

    private int getBPCatLevel(int upper, int lower){
        int cat1;
        int cat2;
        if(upper <=90){
            cat1 = 0;
        }else if(upper<=120){
            cat1 = 1;
        }else if (upper<130){
            cat1 = 2;
        }else if (upper<140){
            cat1 = 3;
        }else if (upper<180){
            cat1 = 4;
        }else{
            cat1 = 5;
        }
        if(lower <= 60){
            cat2 = 0;
        }else if(lower<80){ //note, no cat 1 for lower val.
            cat2 = 1;
        }else if (lower<90){
            cat2 = 3;
        }else if (upper<120){
            cat2 = 4;
        }else{
            cat2 = 5;
        }
        return Math.max(cat1, cat2);
    }


    private void setBPValue() {
        if (upperInput.getText().toString().length() == 0 || lowerInput.getText().toString().length() == 0) {
            BP_Value = "";
            return;
        }
        int upperVal = Integer.parseInt(upperInput.getText().toString());
        int lowerVal = Integer.parseInt(lowerInput.getText().toString());
        currentCat = getBPCatLevel(upperVal, lowerVal);
        switch (currentCat) {
            case 0:
                output.setText("Low Blood Pressure");
                ((TextView)output.getCurrentView()).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.skyBlue));
                //output.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.skyBlue));
                break;
            case 1:
                output.setText("Healthy Range");
                ((TextView)output.getCurrentView()).setTextColor(Color.parseColor("#2ECC71"));
                //output.setTextColor(Color.parseColor("#2ECC71"));
                break;
            case 2:
                output.setText("Elevated Range");
                ((TextView)output.getCurrentView()).setTextColor(Color.parseColor("#F1C40F"));
                //output.setTextColor(Color.parseColor("#F1C40F"));
                break;
            case 3:
                output.setText("Hypertension Stage 1");
                ((TextView)output.getCurrentView()).setTextColor(Color.parseColor("#E59866"));
                //output.setTextColor(Color.parseColor("#E59866"));
                break;
            case 4:
                output.setText("Hypertension Stage 2");
                ((TextView)output.getCurrentView()).setTextColor(Color.parseColor("#EF5350"));
                //output.setTextColor(Color.parseColor("#EF5350"));
                break;
            case 5:
                output.setText("Hypertentive Crisis");
                ((TextView)output.getCurrentView()).setTextColor(Color.parseColor("#D32F2F"));
                //output.setTextColor(Color.parseColor("#D32F2F"));
                break;
        }
        BP_Value = upperVal + "/" + lowerVal;
    }


}