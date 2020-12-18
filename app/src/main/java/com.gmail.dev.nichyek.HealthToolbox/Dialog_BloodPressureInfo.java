package com.gmail.nichyekdev.healthtoolbox;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class Dialog_BloodPressureInfo extends DialogFragment {

    LayoutInflater inflater;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        inflater = LayoutInflater.from(getContext());
        View dView = inflater.inflate(R.layout.dialog_information_bp, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dView);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nth
            }
        });

        return builder.create();
    }
}
