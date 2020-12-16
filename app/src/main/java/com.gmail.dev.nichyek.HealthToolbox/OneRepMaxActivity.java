package com.gmail.dev.nichyek.HealthToolbox;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class OneRepMaxActivity extends FragmentActivity {

    //WorkoutToolId=2
    double oneMaxRepVal;
    FragmentManager fm = getSupportFragmentManager();
    Fragment ormSetup = new OneRepMaxSetup();
    Fragment ormOutput = new OneRepMaxValues();
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_frame);
        fm.beginTransaction().replace(R.id.empty_frame_inner, ormSetup).commit();

        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("One Rep Max Helper");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(fm.findFragmentById(R.id.empty_frame_inner) instanceof OneRepMaxSetup && ormOutput!=null){
                    ((OneRepMaxValues)ormOutput).resetOneRepMaxValues();
                }
            }
        });
    }

    protected void startOneRMOutput(){
        Bundle ormVal = new Bundle();
        ormVal.putDouble("getORM", oneMaxRepVal);
        ormOutput.setArguments(ormVal);
        fm.beginTransaction().setCustomAnimations(R.anim.fade_in_fast, R.anim.fade_out_fast, R.anim.fade_in_fast, R.anim.slide_out).replace(ormSetup.getId(), ormOutput).addToBackStack(null).commit();
    }

    protected void setOrmValue(double ormValue){
      oneMaxRepVal = ormValue;
    }

}