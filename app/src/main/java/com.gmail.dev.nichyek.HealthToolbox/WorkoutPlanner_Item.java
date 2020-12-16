package com.gmail.dev.nichyek.HealthToolbox;

public class WorkoutPlanner_Item {

    String name;
    //Note, '-1' denotes special cases for weight, numReps and time.
    //It means that the exercise is weightless, reps to failure, and timer is not used respectively.
    int weight;
    int numReps;
    int time;
    //Constructor
    public WorkoutPlanner_Item(String workoutName, int weightUsed, int reps, int timeNeeded){
        name = workoutName;
        weight = weightUsed;
        numReps = reps;
        time = timeNeeded;
    }

    public boolean checkValidity(){
        return (name.trim().length()>0);
    }


}
