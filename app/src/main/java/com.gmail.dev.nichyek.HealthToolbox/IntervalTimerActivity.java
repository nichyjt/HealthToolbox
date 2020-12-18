package com.gmail.nichyekdev.healthtoolbox;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

//Main Coordinator for fragment transactions
public class IntervalTimerActivity extends FragmentActivity {

    //Setup fragments for fm to manipulate
    Fragment IntervalTimerMenu = new IntervalTimerMenu();
    Fragment IntervalTimerSetup = new IntervalTimerSetup(null, null);
    Fragment IntervalTimerCountdown;
    FragmentManager fm = getSupportFragmentManager();
    Toolbar toolbar;
    ArrayList<Interval_Item> tabataRoutine = getTabataRoutine();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Loads up setup fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_frame);
        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Choose an Interval Workout");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        fm.beginTransaction().replace(R.id.empty_frame_inner, IntervalTimerMenu, "INTERVAL_TIMER_MENU_FRAG").commit();
        //Check currently displayed fragment and execute countdown cancel if necessary.
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment frag =  fm.findFragmentById(R.id.empty_frame_inner);
                if(IntervalTimerCountdown!=null && !(frag instanceof IntervalTimerCountdown)){
                    ((IntervalTimerCountdown)IntervalTimerCountdown).disableIntervalCountdown();
                }
                if(frag instanceof IntervalTimerMenu){
                    toolbar.setVisibility(View.VISIBLE);
                    toolbar.setTitle("Choose an Interval Workout");
                }
            }
        });

    }


    //replace setup fragment with countdown fragment
    public void startCountdownFragment(@Nullable ArrayList<Interval_Item> routine){
        if(routine == null){
            routine = tabataRoutine;
            IntervalTimerCountdown = new IntervalTimerCountdown(routine);
            ((IntervalTimerCountdown)IntervalTimerCountdown).isTabata = true;
            fm.beginTransaction().replace(IntervalTimerMenu.getId(), IntervalTimerCountdown, "TABATA_COUNTDOWN_FRAGMENT").addToBackStack(null).commit();

        }else{
            IntervalTimerCountdown = new IntervalTimerCountdown(routine);
            ((IntervalTimerCountdown)IntervalTimerCountdown).isTabata = false;
            fm.beginTransaction().replace(IntervalTimerSetup.getId(), IntervalTimerCountdown).addToBackStack(null).commit();
        }
        toolbar.setVisibility(View.GONE);
        ((IntervalTimerCountdown)IntervalTimerCountdown).enableIntervalCountdown();

    }

    public void startIntervalSetupFragment(@Nullable ArrayList<Interval_Item> routine, String routineName, @Nullable Integer routineIndex){
        IntervalTimerSetup = new IntervalTimerSetup(routineName, routineIndex);
        if(routine!=null){
            ((IntervalTimerSetup)IntervalTimerSetup).setIntervalRounds(routine);
        }
        fm.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out).replace(IntervalTimerMenu.getId(), IntervalTimerSetup).addToBackStack(null).commit();
        toolbar.setVisibility(View.GONE);

    }

    public void restartIntervalSetupFragment(){
        fm.popBackStack();
    }

    public void returnToIntervalMenu(){
        fm.popBackStack();
    }

    public ArrayList<Interval_Item> getTabataRoutine(){
        ArrayList<Interval_Item> tabata = new ArrayList<>();
        for(int i=0; i<15; ++i){
            Interval_Item item;
            if(i%2==0){ //work
                item = new Interval_Item(true, "");
                item.intervalItemLength = 20;
            }else{ //rest
                item = new Interval_Item(false, "");
                item.intervalItemLength = 10;
            }
            tabata.add(item);
        }
        return tabata;
    }


    //Launch confirmation dialog for setup and countdown instances
    @Override
    public void onBackPressed() {
        Dialog_ExitConfirmation dialog = new Dialog_ExitConfirmation();

        Fragment f = fm.findFragmentById(R.id.empty_frame_inner);
        if(f instanceof IntervalTimerSetup){
            dialog.text = "Are you sure you want to exit?\nAny unsaved edits will be lost.";
            dialog.show(fm, "DIALOG_EXIT");
        }else if(f instanceof  IntervalTimerCountdown){
            dialog.text = "Are you sure you want to exit?";
            dialog.show(fm,"DIALOG_EXIT");
        }else{
            super.onBackPressed();
        }

    }
}