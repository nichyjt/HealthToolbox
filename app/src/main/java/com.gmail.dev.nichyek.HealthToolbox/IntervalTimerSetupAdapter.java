package com.gmail.dev.nichyek.HealthToolbox;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class IntervalTimerSetupAdapter extends ArrayAdapter<Interval_Item> {

    //This Adapter's function is purely for UI manipulation
    IntervalTimerSetup setupFragment;
    LayoutInflater inflater;
    ArrayList<Interval_Item> routine;

    //Constructor
    public IntervalTimerSetupAdapter(Activity context, ArrayList<Interval_Item> IntervalItemArrayList, IntervalTimerSetup setupFragment) {
        super(context, 0, IntervalItemArrayList);
        this.setupFragment = setupFragment;
        this.routine = IntervalItemArrayList;
        inflater = LayoutInflater.from(context);
    }

    public class ViewHolder{
        TextView roundDetailText;
        EditText lengthOfRoundText;
        ImageView addNameButton;
        TextView workoutName;
        lengthTextWatcher textWatcher;
        public ViewHolder(View view){
            //Manipulate UI
            this.roundDetailText = view.findViewById(R.id.roundDetailText);
            this.lengthOfRoundText = view.findViewById(R.id.lengthOfRoundText);
            this.addNameButton = view.findViewById(R.id.intervalRoundListItem_addName);
            this.workoutName = view.findViewById(R.id.intervalRoundListItem_Name);
            textWatcher = new lengthTextWatcher(lengthOfRoundText);
            lengthOfRoundText.addTextChangedListener(textWatcher);
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.interval_round_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.lengthOfRoundText.setSelectAllOnFocus(true);
        final Interval_Item myItem = routine.get(position);
        holder.textWatcher.position = position;


        //Sets the Round details, Icon details, Timing Details and Name Details
        if(myItem.isWork){
            //Get the various params from Interval_Item and update the UI accordingly
            holder.workoutName.setVisibility(View.VISIBLE);
            holder.addNameButton.setVisibility(View.VISIBLE);
            String exerciseName = myItem.getIntervalItemName();
            holder.workoutName.setText(exerciseName);
            int numRound = (position/2)+1;
            holder.roundDetailText.setText("ROUND "+ numRound);
        }else {
            holder.workoutName.setVisibility(View.INVISIBLE);
            holder.addNameButton.setVisibility(View.INVISIBLE);
            holder.roundDetailText.setText("REST");
        }
        holder.lengthOfRoundText.setText(Integer.toString(myItem.intervalItemLength));

        holder.addNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_IntervalTimerSetupName dialog = new Dialog_IntervalTimerSetupName();
                dialog.setDialogParams(IntervalTimerSetupAdapter.this, position, myItem, myItem.intervalExerciseName);
                assert setupFragment.getFragmentManager() != null;
                dialog.show(setupFragment.getFragmentManager(), "SAVE NAME TAG");
            }
        });
        return convertView;
    }

    private class lengthTextWatcher implements TextWatcher{
        EditText editText;
        int position;
        public lengthTextWatcher(EditText editText){
            this.editText = editText;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            int newTime;
            if(editable.length() == 0 || Integer.parseInt(editable.toString())==0){
                newTime = 5;
                editText.setText("5");
                editText.selectAll();
            }else{
                newTime = Integer.parseInt(editable.toString());
            }
            routine.get(position).intervalItemLength = newTime;
        }
    }


}