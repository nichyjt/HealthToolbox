package com.gmail.dev.nichyek.HealthToolbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Dialog_ExitConfirmation extends DialogFragment {
    String text;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_simple_dialog, null);

        TextView textView = view.findViewById(R.id.dialog_text);
        if(text!=null){
            textView.setText(text);
        }

        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nth
            }
        });

        return builder.create();
    }
}