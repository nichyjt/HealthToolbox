package com.gmail.dev.nichyek.HealthToolbox;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


public class HeartRateActivity extends FragmentActivity {

    //ToolID = 3

    Fragment HeartRateTimerCountdown;
    Fragment HeartRateTimerSetup;
    FragmentManager fm = getSupportFragmentManager();
    Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_frame);
        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Heart Rate Timer");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        HeartRateTimerSetup =  new HeartRateTimerSetupFragment();
        fm.beginTransaction().replace(R.id.empty_frame_inner, HeartRateTimerSetup).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }else{
            super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void startHeartRateTimerCountdownFragment(int timerLength){
        HeartRateTimerCountdown = new HeartRateTimerCountdownFragment(timerLength);
        fm.beginTransaction().replace(HeartRateTimerSetup.getId(), HeartRateTimerCountdown).addToBackStack(null).commit();
    }


}
