package com.gmail.nichyekdev.healthtoolbox;


//This class defines the parameters of a WorkoutTool object
public class WorkoutTool {

    int myWorkoutToolId;
    String myWorkoutToolName;
    String myWorkoutToolDescription;
    int myWorkoutToolImageId;

    //Constructor
    public WorkoutTool(int workoutToolId, String workoutToolName, String workoutToolDescription, int workoutToolImageId){
        myWorkoutToolId = workoutToolId;
        myWorkoutToolName = workoutToolName;
        myWorkoutToolDescription = workoutToolDescription;
        myWorkoutToolImageId = workoutToolImageId;
    }

    public int getMyWorkoutToolId() {
        return myWorkoutToolId;
    }

    public String getMyWorkoutToolName() {
        return myWorkoutToolName;
    }

    public String getMyWorkoutToolDescription() {
        return myWorkoutToolDescription;
    }

    public int getMyWorkoutToolImageId() {
        return myWorkoutToolImageId;
    }


}
