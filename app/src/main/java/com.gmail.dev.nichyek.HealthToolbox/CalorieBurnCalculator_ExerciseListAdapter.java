package com.gmail.nichyekdev.healthtoolbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CalorieBurnCalculator_ExerciseListAdapter extends ArrayAdapter<CalorieBurnCalculator_ExerciseItem> {

    Context mainCalcContext;
    int itemMETCODE = 0;

    public CalorieBurnCalculator_ExerciseListAdapter(@NonNull Context context, ArrayList<CalorieBurnCalculator_ExerciseItem> exerciseItemParams) {
        super(context, 0, exerciseItemParams);
        mainCalcContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent){
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.calorie_burn_exercise_option_layout, parent, false);
        //Return the METCODE to the caller for manipulation
        //'FINAL' to ensure the metcode does not change in the click listener
        convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.almostGray));
        final int constItemMetCode = getItem(position).metCode;
        final String constExerciseName = getItem(position).vExerciseName;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalorieBurnCalculatorActivity activeClass = (CalorieBurnCalculatorActivity) mainCalcContext;
                activeClass.setMETCODE(constItemMetCode);
                TextView output = activeClass.findViewById(R.id.calorieCalc_ExerciseConfirmationText);
                output.setText(constExerciseName);
                activeClass.findViewById(R.id.calorieCalc_LayoutContainer).performClick();
                Snackbar.make(view, constExerciseName+ " chosen.", Snackbar.LENGTH_SHORT).show();
            }
        });
        itemMETCODE = getItem(position).metCode;
        TextView codeForNerds = convertView.findViewById(R.id.calorieCalc_metCodeForNerds);
        TextView exerciseNameView = convertView.findViewById(R.id.calorieCalc_Option_ExerciseName);
        codeForNerds.setText("MET CODE: "+itemMETCODE);
        exerciseNameView.setText(getItem(position).vExerciseName);

        return convertView;
    }

}

