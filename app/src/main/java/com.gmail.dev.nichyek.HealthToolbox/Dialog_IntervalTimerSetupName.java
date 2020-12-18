package com.gmail.nichyekdev.healthtoolbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Dialog_IntervalTimerSetupName extends DialogFragment {

    String name = null;
    int position;
    Interval_Item item;
    IntervalTimerSetupAdapter adapterContext;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_intervaltimersetupname, null);
        EditText nameInput = view.findViewById(R.id.dialog_input);
        if(name == null){
            nameInput.setText("Your Exercise Name");
        }else{
            nameInput.setText(name);
        }
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                name = editable.toString();
            }
        }; nameInput.addTextChangedListener(tw);

        builder.setView(view);
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                item.setIntervalExerciseName(name.trim());
                adapterContext.notifyDataSetChanged();
            }
        });


        return builder.create();
    }


    public void setDialogParams(IntervalTimerSetupAdapter adapter, int position, Interval_Item item, String existingName){
        this.item = item;
        this.adapterContext = adapter;
        this.position = position;
        this.name = existingName;
    }

}
