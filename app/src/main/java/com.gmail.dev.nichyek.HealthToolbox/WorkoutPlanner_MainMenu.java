package com.gmail.dev.nichyek.HealthToolbox;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

//This class is the primary coordinator of the files storing the workout names and routines.
//This class also handles the MainMenu UI.
public class WorkoutPlanner_MainMenu extends Fragment {

    Gson gson = new Gson();
    ArrayList<String> workoutNames = new ArrayList<>();
    ArrayList<ArrayList<WorkoutPlanner_Item>> workoutRoutineList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    public WorkoutPlanner_MainMenu() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout_planner__mainmenu, container, false);
        ListView listView = view.findViewById(R.id.WorkoutPlanner_MainMenu_ListView);
        adapter = new WorkoutPlanner_MainMenu_Adapter(getActivity(), R.layout.workoutplanner_mainmenu_item, workoutNames);
        listView.setAdapter(adapter);
        loadData();
        adapter.notifyDataSetChanged();
        return view;
    }

    //"WorkoutPlannerData" is stored in 2 ArrayLists.
    //1. "workoutPlannerNames" -> stores an array of (String) names
    //2. "workoutPlannerLists" -> stores a (Collection) of (ArrayList<ArrayList<WorkoutPlanner_Item>>)
    //The overall JSONArray Collection of (Arraylist<str> & Arraylist<wpi>)
    //In both (Lists), the same index points to the same WorkoutRoutine & its Name.
    //We MUST save the data in this order: name -> wpiList

    private void loadData(){
        workoutNames.clear();
        workoutRoutineList.clear();

        FileInputStream fis;
        StringBuilder sb = new StringBuilder();
        String workoutPlannerDataJson;
        try{
            fis = getActivity().openFileInput("WorkoutPlannerData");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String s = br.readLine();
            while(s!=null){
                sb.append(s).append("\n");
                s = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            workoutPlannerDataJson = sb.toString();
        }
        //First run check: file does not exist
        if(workoutPlannerDataJson.length()==0){
            workoutNames.add("Create New Workout");
            adapter.notifyDataSetChanged();
            return;
        }
        //File exists, assign data
        JsonArray rawData = JsonParser.parseString(workoutPlannerDataJson).getAsJsonArray();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        Type type1 = new TypeToken<ArrayList<ArrayList<WorkoutPlanner_Item>>>(){}.getType();
        ArrayList<String> nameHolder = gson.fromJson(rawData.get(0), type);
        workoutNames.addAll(nameHolder);
        workoutNames.add("Create New Workout");
        ArrayList<ArrayList<WorkoutPlanner_Item>> routinesHolder = gson.fromJson(rawData.get(1), type1);
        workoutRoutineList.addAll(routinesHolder);
        adapter.notifyDataSetChanged();
    }


    public void saveNewWorkoutPlannerData(int position, String newName, ArrayList<WorkoutPlanner_Item> updatedRoutine){
        //Prepare data
        if(position==-1){ //add to back of array. Replace "Create New Workout"
            workoutRoutineList.add(updatedRoutine);
            workoutNames.set(workoutNames.size()-1, newName);
        }else{
            workoutRoutineList.set(position, updatedRoutine);
            workoutNames.set(position, newName);
            workoutNames.remove(workoutNames.size()-1);
        }
        saveWorkoutPlannerData();
    }

    public void deleteWorkoutPlannerData(int position){
        workoutNames.remove(workoutNames.size()-1);
        workoutNames.remove(position);
        workoutRoutineList.remove(position);
        saveWorkoutPlannerData();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void saveWorkoutPlannerData(){
        //Save data
        Collection collection = new ArrayList();
        collection.add(workoutNames);
        collection.add(workoutRoutineList);
        String newJson = gson.toJson(collection);

        FileOutputStream fos = null;
        try{
            fos = getActivity().openFileOutput("WorkoutPlannerData", Context.MODE_PRIVATE);
            fos.write(newJson.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(fos!=null) fos.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        //Update UI accordingly.
        loadData();
    }

    //Takes a position value and sends the routine arraylist needed
    //No key-pair value needed as the whole thing is saved as a jsonarray.
    public ArrayList<WorkoutPlanner_Item> getRoutine(int position){
        //Type type = new TypeToken<ArrayList<WorkoutPlanner_Item>>(){}.getType();
        return (workoutRoutineList.get(position));
    }



}