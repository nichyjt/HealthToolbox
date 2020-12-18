package com.gmail.nichyekdev.healthtoolbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class WorkoutPlanner_Setup extends Fragment {

    ArrayList<WorkoutPlanner_Item> workoutPlanner_items =  new ArrayList<>();
    ArrayList<WorkoutPlanner_Item> routine;
    int workoutDataPosition;
    String WorkoutName = null;
    boolean menuActive = false;
    final WorkoutPlanner_SetupAdapter mAdapter = new WorkoutPlanner_SetupAdapter(workoutPlanner_items, this);

    public WorkoutPlanner_Setup() {
        // Required empty public constructor
    }

    public void setSetupParams(ArrayList<WorkoutPlanner_Item> routine, int position, @Nullable String existingName){
        this.routine = routine;
        this.workoutDataPosition = position;
        this.WorkoutName = existingName;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final FragmentManager fm = getChildFragmentManager();

        //if this is not a new workout, load the existing data
        if(routine!=null){
            workoutPlanner_items.addAll(routine);
        }else{
            workoutPlanner_items.add(new WorkoutPlanner_Item("Exercise 1", 1, 1,-1));
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout_planner__setup, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new WorkoutPlanner_SetupTouchHelper(mAdapter, getContext()));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //Button UI
        final FloatingActionButton menu = view.findViewById(R.id.WorkoutPlanner_Setup_MenuButton);
        final FloatingActionButton save = view.findViewById(R.id.WorkoutPlanner_Setup_SaveButton);
        save.setVisibility(View.INVISIBLE);
        final FloatingActionButton addItem = view.findViewById(R.id.WorkoutPlanner_Setup_AddExercise);
        addItem.setVisibility(View.INVISIBLE);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!menuActive){ //activate options
                    addItem.show();
                    save.show();
                    menuActive = true;
                }else{ //deactivate options
                    addItem.hide();
                    save.hide();
                    menuActive = false;
                }
            }
        });
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = workoutPlanner_items.size();
                workoutPlanner_items.add(new WorkoutPlanner_Item("Exercise " + (size+1), 1, 1 ,-1));
                mAdapter.notifyItemInserted(size);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogWorkoutSetupInvalid errorDialog = new DialogWorkoutSetupInvalid();
                for(WorkoutPlanner_Item item:workoutPlanner_items){
                    if(!item.checkValidity()){
                        errorDialog.show(fm, "INVALID_SETUP_DIALOG");
                        return;
                    }
                }
                Dialog_WorkoutPlannerSetupSave saveDialog = new Dialog_WorkoutPlannerSetupSave();
                if(WorkoutName!=null) {
                    saveDialog.setExistingName(WorkoutName);
                    saveDialog.setDescriptionText("Update name? (Optional)\nClick save to continue.");
                }else{
                    saveDialog.setDescriptionText("Give your new workout a name!");
                }
                //NOTE: tag is used to access fragment state when necessary.
                //Also allows to get a handle to the fragment by calling findFragmentByTag().
                saveDialog.show(fm, "saveworkout");

            }
        });

        //RecyclerView and FAB scroll logic
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollVertically(1) && !recyclerView.canScrollVertically(-1)){
                    menu.show();
                    if(menuActive){
                        addItem.show();
                        save.show();
                    }

                }
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy>3){
                    menu.hide();
                    if(menuActive){
                        addItem.hide();
                        save.hide();
                    }
                }else if(dy<-3){
                    menu.show();
                    if(menuActive){
                        addItem.show();
                        save.show();
                    }
                }
            }
        });
        return view;
    }
    public static class DialogWorkoutSetupInvalid extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Did you give every workout a name?");
            builder.setTitle("Warning");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Nothing to be done.
                }
            });
            return builder.create();
        }
    }

    public void saveRoutine(String newName){
        Fragment mainMenu = getActivity().getSupportFragmentManager().findFragmentByTag("FRAGMENT_WORKOUT_PLANNER_MENU");
        ((WorkoutPlanner_MainMenu)mainMenu).saveNewWorkoutPlannerData(workoutDataPosition, newName, workoutPlanner_items);
    }

}