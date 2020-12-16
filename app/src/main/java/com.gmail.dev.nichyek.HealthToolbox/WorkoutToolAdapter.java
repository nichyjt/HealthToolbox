package com.gmail.dev.nichyek.HealthToolbox;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WorkoutToolAdapter extends ArrayAdapter<WorkoutTool> {

    public WorkoutToolAdapter(Activity context, ArrayList<WorkoutTool> workoutToolArrayList) {
        super(context, 0, workoutToolArrayList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //the view that this adapter takes comes from Workout_Tool_List_Item
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.workout_tool_list_item, parent, false);

        TextView workoutToolName = convertView.findViewById(R.id.workoutToolName);
        TextView workoutToolDescription = convertView.findViewById(R.id.workoutToolDescription);
        ImageView workoutToolImage = convertView.findViewById(R.id.workoutToolIcon);

        workoutToolName.setText(getItem(position).getMyWorkoutToolName());
        workoutToolDescription.setText(getItem(position).getMyWorkoutToolDescription());
        workoutToolImage.setImageResource(getItem(position).getMyWorkoutToolImageId());

        workoutToolName.setClickable(false);
        workoutToolDescription.setClickable(false);

        return convertView;
    }
}
