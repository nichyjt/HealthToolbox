package com.gmail.dev.nichyek.HealthToolbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public class Dialog_DeleteConfirmation extends DialogFragment {
    String titleText;
    String descriptionText;
    int dialogContext;
    int position;
    Fragment fragContext;
    Activity activityContext;
    public static final int DELETE_INTERVAL_WORKOUT = 0;
    public static final int DELETE_WORKOUT = 1;
    public static final int DELETE_REPORT_ITEM = 4;
    int record_type = -1;

    public void setRequiredParams(int dialogContext, @Nullable Fragment fragContext, int position, String titleText, String descriptionText){
        this.dialogContext = dialogContext;
        this.fragContext = fragContext;
        this.position = position;
        this.titleText = titleText;
        this.descriptionText = descriptionText;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_simple_dialog, null);
        TextView title = view.findViewById(R.id.dialog_title);
        TextView description = view.findViewById(R.id.dialog_text);

        if(titleText !=null){
            title.setText(titleText);
        }
        if(descriptionText !=null){
            description.setText(descriptionText);
        }

        builder.setView(view);
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(dialogContext){
                    case DELETE_INTERVAL_WORKOUT:
                        ((IntervalTimerMenu)fragContext).updateIntervalWorkoutList(false, null, null, position);
                        Snackbar.make(fragContext.getView(), "Workout deleted.", Snackbar.LENGTH_SHORT).show();
                        break;
                    case DELETE_WORKOUT:
                        ((WorkoutPlanner_MainMenu)fragContext).deleteWorkoutPlannerData(position);
                        Snackbar.make(fragContext.getView(), "Workout deleted.", Snackbar.LENGTH_SHORT).show();
                        break;
                    case DELETE_REPORT_ITEM:
                        if(record_type==-1){
                            return;
                        }
                        FullReportCardFileManager fileManager = new FullReportCardFileManager(activityContext);
                        fileManager.saveHealthToolRecord(record_type, null, position);
                        ((FullReportCardActivity)activityContext).redrawReportCard();
                        break;
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }

}
