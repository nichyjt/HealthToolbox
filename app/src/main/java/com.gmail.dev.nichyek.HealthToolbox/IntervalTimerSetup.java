package com.gmail.dev.nichyek.HealthToolbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class IntervalTimerSetup extends Fragment {

    //Upon launch of IntervalTimerActivity, this Fragment will be called.
    //This Fragment's function is for the user to set-up their Interval Workout.

    int numberOfItems = 0;
    boolean readyOn = false;

    @Nullable
    Integer routineIndex;
    String routineName;
    ArrayList<Interval_Item> myIntervalRounds =  new ArrayList<>();

    public IntervalTimerSetup(String routineName, @Nullable Integer routineIndex){
        this.routineName = routineName;
        this.routineIndex = routineIndex;
    }

    //"Import" arraylist data over
    public void setIntervalRounds(ArrayList<Interval_Item> myIntervalRounds){
        this.myIntervalRounds = myIntervalRounds;
        numberOfItems = myIntervalRounds.size();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final FragmentManager fm = getChildFragmentManager();

        // Inflate the layout for this fragment
        final View mView =  inflater.inflate(R.layout.fragment_interval_timer_setup, container, false);
        final AdapterView myIntervalItemListView = mView.findViewById(R.id.Interval_ItemHolder);

        //Instantiate the first item which is a 'work' IntervalItem
        if(numberOfItems==0) {
            //catches exception when set is reset
            myIntervalRounds.add(new Interval_Item(true, ""));
        }

        final IntervalTimerSetupAdapter myInterval_TimerSetupAdapter = new IntervalTimerSetupAdapter(getActivity(), myIntervalRounds, IntervalTimerSetup.this);
        myIntervalItemListView.setAdapter(myInterval_TimerSetupAdapter);
        myIntervalItemListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        //Instantiate UI elements
        final FloatingActionButton addItem = mView.findViewById(R.id.addIntervalRound);
        final FloatingActionButton readyButton = mView.findViewById(R.id.readyInterval);
        final FloatingActionButton saveButton = mView.findViewById(R.id.saveInterval);
        final FloatingActionButton startButton = mView.findViewById(R.id.quickStartInterval);
        final FloatingActionButton deleteLastRound = mView.findViewById(R.id.deleteLastRound);
        startButton.setVisibility(View.INVISIBLE);
        saveButton.setVisibility(View.INVISIBLE);

        //ADD ROUND button logic
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create a rest Item
                numberOfItems +=1;
                myIntervalRounds.add(new Interval_Item(false,"" ));
                //Create a work Item
                numberOfItems +=1;
                myIntervalRounds.add(new Interval_Item(true,""));
                myInterval_TimerSetupAdapter.notifyDataSetChanged();
                Snackbar.make(addItem, "Round added", Snackbar.LENGTH_SHORT).show();


            }
        });

        //Button logic
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyOn = !readyOn;
                if(readyOn){
                    saveButton.show();
                    startButton.show();
                }else{
                    saveButton.hide();
                    startButton.hide();
                }
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyOn = false;
                ((IntervalTimerActivity)getContext()).startCountdownFragment(myIntervalRounds);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_IntervalTimerSetupSave dialog = new Dialog_IntervalTimerSetupSave();
                dialog.setExistingName(routineName);
                dialog.show(fm, "INTERVAL_TIMER_SAVE_DIALOG");

            }
        });

        deleteLastRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteLastRound();
                myInterval_TimerSetupAdapter.notifyDataSetChanged();

            }
        });

        return mView;
    } //end of onCreateView


    //Deletes and re-indexes (if necessary) the arraylist
    private void deleteLastRound(){
        if(numberOfItems >1){
            myIntervalRounds.remove(numberOfItems-1);
            myIntervalRounds.remove(numberOfItems-2);
            numberOfItems -= 2;
            Snackbar snackbar =  Snackbar.make(getView(), "Last round removed", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    public void saveIntervalWorkout(String routineName){
        Snackbar snackbar =  Snackbar.make(getView(), "Interval Workout Saved!", Snackbar.LENGTH_SHORT);
        snackbar.show();
        Fragment menu = getActivity().getSupportFragmentManager().findFragmentByTag("INTERVAL_TIMER_MENU_FRAG");
        ((IntervalTimerMenu) menu).updateIntervalWorkoutList(true, routineName, myIntervalRounds, routineIndex);
    }

}


