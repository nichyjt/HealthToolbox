package com.gmail.nichyekdev.healthtoolbox;

import androidx.annotation.Nullable;

public class FullReportCardItem {

    String date;
    String value;
    int type;
    //Applicable for BMI and BP only
    String exerciseName;
    //Applicable for Calorie Item only
    Integer extraCategory;

    public static int BMI_RECORD = 0;
    public static int BMR_RECORD = 1;
    public static int BFP_RECORD = 2;
    public static int BP_RECORD = 3;
    public static int CALORIE_BURN_RECORD = 4;

    public FullReportCardItem(String formattedDate, String value, int type, @Nullable Integer extraCategory){
        this.date = formattedDate;
        this.value = value;
        this.type = type;
        this.extraCategory = extraCategory;
    }

}
