package com.gmail.dev.nichyek.HealthToolbox;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
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

//This class is the main coordinator for passing of data between fragments.
public class IntervalTimerMenu extends Fragment {

    Gson gson = new Gson();
    ArrayList<ArrayList<Interval_Item>> intervalWorkoutList = new ArrayList<>();
    ArrayList<String> intervalWorkoutNames = new ArrayList<>();
    IntervalTimerMenuAdapter adapter;

    public IntervalTimerMenu() {
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
        View view = inflater.inflate(R.layout.fragment_interval_timer_menu, container, false);
        ListView listView = view.findViewById(R.id.listView);
        adapter = new IntervalTimerMenuAdapter(getActivity(), R.layout.interval_timer_menu_item, intervalWorkoutNames);
        listView.setAdapter(adapter);
        loadIntervalWorkoutList();

        return view;
    }

    public void startIntervalSetup(int index){
        int len = intervalWorkoutNames.size();
        if(index == len-1){ //new workout
            ((IntervalTimerActivity)getActivity()).startIntervalSetupFragment(null, "", null);
        }else if(index == len-2){ //tabata
            ((IntervalTimerActivity) getActivity()).startCountdownFragment(null);
        }else{ //others
            ArrayList<Interval_Item> routine = intervalWorkoutList.get(index);
            ((IntervalTimerActivity)getActivity()).startIntervalSetupFragment(routine, intervalWorkoutNames.get(index), index);
        }
    }

    //File structure is similar to WorkoutPlanner.
    //"IntervalWorkoutRoutineData" is stored in a JSONArray with 2 elems.
    //1. "intervalWorkoutNames" -> stores an array of (String) names
    //2. "intervalWorkoutData" -> stores a (Collection) of (ArrayList<ArrayList<WorkoutPlanner_Item>>)
    //In both (Lists), the same index points to the same WorkoutRoutine & its Name.

    public void updateIntervalWorkoutList(boolean addNotDelete, @Nullable String name, @Nullable ArrayList<Interval_Item> routine, @Nullable Integer index){
        //Remove "NEW WORKOUT" & "TABATA"
        intervalWorkoutNames.remove(intervalWorkoutList.size());
        intervalWorkoutNames.remove(intervalWorkoutList.size());

        if(addNotDelete){ //addition or  logic
            if(name==null || routine==null){
                return;
            }
            if(index!=null){
                intervalWorkoutNames.set(index, name);
                intervalWorkoutList.set(index, routine);
            }else{
                intervalWorkoutNames.add(name);
                intervalWorkoutList.add(routine);
            }
        }else{ //deletion logic
            intervalWorkoutNames.remove((int) index);
            intervalWorkoutList.remove((int) index);
        }
        saveIntervalWorkoutData();
        loadIntervalWorkoutList();
    }

    public void saveIntervalWorkoutData(){
        FileOutputStream fos = null;
        Collection collection = new ArrayList();
        collection.add(intervalWorkoutNames);
        collection.add(intervalWorkoutList);
        String newJson = gson.toJson(collection);

        try{
            fos = getActivity().openFileOutput("IntervalWorkoutRoutineData", Context.MODE_PRIVATE);
            fos.write(newJson.getBytes());
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try{
                    fos.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }


    }

    private void loadIntervalWorkoutList(){
        intervalWorkoutNames.clear();
        intervalWorkoutList.clear();

        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        try{
            fis = getContext().openFileInput("IntervalWorkoutRoutineData");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String s = br.readLine();
            while(s!=null){
                sb.append(s).append('\n');
                s = br.readLine();
            }
        } catch(IOException e){
            e.printStackTrace();
        }finally{
            if(fis!=null){
                try{
                    fis.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            String jsonRaw = sb.toString();
            if(!(jsonRaw.length()==0)){
                JsonArray parsedJSONArray = JsonParser.parseString(jsonRaw).getAsJsonArray();
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                Type type1 = new TypeToken<ArrayList<ArrayList<Interval_Item>>>(){}.getType();
                ArrayList<String> nameHolder = gson.fromJson(parsedJSONArray.get(0), type);
                intervalWorkoutNames.addAll(nameHolder);
                ArrayList<ArrayList<Interval_Item>> routinesHolder = gson.fromJson(parsedJSONArray.get(1), type1);
                intervalWorkoutList.addAll(routinesHolder);
                routinesHolder.clear();
                nameHolder.clear();
            }
            intervalWorkoutNames.add("TABATA");
            intervalWorkoutNames.add("Create new workout");
            adapter.notifyDataSetChanged();
        }
    }
}