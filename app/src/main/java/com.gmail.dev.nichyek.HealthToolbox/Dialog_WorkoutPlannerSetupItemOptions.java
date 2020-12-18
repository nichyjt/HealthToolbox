package com.gmail.nichyekdev.healthtoolbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Dialog_WorkoutPlannerSetupItemOptions extends DialogFragment {

    int position;
    WorkoutPlanner_SetupAdapter adapterContext;
    WorkoutPlanner_Item item;
    boolean noWeight;
    boolean numRepToFailure;
    boolean timerOn;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_workoutplanner_setupitem_options, null);
        Switch noWeightSwitch = view.findViewById(R.id.weightSwitch);
        Switch numRepToFailureSwitch = view.findViewById(R.id.repsSwitch);
        Switch timerOnSwitch = view.findViewById(R.id.timerSwitch);

        noWeight = (item.weight == -1);
        numRepToFailure = (item.numReps == -1);
        timerOn = !(item.time == -1);

        noWeightSwitch.setChecked(noWeight);
        numRepToFailureSwitch.setChecked(numRepToFailure);
        timerOnSwitch.setChecked(timerOn);

        noWeightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                noWeight = b;
            }
        });
        numRepToFailureSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                numRepToFailure = b;
            }
        });
        timerOnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                timerOn = b;
            }
        });

        builder.setView(view);
        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //4 cases. (noWeight, !-1) (noWeight, -1) (!noWeight, !-1) (!noWeight, -1)
                if(noWeight){
                    item.weight = -1;
                }else{
                    item.weight = (item.weight==-1)? 1:item.weight;
                }
                if(numRepToFailure){
                    item.numReps = -1;
                }else{
                    item.numReps = (item.numReps==-1)? 1:item.numReps;
                }
                if(timerOn){
                    item.time = (item.time==-1)? 5:item.time;
                }else{
                    item.time = -1;
                }
                adapterContext.notifyItemChanged(position);
            }
        });

        return  builder.create();
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setAdapterContext(WorkoutPlanner_SetupAdapter adapterContext) {
        this.adapterContext = adapterContext;
    }

    public void setItem(WorkoutPlanner_Item item) {
        this.item = item;
    }
}
