package com.gmail.dev.nichyek.HealthToolbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HealthToolFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.activity_health_tools_fragment, container, false);

        //Create an ArrayList that holds an array of healthTool objects
        final ArrayList<HealthTool> myHealthToolsList = new ArrayList<>();
        myHealthToolsList.add(new HealthTool(1, "Body Mass Index", "Find your BMI based on WHO's BMI Classification.", R.drawable.ic_bmi_color));
        myHealthToolsList.add(new HealthTool(2,"Basal Metabolic Rate", "Calculate the minimum number of calories your body burns daily.", R.drawable.ic_bmr_color));
        myHealthToolsList.add(new HealthTool(3,"Manual Heart Rate Timer", "Manually time your heart rate with this tool.", R.drawable.ic_heart_timer));
        myHealthToolsList.add(new HealthTool(4, "Body Fat Percentage", "Calculate an estimate of your body fat percentage.", R.drawable.ic_bfp_color));
        myHealthToolsList.add(new HealthTool(5, "Blood Pressure Tracker", "Keep logs for your blood pressure.", R.drawable.ic_blood_pressure));

        //create an adapter for listView to use; a listView object uses adapter
        // An adapter takes the array and converts it to a UI component (listView)
        HealthToolAdapter healthAdapter = new HealthToolAdapter(getActivity(), myHealthToolsList);
        AdapterView listView = inflatedView.findViewById(R.id.healthToolsList);
        listView.setAdapter(healthAdapter);


        //sets listeners for each item in the listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Class toolClass;
                int myToolId = myHealthToolsList.get(i).getHealthToolId();
                //This is where your toolId in the comments gets related to the activity to launch.
                switch(myToolId){
                    case 1:
                        toolClass = BmiToolActivity.class;
                        break;
                    case 2:
                        toolClass = BmrToolActivity.class;
                        break;
                    case 3:
                        toolClass = HeartRateActivity.class;
                        break;
                    case 4:
                        toolClass = BodyFatPercentage.class;
                        break;
                    case 5:
                        toolClass = BloodPressureTracker.class;
                        break;
                    default:
                        toolClass = MainActivity.class;
                }

                //creates an intent to start an activity/fragment as specified in the array.
                Intent intent = new Intent(HealthToolFragment.this.getContext(), toolClass);
                startActivity(intent);

                }
        });

        return inflatedView;
    }
}

