package com.gmail.dev.nichyek.HealthToolbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class DebugFragment extends Fragment {

    ArrayList<WorkoutPlanner_Item> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View debugFragmentInflated = inflater.inflate(R.layout.activity_debug_fragment, container, false);
        TextView debugFragmentText = debugFragmentInflated.findViewById(R.id.debugText);
        debugFragmentText.setText("Hello this is DebugFragment!");
        String debugText="";
        if(list!=null){
            for(int i=0; i<list.size(); i++){
                WorkoutPlanner_Item item = list.get(i);
                debugText+="NAME: "+item.name;
            }
            debugFragmentText.setText(debugText);
        }




        /*ViewPager2 bottomMenuViewPager = fakeFragmentInflated.findViewById(R.id.myViewPager);
        Fragment DebugFragment = this;
        //the adapter contains (adapts) information of the items that are to be used by the view pager
        FragmentStateAdapter bottomMenuFragmentAdapter = new FragmentStateAdapter(DebugFragment) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                //Provide a new Fragment associated with the specified position.
                switch (position){
                    case 0:
                        return new DebugFragment();
                    case 1:
                        return new healthToolsFragment();
                    case 2:
                        return new DebugFragment();
                }
                return null;
            }

            @Override
            public int getItemCount() {
                return 3; //Returns the total number of items in the data set held by the adapter.
            }
        };
        bottomMenuViewPager.setAdapter(bottomMenuFragmentAdapter); */




        //do not delete the below
        return debugFragmentInflated;
    }

    public void setDebugArgs(ArrayList<WorkoutPlanner_Item> list){
        this.list = list;
    }

}




