package com.gmail.nichyekdev.healthtoolbox;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class WorkoutPlannerActivity extends FragmentActivity {

    FragmentManager fm = getSupportFragmentManager();
    Fragment setup;
    Fragment menu = new WorkoutPlanner_MainMenu();
    Fragment activeWorkout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_frame);
        fm.beginTransaction().replace(R.id.empty_frame_inner, menu, "FRAGMENT_WORKOUT_PLANNER_MENU").commit();
        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Choose a Workout");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(fm.findFragmentById(R.id.empty_frame_inner) instanceof WorkoutPlanner_MainMenu && activeWorkout!=null){
                    toolbar.setVisibility(View.VISIBLE);
                    if(((WorkoutPlanner_Active)activeWorkout).adapter.timerActive){
                        ((WorkoutPlanner_Active)activeWorkout).adapter.timer.cancel();
                    }
                }
            }
        });
    }

    public void startWorkoutPlannerSetup(@Nullable ArrayList<WorkoutPlanner_Item> routineList, int routinePosition, String existingName){
        toolbar.setVisibility(View.GONE);
        setup = new WorkoutPlanner_Setup();
        ((WorkoutPlanner_Setup)setup).setSetupParams(routineList, routinePosition, existingName);
        fm.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out).replace(menu.getId(), setup).addToBackStack(null).commit();
    }

    public void exitWorkoutSetupFragment(){
        fm.popBackStack();
    }

    public void startWorkout(ArrayList<WorkoutPlanner_Item> routine){
        toolbar.setVisibility(View.GONE);
        activeWorkout = new WorkoutPlanner_Active();
        ((WorkoutPlanner_Active)activeWorkout).setWorkoutPlannerActiveRoutine(routine);
        fm.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out).replace(menu.getId(), activeWorkout).addToBackStack(null).commit();
    }

    //Exit confirmation dialog
    @Override
    public void onBackPressed() {
        Fragment frag = fm.findFragmentById(R.id.empty_frame_inner);
        Dialog_ExitConfirmation dialog = new Dialog_ExitConfirmation();
        if(frag instanceof WorkoutPlanner_Setup){
            dialog.text = "Are you sure you want to exit? \nAny unsaved changes will be lost.";
        }else if(frag instanceof  WorkoutPlanner_Active){
            dialog.text = "Are you sure you want to exit?";
        }else{
            super.onBackPressed();
            return;
        }
        dialog.show(fm, "EXIT_DIALOG");

    }
}