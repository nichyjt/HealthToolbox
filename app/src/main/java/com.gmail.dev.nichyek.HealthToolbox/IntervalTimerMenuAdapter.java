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
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class IntervalTimerMenuAdapter extends ArrayAdapter<String> {

    private LayoutInflater inflater;
    ArrayList<String> workoutNames;
    public IntervalTimerMenuAdapter(@NonNull Context context, int resource, ArrayList<String> workoutNames) {
        super(context, resource, workoutNames);
        this.workoutNames = workoutNames;
        inflater = LayoutInflater.from(context);
    }
    private static class ViewHolder{
        TextView intervalName;
        ImageView deleteButton;
        private ViewHolder(View view){
            this.intervalName = view.findViewById(R.id.IntervalTimerMenuItem_ExerciseName);
            this.deleteButton = view.findViewById(R.id.IntervalTimerMenuItem_DeleteButton);
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.interval_timer_menu_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //UI
        holder.intervalName.setText(workoutNames.get(position));
        if(position>=workoutNames.size()-2){
            if(position==workoutNames.size()-1){
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGreen));
            }else{
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightOrange));
            }
            holder.deleteButton.setVisibility(View.GONE);
        }else{
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.almostGray));
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog_DeleteConfirmation dialog = new Dialog_DeleteConfirmation();
                    String workoutName = workoutNames.get(position);
                    dialog.setRequiredParams(Dialog_DeleteConfirmation.DELETE_INTERVAL_WORKOUT,
                            ((IntervalTimerActivity)getContext()).getSupportFragmentManager().findFragmentByTag("INTERVAL_TIMER_MENU_FRAG"),
                            position, "Delete Workout", "Are you sure you want to delete:\n\"" + workoutName+"\"?");
                    dialog.show(((IntervalTimerActivity) getContext()).getSupportFragmentManager(), "DELETE_DIALOG");
                }
            });
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            Fragment menu = ((IntervalTimerActivity) getContext()).getSupportFragmentManager().findFragmentByTag("INTERVAL_TIMER_MENU_FRAG");
            @Override
            public void onClick(View view) {
                //load routine and start timer.
                ((IntervalTimerMenu)menu).startIntervalSetup(position);
            }
        });

        return convertView;
    }


}
