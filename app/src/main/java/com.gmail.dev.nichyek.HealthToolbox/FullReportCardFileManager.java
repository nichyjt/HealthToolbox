package com.gmail.dev.nichyek.HealthToolbox;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

//Saves and loads HealthTool files as needed
public class FullReportCardFileManager {

    Gson gson = new Gson();
    Context context;
    ArrayList<String> monthStrings = new ArrayList<>();
    ArrayList<Integer> newMonthIndexes = new ArrayList<>();
    //For reference
    //"BMI_Data" type 0
    //"BMR_Data" type 1
    //"BFP_Data" type 2
    //"BP_Data" type 3
    //"CAL_Data" type 4
    public FullReportCardFileManager(Context context){
        this.context = context;
    }

    public void saveHealthToolRecord(int type, FullReportCardItem item, @Nullable Integer removeAtIndex){
        String filename = getFileName(type);
        ArrayList<FullReportCardItem> dataList = loadReportCardItemData(type);
        if(dataList==null || dataList.size()==0){ //First save
            dataList =  new ArrayList<>();
            dataList.add(item);
        }else if(removeAtIndex!=null){ //delete logic
            //Invert index as removeAtIndex = 0 refers to the last elem in dataList
            int index = dataList.size()-1-removeAtIndex;
            dataList.remove(index);
        }else{
            //Newest readings are parked in last index
            FullReportCardItem lastItem = dataList.get(dataList.size()-1);
            //Overwrite logic
            if(lastItem.date.equals(item.date) && type!=4){
                dataList.set(dataList.size()-1, item);
            }else{
                dataList.add(item);
            }

        }

        String dataListJson = gson.toJson(dataList);
        FileOutputStream fos = null;
        try{
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(dataListJson.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fos!=null){
                try{
                    fos.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<FullReportCardItem> loadReportCardItemData(int type){
        String filename = getFileName(type);
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        try{
            fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String s = br.readLine();
            while(s!=null) {
                sb.append(s).append('\n');
                s = br.readLine();
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try {
                if(fis!=null){
                    fis.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        String rawjson = sb.toString();
        if(rawjson.length()==0) return null;
        Type varType = new TypeToken<ArrayList<FullReportCardItem>>(){}.getType();
        return gson.fromJson(rawjson, varType);
    }


    public FullReportCardItem[] prepareReportDataForAdapter(int type){
        ArrayList<FullReportCardItem> arrList = loadReportCardItemData(type);
        if(arrList == null) return null;
        FullReportCardItem[] arr = new FullReportCardItem[arrList.size()];
        for(int i=arrList.size()-1, j=0; i>=0; --i, ++j){
            //iterate backwards so tt newest data are logged first
            arr[j] = arrList.get(i);
        }
        compileMonthlyStringAndIndex(arr);
        return arr;
    }
    //String contains Month-Year (e.g. December 2020), ArrayList holds records of the month's recordings.
    private void compileMonthlyStringAndIndex(FullReportCardItem[] array){
        if(monthStrings.size()!=0||newMonthIndexes.size()!=0){
            monthStrings.clear();
            newMonthIndexes.clear();
        }
        String currentMonthYear = "";
        for(int i=0; i<array.length; ++i){
            //iterated forwards as new data is already at the front
            FullReportCardItem item = array[i];
            String monthYear = findMonthYearString(item.date);
            // item.date = "DD/MM/YYYY"
            if(!(currentMonthYear.equals(monthYear))){
                currentMonthYear = monthYear;
                monthStrings.add(monthYear);
                newMonthIndexes.add(i);
            }
        }
    }

    private String getFileName(int type){
        String filename;
        switch(type){
            case 0:
                filename = "BMI_Data";
                break;
            case 1:
                filename = "BMR_Data";
                break;
            case 2:
                filename = "BFP_Data";
                break;
            case 3:
                filename = "BP_Data";
                break;
            case 4:
                filename = "CAL_Data";
                break;
            default:
                filename = "";
                break;
        }
        return filename;
    }

    public ArrayList<String> getMonthStrings(){
        return monthStrings;
    }
    public ArrayList<Integer> getNewMonthIndexes(){
        return newMonthIndexes;
    }

    //Takes MM/YYYY and converts it to a UI-friendly String (eg. June 2020)
    private String findMonthYearString(String rawString){
        String string = "";
        int month = Integer.parseInt(rawString.substring(3,5));
        switch(month){
            case 1:
                string += "January";
                break;
            case 2:
                string += "February";
                break;
            case 3:
                string += "March";
                break;
            case 4:
                string += "April";
                break;
            case 5:
                string += "May";
                break;
            case 6:
                string += "June";
                break;
            case 7:
                string += "July";
                break;
            case 8:
                string += "August";
                break;
            case 9:
                string += "September";
                break;
            case 10:
                string += "October";
                break;
            case 11:
                string += "November";
                break;
            case 12:
                string += "December";
                break;
        }
        String year = rawString.substring(6);
        return string + " "+ year;
    }
}
