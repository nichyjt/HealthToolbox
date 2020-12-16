package com.gmail.dev.nichyek.HealthToolbox;


//This class includes the details of a healthTool
public class HealthTool {

    private int healthToolImageId;
    private String healthToolName;
    private String healthToolDescription;
    public int healthToolId;


    //healthTool object constructor
    public HealthTool(int ToolId , String myHealthToolName , String myHealthToolDescription , int myHealthToolImageId){

        healthToolId = ToolId;
        healthToolName = myHealthToolName;
        healthToolImageId = myHealthToolImageId;
        healthToolDescription = myHealthToolDescription;

    }

    //methods to get the parameters for a healthTool
    public int getHealthToolId(){
        return  healthToolId;
    }
    public String getHealthToolName(){
        return healthToolName;
    }
    public String getHealthToolDescription(){
        return  healthToolDescription;
    }
    public int getHealthToolImageId(){
        return healthToolImageId;
    }


}
