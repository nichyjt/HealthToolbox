package com.gmail.nichyekdev.healthtoolbox;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class IntervalTimerCountdown extends Fragment{

    int currentIndex = 0;
    int activeTime = 0;
    boolean isPaused = false;
    boolean pausedTimerActive = false;
    boolean userExitFrag = false;
    boolean isTabata = false;
    ArrayList<Interval_Item> intervalRoutine;


    public IntervalTimerCountdown(ArrayList<Interval_Item> intervalRoutine) {
        this.intervalRoutine = intervalRoutine;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View intervalTimerCountdownView = inflater.inflate(R.layout.fragment_interval_timer_countdown, container, false);
        //Media Setup
        final MediaPlayer singleBellPlayer = MediaPlayer.create(getContext(), R.raw.boxingbellsingle);
        final MediaPlayer tripleBellPlayer = MediaPlayer.create(getContext(), R.raw.boxingbelltriple);
        final MediaPlayer flatBeepPlayer = MediaPlayer.create(getContext(), R.raw.countdown_beep);

        //Instantiate UI
        final TextView roundNumber = intervalTimerCountdownView.findViewById(R.id.roundNumber);
        roundNumber.setText("GET READY");
        final TextView countdownTime = intervalTimerCountdownView.findViewById(R.id.countdownTime);
        final TextView activeExerciseName = intervalTimerCountdownView.findViewById(R.id.IntervalExerciseName);

        final ImageSwitcher pause = intervalTimerCountdownView.findViewById(R.id.pauseButtonSwitcher);
        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_fast);
        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_fast);

        pause.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(getContext());
            }
        });
        pause.setInAnimation(in);
        pause.setOutAnimation(out);
        pause.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pause_button));

        final ImageView skip = intervalTimerCountdownView.findViewById(R.id.skipButton);
        final ImageView back = intervalTimerCountdownView.findViewById(R.id.previousRound);
        final ImageView restartSet = intervalTimerCountdownView.findViewById(R.id.restartSet);
        final ImageView newInterval = intervalTimerCountdownView.findViewById(R.id.newInterval);

        //Import arrays from main activity for this tool. myTime defines every work and rest round
        final int numWork = (intervalRoutine.size()+1)/2;
        final int numRest =  (intervalRoutine.size()-1)/2;

        //Create an array of countdown timers
        final CountDownTimer[] myCollatedTimings = new CountDownTimer[intervalRoutine.size()];
        final CountDownTimer[] pausedTimer =  new CountDownTimer[1];

        //Initial 5s preparation timer
        final CountDownTimer fiveSecondTimer = new CountDownTimer(5000,100) {
            @Override
            public void onTick(long l) {
                if(userExitFrag) {
                    cancel();
                    flatBeepPlayer.pause();
                }

                //Offset timing by 1 as displayed text tends to round down. e.g. 15s shows 14s
                //long tmp = 1+ l/1000;
                int tmp = 1+ Integer.parseInt(String.valueOf(l))/1000;
                countdownTime.setText(""+ tmp);
                //Play media
                if(tmp==3){
                    flatBeepPlayer.start();
                }
            }
            @Override
            public void onFinish() {
                if(userExitFrag) cancel();
                flatBeepPlayer.pause();
                flatBeepPlayer.seekTo(0);

                myCollatedTimings[0].start();
                roundNumber.setText("ROUND " + 1 + "/" + numWork);
                activeExerciseName.setText(intervalRoutine.get(currentIndex).intervalExerciseName);
                pause.setVisibility(View.VISIBLE);
                skip.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                //Play Media
                singleBellPlayer.start();
            }
        }; fiveSecondTimer.start();

        //Button logic
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPaused = !isPaused;
                //User has paused the timing
                if(isPaused){
                    pause.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_play_button_arrowhead));
                    flatBeepPlayer.pause();
                    int seek = (3-activeTime)*1000;
                    flatBeepPlayer.seekTo(seek-300);

                    if(!pausedTimerActive){ //this is the first pause. Cancel main timer and instantiate paused timer
                        pausedTimerActive = true;
                        myCollatedTimings[currentIndex].cancel();
                        pausedTimer[0] = new CountDownTimer(activeTime *1000, 100) {
                            @Override
                            public void onTick(long l) {
                                if(userExitFrag){
                                    cancel();
                                    flatBeepPlayer.pause();
                                    return;
                                }
                                activeTime = 1+ Integer.parseInt(String.valueOf(l))/1000;
                                countdownTime.setText(""+ activeTime);
                                if(activeTime<=3){
                                    countdownTime.setTextColor(ContextCompat.getColor(getContext(), R.color.crimsonError));
                                    if(!flatBeepPlayer.isPlaying()) flatBeepPlayer.start();
                                }
                            }
                            @Override
                            public void onFinish() {
                                myCollatedTimings[currentIndex].onFinish();
                            }
                        };
                    }else{
                        //Re-calibrate paused timer.
                        pausedTimer[0].cancel();
                        pausedTimer[0] = new CountDownTimer(activeTime *1000, 100) {
                            @Override
                            public void onTick(long l) {
                                if(userExitFrag) {
                                    cancel();
                                    flatBeepPlayer.pause();
                                    return;
                                }

                                activeTime = 1+ Integer.parseInt(String.valueOf(l))/1000;
                                countdownTime.setText(""+ activeTime);
                                if(activeTime<=3){
                                    countdownTime.setTextColor(ContextCompat.getColor(getContext(), R.color.crimsonError));
                                    if(!flatBeepPlayer.isPlaying()) flatBeepPlayer.start();
                                }
                            }

                            @Override
                            public void onFinish() {
                                myCollatedTimings[currentIndex].onFinish();
                            }
                        };
                    }
                }else{ //implies that the user wants to unpause the timer and start tmpTimer
                    pause.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pause_button));
                    pausedTimer[0].start();
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPaused = false;
                flatBeepPlayer.pause();
                flatBeepPlayer.seekTo(-1);
                if(pausedTimerActive){
                    pausedTimer[0].cancel();
                    pausedTimerActive = false;
                    pause.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pause_button));
                }
                myCollatedTimings[currentIndex].cancel();
                myCollatedTimings[currentIndex].onFinish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPaused = false;
                flatBeepPlayer.pause();
                flatBeepPlayer.seekTo(-1);
                countdownTime.setTextColor(Color.parseColor("#000000"));
                if(pausedTimerActive){
                    pausedTimer[0].cancel();
                    pausedTimerActive = false;
                    pause.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pause_button));
                }
                if(currentIndex>1){
                    int tmpIndex = currentIndex;
                    myCollatedTimings[currentIndex].cancel();
                    currentIndex -= 2;
                    myCollatedTimings[tmpIndex-2].onFinish();
                } else {
                    myCollatedTimings[currentIndex].cancel();
                    currentIndex = 0;
                    fiveSecondTimer.onFinish();
                }
            }
        });


        //Looping Logic to setup the main timers
        for(int i = 0; i<myCollatedTimings.length; i++){
            final Interval_Item curr = intervalRoutine.get(i);
            final int time = curr.intervalItemLength*1000;
            final int j = i;

            //MAIN TIMER LOGIC
            myCollatedTimings[i] = new CountDownTimer(time, 100) {
                @Override
                public void onTick(long l) {
                    if(userExitFrag) {
                        cancel();
                        flatBeepPlayer.pause();
                        return;
                    }
                    activeTime = 1+ Integer.parseInt(String.valueOf(l))/1000;
                    countdownTime.setText(""+ activeTime);

                    if(activeTime<=3){
                        countdownTime.setTextColor(ContextCompat.getColor(getContext(), R.color.crimsonError));
                        if(!flatBeepPlayer.isPlaying()) flatBeepPlayer.start();
                    }
                }

                @Override
                public void onFinish() { //Logic dictating the transition between rounds
                    if(userExitFrag) cancel();
                    flatBeepPlayer.pause();
                    flatBeepPlayer.seekTo(0);
                    countdownTime.setTextColor(Color.parseColor("#000000"));

                    int nextIndex = j + 1;

                    if(pausedTimerActive){
                        pausedTimer[0].cancel();
                        pausedTimerActive = false;
                    }

                    //UI setup for next coutndown
                    if(j!=myCollatedTimings.length-1){
                        myCollatedTimings[j+1].start();
                        //Update current index for pause/skip/back functions to work.
                        currentIndex = nextIndex;
                        //Logic to handle UI and media for Rest & Work
                        switch(nextIndex%2){
                            case 0: //implies that next round is a work round
                                //Media
                                singleBellPlayer.start();
                                //Work round 'n' follows the pattern: i = 2(n-1)
                                int tmp = 1 + nextIndex/2;
                                roundNumber.setText("ROUND " + tmp + "/" + numWork);
                                if(!intervalRoutine.get(nextIndex).intervalExerciseName.equals("")){
                                    activeExerciseName.setText(intervalRoutine.get(nextIndex).intervalExerciseName);
                                }else{
                                    activeExerciseName.setText("");
                                }
                                break;
                            case 1: //implies that next round is a rest round
                                //Media
                                tripleBellPlayer.start();
                                //Rest round 'n' takes the pattern:  i = 2n-1
                                int tmp2 = (nextIndex+1)/2;
                                roundNumber.setText("REST " + tmp2 + "/" + numRest);

                                if(!intervalRoutine.get(nextIndex+1).intervalExerciseName.equals("")){
                                    activeExerciseName.setText("Next up: " + intervalRoutine.get(nextIndex+1).intervalExerciseName);
                                }else{
                                    activeExerciseName.setText("");
                                }
                                break;
                        }
                    } else{
                        //implies that index this is the LAST work time. Display end message and update UI accordingly
                        tripleBellPlayer.start();
                        //Display end message
                        roundNumber.setText("FINISHED");
                        roundNumber.setTextColor(ContextCompat.getColor(getContext(), R.color.bluePriComplement));
                        countdownTime.setText("");
                        activeExerciseName.setText("");
                        skip.setVisibility(View.INVISIBLE);
                        pause.setVisibility(View.INVISIBLE);
                        back.setVisibility(View.INVISIBLE);
                        newInterval.setVisibility(View.VISIBLE);
                        restartSet.setVisibility(View.VISIBLE);

                        newInterval.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Transition back to setup page. Reset some fields
                                if(!isTabata){
                                    ((IntervalTimerActivity)getActivity()).restartIntervalSetupFragment();
                                }else{
                                    ((IntervalTimerActivity)getActivity()).returnToIntervalMenu();
                                }
                                currentIndex = 0;
                            }
                        });
                        restartSet.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                currentIndex = 0;
                                restartSet.setVisibility(View.INVISIBLE);
                                newInterval.setVisibility(View.INVISIBLE);
                                roundNumber.setText("GET READY");
                                roundNumber.setTextColor(Color.parseColor("#000000"));
                                fiveSecondTimer.start();
                            }
                        });
                    }
                }
            };
        }
        return intervalTimerCountdownView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void disableIntervalCountdown(){
        userExitFrag = true;

    }
    public void enableIntervalCountdown(){
        userExitFrag = false;
    }


}