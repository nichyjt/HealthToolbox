package com.gmail.dev.nichyek.HealthToolbox;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class WorkoutPlanner_SetupAdapter extends RecyclerView.Adapter<WorkoutPlanner_SetupAdapter.WP_SetupViewHolder> {

    ArrayList<WorkoutPlanner_Item> workoutPlannerItems;
    WorkoutPlanner_Setup setup;
    public WorkoutPlanner_SetupAdapter(ArrayList<WorkoutPlanner_Item> workoutPlanner_items, WorkoutPlanner_Setup setupContext){
        workoutPlannerItems = workoutPlanner_items;
        setup = setupContext;
    }

    //ViewHolders are persistent
    public class WP_SetupViewHolder extends RecyclerView.ViewHolder{
        EditText nameInput;
        TextInputLayout nameInputLayout;
        TextInputLayout weightInputLayout;
        TextInputLayout numRepInputLayout;
        EditText weightInput;
        EditText numRepInput;
        EditText timerInput;
        TextView exerciseNumber;
        ImageView deleteButton;
        ImageView optionButton;
        ConstraintLayout timerContainer;

        final NameTextWatcher nameTw;
        WeightTextWatcher weightTw;
        NumRepTextWatcher numRepTw;
        TimerTextWatcher timerTw;
        OptionClickListener optionClickListener = new OptionClickListener(0);

        public WP_SetupViewHolder(@NonNull View itemView) {
            super(itemView);
            nameInputLayout = itemView.findViewById(R.id.WorkoutPlanner_Setup_Item_NameLayout);
            weightInputLayout = itemView.findViewById(R.id.workoutPlanner_Setup_Item_WeightInputCon);
            numRepInputLayout = itemView.findViewById(R.id.WorkoutPlanner_Setup_Item_NumRepInputCon);

            nameInput = itemView.findViewById(R.id.WorkoutPlanner_Setup_ExerciseName);
            nameInput.setSelectAllOnFocus(true);
            weightInput = itemView.findViewById(R.id.WorkoutPlanner_Setup_Item_WeightInput);
            weightInput.setSelectAllOnFocus(true);
            numRepInput = itemView.findViewById(R.id.WorkoutPlanner_Setup_Item_NumRepInput);
            numRepInput.setSelectAllOnFocus(true);
            timerInput = itemView.findViewById(R.id.WorkoutPlanner_Setup_Item_TimerInput);
            timerInput.setSelectAllOnFocus(true);
            exerciseNumber = itemView.findViewById(R.id.WorkoutPlanner_Setup_Item_ExerciseNumber);
            deleteButton = itemView.findViewById(R.id.WorkoutPlanner_Setup_Item_DeleteButton);
            optionButton = itemView.findViewById(R.id.WorkoutPlanner_Setup_Item_OptionBtn);
            timerContainer = itemView.findViewById(R.id.WorkoutPlanner_Setup_Item_TimerCon);

            nameTw = new NameTextWatcher(nameInputLayout);
            nameInput.addTextChangedListener(nameTw);
            weightTw = new WeightTextWatcher(weightInput, weightInputLayout);
            weightInput.addTextChangedListener(weightTw);
            numRepTw = new NumRepTextWatcher(numRepInput, numRepInputLayout);
            numRepInput.addTextChangedListener(numRepTw);
            timerTw = new TimerTextWatcher(timerInput, timerContainer);
            timerInput.addTextChangedListener(timerTw);
            optionButton.setOnClickListener(optionClickListener);

        }

    }

    @NonNull
    @Override
    public WP_SetupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.workoutplanner_setupitem, viewGroup, false);
        WP_SetupViewHolder viewHolder = new WP_SetupViewHolder(view);

        return viewHolder;
    }

    //Notifydata... calls onBindVH
    @Override
    public void onBindViewHolder(@NonNull final WP_SetupViewHolder holder, int position) {
        //Exercise Number data binding
        final int pos = position;
        //IMPORTANT: Declare textWatcher positions before setText.
        //This prevents recyclerview from re-binding/setting the 0th item.
        //This is because 'position' is by default 0 (lang trait) in the class field below.
        holder.nameTw.position = position;
        holder.weightTw.position = position;
        holder.numRepTw.position = position;
        holder.timerTw.position = position;
        holder.optionClickListener.position = position;

        holder.exerciseNumber.setText(String.valueOf(position+1));
        holder.nameInput.setText(workoutPlannerItems.get(position).name);

        holder.weightInput.setText(String.valueOf(workoutPlannerItems.get(position).weight));
        holder.numRepInput.setText(String.valueOf(workoutPlannerItems.get(position).numReps));
        holder.timerInput.setText(String.valueOf(workoutPlannerItems.get(position).time));
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Spam 'delete' crash handler
                if(workoutPlannerItems.size()==1){
                    Snackbar.make(holder.exerciseNumber, "You need at least one workout item!", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                workoutPlannerItems.remove(pos);
                notifyItemRemoved(pos);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return workoutPlannerItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //Classes to assist with textwatching
    private class NameTextWatcher implements TextWatcher {
        TextInputLayout layout;
        //position helps tw know where to assign data changes to the list.
        int position;
        public NameTextWatcher(TextInputLayout textInputLayout){
            layout = textInputLayout;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length()==0){
                //Warning + 'invalidate' item
                layout.setErrorEnabled(true);
                layout.setError("You need to enter a name.");
                workoutPlannerItems.get(position).name = "";
            }else{
                //Disable Warning + update item param
                layout.setErrorEnabled(false);
                workoutPlannerItems.get(position).name = editable.toString();
            }
        }
    }
    private class WeightTextWatcher implements TextWatcher{
        EditText mEditText;
        TextInputLayout mLayout;
        int position;
        public WeightTextWatcher(EditText editText, TextInputLayout layout){
            mEditText = editText;
            mLayout = layout;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length()==0||Integer.parseInt(editable.toString())==0){
                if(workoutPlannerItems.get(position).weight==-1) return;
                workoutPlannerItems.get(position).weight = 1;
                mEditText.setText("1");
                mEditText.selectAll();
            }else{
                int val = Integer.parseInt(editable.toString());
                //perform a check if value is -1
                if(val==-1){
                    mLayout.setHint("N.A.");
                    mEditText.setText("");
                    mEditText.setFocusable(false);
                    return;
                }
                mLayout.setHint("Weight");
                mEditText.setFocusable(true);
                mEditText.setFocusableInTouchMode(true);
                workoutPlannerItems.get(position).weight = val;
            }
        }
    }
    private class NumRepTextWatcher implements TextWatcher {
        EditText mEditText;
        TextInputLayout layout;
        int position;
        public NumRepTextWatcher(EditText editText, TextInputLayout layout){
            mEditText = editText;
            this.layout = layout;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length()==0||Integer.parseInt(editable.toString())==0){
                if(workoutPlannerItems.get(position).numReps == -1) return;
                workoutPlannerItems.get(position).numReps = 1;
                mEditText.setText("1");
                mEditText.selectAll();
            }else{
                int val = Integer.parseInt(editable.toString());
                if(val==-1){
                    layout.setHint("N.A.");
                    mEditText.setText("");
                    mEditText.setFocusable(false);
                    return;
                }
                layout.setHint("Reps");
                mEditText.setFocusable(true);
                mEditText.setFocusableInTouchMode(true);
                workoutPlannerItems.get(position).numReps = val;
            }
        }
    }
    private class TimerTextWatcher implements TextWatcher{
        ConstraintLayout container;
        EditText mEditText;
        int position;
        public TimerTextWatcher(EditText editText, ConstraintLayout container){
            mEditText = editText;
            this.container = container;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length()==0){
                if(workoutPlannerItems.get(position).time == -1) return;
                mEditText.setText("5");
                mEditText.selectAll();
                workoutPlannerItems.get(position).time = 5;
            }else{
                int val = Integer.parseInt(editable.toString());
                if(val==-1){
                    container.setVisibility(View.GONE);
                    return;
                }else if(val<1){
                    val = 1;
                    mEditText.setText("1");
                    mEditText.selectAll();
                }
                container.setVisibility(View.VISIBLE);
                workoutPlannerItems.get(position).time = val;
            }
        }
    }

    private class OptionClickListener implements View.OnClickListener {
        int position;
        public OptionClickListener(int position){
            this.position = position;
        }
        @Override
        public void onClick(View view) {
            Dialog_WorkoutPlannerSetupItemOptions dialogOptions = new Dialog_WorkoutPlannerSetupItemOptions();
            dialogOptions.setPosition(position);
            dialogOptions.setItem(workoutPlannerItems.get(position));
            dialogOptions.setAdapterContext(WorkoutPlanner_SetupAdapter.this);
            dialogOptions.show(setup.getFragmentManager(), "dialogOption");
        }
    }
}
