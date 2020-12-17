package com.gmail.dev.nichyek.HealthToolbox;


//This class describes the params of an interval list item to be used in the Interval Timer activity
// Each Interval Item created has TWO types. Either  WORK or REST which will affect the respective listItem params
// Each Interval Item will govern 1) The LENGTH of the round 2) UI Elements

public class Interval_Item {

    boolean isWork;
    int intervalItemLength;
    String intervalExerciseName;

    //Interval_Item constructor
    public Interval_Item(boolean isWorkNotRest, String exerciseName){
        //This indexes all of the items

        //This indexes the work/rest items,
        //and pre-sets the Interval's time to 20s if it is work, or 10s if it is a rest
        if(isWorkNotRest){
            intervalItemLength = 20;
        } else {
            intervalItemLength = 10;
        }

        //This checks the type of item
        this.isWork = isWorkNotRest;
        //Log the exerciseNames
        this.intervalExerciseName = exerciseName;
    }


    //Code to get and set the interval round data
    public String getIntervalItemName(){return intervalExerciseName;}
    public void setIntervalExerciseName(String intervalName){intervalExerciseName = intervalName;}
}
