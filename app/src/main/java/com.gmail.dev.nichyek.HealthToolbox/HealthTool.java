package com.gmail.dev.nichyek.HealthToolbox;


public class HealthTool {

    private int healthToolImageId;
    private String healthToolName;
    private String healthToolDescription;
    public int healthToolId;

    public HealthTool(int ToolId , String myHealthToolName , String myHealthToolDescription , int myHealthToolImageId){

        healthToolId = ToolId;
        healthToolName = myHealthToolName;
        healthToolImageId = myHealthToolImageId;
        healthToolDescription = myHealthToolDescription;

    }

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
