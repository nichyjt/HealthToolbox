package com.gmail.dev.nichyek.HealthToolbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Dialog_IntervalTimerSetupSave extends DialogFragment {
    String existingName = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_workoutplanner_setupsave, null);
        final EditText nameInput = view.findViewById(R.id.workoutNameInput);
        TextView saveTitle = view.findViewById(R.id.saveTitle);
        if(!existingName.equals("")){
            nameInput.setText(existingName);
            saveTitle.setText("Update Name?");
        }else{
            saveTitle.setText("Save Workout");
            nameInput.setText("New Interval");
            existingName = "New Interval";
        }
        TextWatcher tw =  new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                AlertDialog dialog = (AlertDialog) getDialog();
                if(editable.length()==0){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }else{
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    existingName = editable.toString();
                }
            }
        }; nameInput.addTextChangedListener(tw);

        //Build the dialog
        builder.setView(view);
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((IntervalTimerSetup)getParentFragment()).saveIntervalWorkout(existingName);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Dialog_IntervalTimerSetupSave.this.getDialog().cancel();
            }
        });

        return builder.create();
    }

    public void setExistingName(String existingName) {
        this.existingName = existingName;
    }
}
