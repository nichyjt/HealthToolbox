package com.gmail.dev.nichyek.HealthToolbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class WorkoutPlanner_Active extends Fragment {

    private ArrayList<WorkoutPlanner_Item> routine;
    TextView invisTextView;
    WorkoutPlanner_ActiveAdapter adapter;
    public WorkoutPlanner_Active() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout_planner__active, container, false);
        invisTextView = view.findViewById(R.id.WorkoutPlanner_Active_InvisTimerText);
        ListView listView = view.findViewById(R.id.WorkoutPlanner_Active_ListView);
        adapter = new WorkoutPlanner_ActiveAdapter(getContext(), R.layout.workoutplanner_activeitem, routine, invisTextView);
        listView.setAdapter(adapter);
        return view;
    }

    public void setWorkoutPlannerActiveRoutine(ArrayList<WorkoutPlanner_Item> routine) {
        this.routine = routine;
    }

}