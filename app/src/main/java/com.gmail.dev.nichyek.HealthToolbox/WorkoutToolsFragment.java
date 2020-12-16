package com.gmail.dev.nichyek.HealthToolbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class WorkoutToolsFragment extends Fragment {


    public WorkoutToolsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_workout_tools, container, false);

        //Create the array of items
        final ArrayList<WorkoutTool> myWorkoutToolList = new ArrayList<>();
        myWorkoutToolList.add(new WorkoutTool(1,"Interval Timer", "Customise and play your HIIT workouts.", R.drawable.ic_interval_timer));
        myWorkoutToolList.add(new WorkoutTool(2,"One Rep Max Helper", "Calculate and get various One Rep Max statistics.", R.drawable.ic_onerepmax_color));
        myWorkoutToolList.add(new WorkoutTool(3,"Calorie Burn Calculator", "Find your calories burnt doing exercise.", R.drawable.ic_calorie_burn_color));
        myWorkoutToolList.add(new WorkoutTool(4,"Workout Planner", "Plan and execute your exercise routines!", R.drawable.ic_workout_planner));
        myWorkoutToolList.add(new WorkoutTool(5,"VO2 Max Tests", "Find your estimated VO2 Max.", R.drawable.ic_vo2_calc_color));
        //Create an adapter to take in the ArrayList<WorkoutTool>
        ArrayAdapter<WorkoutTool> myWorkoutToolArrayAdapter = new WorkoutToolAdapter(getActivity(), myWorkoutToolList);
        AdapterView myWorkoutListView = inflatedView.findViewById(R.id.workoutToolsList);
        myWorkoutListView.setAdapter(myWorkoutToolArrayAdapter);

        //Attach click listeners
        myWorkoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            Class WorkoutToolClass;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            int myWorkoutToolSelected = myWorkoutToolList.get(i).getMyWorkoutToolId();

                switch(myWorkoutToolSelected){
                    case 1:
                        WorkoutToolClass = IntervalTimerActivity.class;
                        break;
                    case 2:
                        WorkoutToolClass = OneRepMaxActivity.class;
                        break;
                    case 3:
                        WorkoutToolClass = CalorieBurnCalculatorActivity.class;
                        break;
                    case 4:
                        WorkoutToolClass = WorkoutPlannerActivity.class;
                        break;
                    case 5:
                        WorkoutToolClass = VO2Calculator.class;
                        break;

                }

                //creates an intent to start an activity/fragment as specified in the array.
                Intent intent = new Intent(WorkoutToolsFragment.this.getContext(), WorkoutToolClass);
                startActivity(intent);


            }
        });



        return inflatedView;
    }






}



