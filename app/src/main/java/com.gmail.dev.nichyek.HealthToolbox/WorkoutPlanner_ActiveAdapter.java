package com.gmail.dev.nichyek.HealthToolbox;

import android.content.Context;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;

public class WorkoutPlanner_ActiveAdapter extends ArrayAdapter<WorkoutPlanner_Item> {

    private LayoutInflater inflater;
    boolean[] exerciseDone;
    private ArrayList<WorkoutPlanner_Item> routine;
    boolean timerActive = false;
    int activeTimerIndex = -1;
    TextView activeTimerTextView;
    TextView invisibleTimerText;
    CountDownTimer timer;
    boolean soundPlaying = false;

    public WorkoutPlanner_ActiveAdapter(@NonNull Context context, int resource, ArrayList<WorkoutPlanner_Item> objects, TextView invisibleTimerText) {
        super(context, 0, objects);
        inflater = LayoutInflater.from(getContext());
        this.invisibleTimerText = invisibleTimerText;
        exerciseDone = new boolean[objects.size()];
        Arrays.fill(exerciseDone, false);
        routine = objects;
    }

    private static class ViewHolder{
        TextView exerciseNumber;
        TextView exerciseName;
        TextView exerciseWeightReps;
        CheckBox checkBox;
        ConstraintLayout timerContainer;
        TextView timerTextView;
        ImageButton timerButton;
        ConstraintLayout container;
        CardView timerCard;
        public ViewHolder(View itemView){
            this.exerciseNumber = itemView.findViewById(R.id.WorkoutPlanner_ActiveItem_ExNumberText);
            this.exerciseName = itemView.findViewById(R.id.WorkoutPlanner_ActiveItem_Name);
            this.exerciseWeightReps = itemView.findViewById(R.id.WorkoutPlanner_ActiveItem_RepWeightText);
            this.checkBox = itemView.findViewById(R.id.checkBox);
            this.timerContainer = itemView.findViewById(R.id.WorkoutPlanner_ActiveItem_TimerContainer);
            this.timerTextView = itemView.findViewById(R.id.WorkoutPlanner_ActiveItem_TimerText);
            this.timerButton = itemView.findViewById(R.id.WorkoutPlanner_ActiveItem_TimerBtn);
            this.container = itemView.findViewById(R.id.WorkoutPlanner_ActiveItem_Container);
            this.timerCard = itemView.findViewById(R.id.WorkoutPlanner_ActiveItem_TimerContainerCard);
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.workoutplanner_activeitem, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final WorkoutPlanner_Item workout = routine.get(position);
        //Name, Weight and Rep
        holder.exerciseName.setText(workout.name);
        Paint fakeBold = new Paint();
        fakeBold.setFakeBoldText(true);
        holder.exerciseName.setPaintFlags(fakeBold.getFlags());
        holder.exerciseNumber.setText("Exercise "+(position+1));
        holder.exerciseWeightReps.setText(setExerciseDetails(workout));

        //Timer Display Logic
        if(workout.time!=-1){
            holder.timerContainer.setVisibility(View.VISIBLE);
            //Set Timer Text.
            //Only the position text should be visible.
            if(timerActive){
                if(position==activeTimerIndex){
                    holder.timerButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_stop_button_black_rounded_square));
                    activeTimerTextView = holder.timerTextView;
                }else if(holder.timerTextView == activeTimerTextView){
                    //Current view is not the active timer and contains the recycled textView element
                    holder.timerButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_play_button_arrowhead));
                    activeTimerTextView = invisibleTimerText;
                    holder.timerTextView.setText(String.valueOf(workout.time));
                }else{
                    holder.timerButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_play_button_arrowhead));
                    holder.timerTextView.setText(String.valueOf(workout.time));
                }
            }else{
                holder.timerButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_play_button_arrowhead));
                holder.timerTextView.setText(String.valueOf(workout.time));
            }


            //Timer button logic
            holder.timerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!timerActive){
                        timer = setTimer(workout.time);
                        timer.start();
                        timerActive = true;
                        activeTimerIndex = position;
                        activeTimerTextView = holder.timerTextView;
                        holder.timerButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_stop_button_black_rounded_square));
                        Snackbar.make(invisibleTimerText, "Timer started.", Snackbar.LENGTH_SHORT).show();
                    }else if(position == activeTimerIndex){
                        timer.cancel();
                        if(soundPlaying){
                            soundPlaying = false;
                            beep.pause();
                            beep.seekTo(0);
                        }
                        timerActive = false;
                        holder.timerTextView.setText(String.valueOf(workout.time));
                        holder.timerButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_play_button_arrowhead));
                        Snackbar.make(invisibleTimerText, "Timer stopped.", Snackbar.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(invisibleTimerText, "Another timer is currently active!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            holder.timerContainer.setVisibility(View.GONE);
        }

        //Checkbox and related UI Logic
        holder.checkBox.setChecked(exerciseDone[position]);
        holder.container.setBackgroundColor(getColor(exerciseDone[position]));
        holder.timerCard.setCardBackgroundColor(getColor(exerciseDone[position]));
        setStrikethrough(holder.exerciseName, position);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exerciseDone[position] = !exerciseDone[position];
                setStrikethrough(holder.exerciseName, position);
                holder.timerCard.setCardBackgroundColor(getColor(exerciseDone[position]));
                holder.container.setBackgroundColor(getColor(exerciseDone[position]));
            }
        });

        return convertView;
    }//end of main getView

    private String setExerciseDetails(WorkoutPlanner_Item item){
        String string = "";
        int weight = item.weight; //Bodyweight
        int reps = item.numReps; //To failure
        string += (weight==-1)? "Bodyweight: ":weight+" kg/lbs: ";
        switch (reps){
            case -1:
                string+= "To Failure";
                break;
            case 1:
                string+= reps+" Rep";
                break;
            default:
                string+= reps+" Reps";
        }
        return string;
    }

    MediaPlayer beep  = MediaPlayer.create(getContext(), R.raw.countdown_beep);
    MediaPlayer bell = MediaPlayer.create(getContext(), R.raw.boxingbellsingle);

    private CountDownTimer setTimer(int time){
        long t = (long) time*1000;

        return new CountDownTimer(t, 100) {
            @Override
            public void onTick(long l) {
                int activeTime = 1+ Integer.parseInt(String.valueOf(l))/1000;
                activeTimerTextView.setText(String.valueOf(activeTime));
                if(activeTime<=3 && !soundPlaying){
                    //soundplaying boolean to only call the media once
                    beep.start();
                    soundPlaying = true;
                }
            }
            @Override
            public void onFinish() {
                bell.start();
                activeTimerTextView.setText("Done!");
                timerActive = false;
                soundPlaying = false;
            }
        };
    }

    private int getColor(boolean isChecked){
        return (isChecked)? ContextCompat.getColor(getContext(), R.color.superLightGreen):ContextCompat.getColor(getContext(), R.color.almostGray);
    }
    private void setStrikethrough(TextView exerciseName, int position){
        if(exerciseDone[position]){
            exerciseName.setPaintFlags(exerciseName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            exerciseName.setPaintFlags(exerciseName.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

}
