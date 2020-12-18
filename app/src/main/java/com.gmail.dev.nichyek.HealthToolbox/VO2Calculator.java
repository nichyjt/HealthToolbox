package com.gmail.nichyekdev.healthtoolbox;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class VO2Calculator extends FragmentActivity {


    Fragment fBurger = new vo2burgerFragment();
    Fragment fAcsm = new vo2ACSMFragment();
    Fragment fCooper = new vo2cooperFragment();
    Fragment fBeep = new vo2beepFragment();
    RadioGroup vo2Options;
    RadioButton cooperBtn;
    RadioButton burgerBtn;
    RadioButton acsmBtn;
    TextView output;
    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vo2_calculator);

        output = findViewById(R.id.VO2output);
        cooperBtn = findViewById(R.id.vo2cooperbtn);
        burgerBtn = findViewById(R.id.vo2burgerbtn);
        acsmBtn = findViewById(R.id.vo2burgerbtn2);

        vo2Options = findViewById(R.id.vo2options);
        switchToBurger();
        vo2Options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(cooperBtn.isChecked()){
                    switchToCooper();
                }else if(burgerBtn.isChecked()){
                    switchToBurger();
                }else if(acsmBtn.isChecked()){
                    switchToAcsm();
                }else { //beep btn
                    switchToBeep();
                }
            }
        });
    }

    private void switchToCooper(){
        fm.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.vo2FragmentSpace, fCooper).commit();
    }
    private void switchToBurger(){
        fm.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.vo2FragmentSpace, fBurger).commit();
    }
    private void switchToAcsm(){
        fm.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.vo2FragmentSpace, fAcsm).commit();
    }
    private void switchToBeep(){
        fm.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.vo2FragmentSpace, fBeep).commit();
    }

}
