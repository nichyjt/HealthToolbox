package com.gmail.dev.nichyek.HealthToolbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class WorkoutPlanner_MainMenu_Adapter extends ArrayAdapter<String> {

    private LayoutInflater inflater;
    ArrayList<String> myObjects;

    public WorkoutPlanner_MainMenu_Adapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.myObjects = objects;
        inflater = LayoutInflater.from(getContext());
    }
    private static class ViewHolder{ //every list item will have this viewholder object to hold onto the params
        private ImageView editButton;
        private ImageView deleteButton;
        TextView workoutTitle;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.workoutplanner_mainmenu_item, parent, false);
            holder.deleteButton = convertView.findViewById(R.id.WorkoutPlanner_mainmenu_delete);
            holder.editButton = convertView.findViewById(R.id.WorkoutPlanner_mainmenu_edit);
            holder.workoutTitle = convertView.findViewById(R.id.WorkoutPlanner_MainMenu_WorkoutName);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final String workoutName = getItem(position);
        holder.workoutTitle.setText(workoutName);
        if(position==myObjects.size()-1){ //Default NEW WORKOUT option
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGreen));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((WorkoutPlannerActivity)getContext()).startWorkoutPlannerSetup(null, -1, null);
                }
            });
        }else{
            //The code below ensures that when the last 'new workout' view is recycled
            //The new view retains its buttons
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.almostGray));
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
            final WorkoutPlanner_MainMenu menu = (WorkoutPlanner_MainMenu) ((WorkoutPlannerActivity)getContext()).getSupportFragmentManager().findFragmentByTag("FRAGMENT_WORKOUT_PLANNER_MENU");
            assert menu != null;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((WorkoutPlannerActivity)getContext()).startWorkout(menu.getRoutine(position));
                }
            });
            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Get the WP_Item List relevant for setup
                    ((WorkoutPlannerActivity)getContext()).startWorkoutPlannerSetup(menu.getRoutine(position), position, getItem(position));
                }
            });
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog_DeleteConfirmation dialog = new Dialog_DeleteConfirmation();
                    dialog.setRequiredParams(Dialog_DeleteConfirmation.DELETE_WORKOUT,
                            ((WorkoutPlannerActivity)getContext()).getSupportFragmentManager().findFragmentByTag("FRAGMENT_WORKOUT_PLANNER_MENU"),
                            position, "Delete Workout", "Are you sure you want to delete:\n\""+workoutName+"\"?");
                    dialog.show(((WorkoutPlannerActivity) getContext()).getSupportFragmentManager(), "DELETE_DIALOG");
                }
            });
        }

        return convertView;
    }


}
