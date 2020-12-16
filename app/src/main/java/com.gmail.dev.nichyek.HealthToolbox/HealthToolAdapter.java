package com.gmail.dev.nichyek.HealthToolbox;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class HealthToolAdapter extends ArrayAdapter<HealthTool> {

    /* healthToolAdapter constructor. An arraylist adapter requires context & the array type
    using: ArrayAdapter(Context context, int resource, List<T> objects) */
    public HealthToolAdapter(Activity context, ArrayList<HealthTool> healthToolList){
        //the below statement is referring to the (Context context, int resource, List<T> objects)
        super(context, 0, healthToolList);
    }

    //The code below is the view returned by the healthToolAdapter.
    //There is a need to define getView to return a custom view. (Refer to documentation)
    //It automatically(?) triggers when it loads the initial list and new items when scrolling

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        //convertView gets the view layout from a healthToolListItem
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.health_tool_list_item, parent, false);

        //The below code gets the UI components from an 'item' and associates it to a variable for further manipulation
        TextView myHealthToolName =convertView.findViewById(R.id.healthToolName);
        ImageView myHealthToolIcon = convertView.findViewById(R.id.healthToolIcon);
        TextView myHealthToolDescription = convertView.findViewById(R.id.healthToolDescription);

        //The below code puts in params specific to the item's function.
        myHealthToolName.setText(getItem(position).getHealthToolName());
        myHealthToolIcon.setImageResource(getItem(position).getHealthToolImageId());
        myHealthToolDescription.setText(getItem(position).getHealthToolDescription());

        myHealthToolDescription.setClickable(false);
        myHealthToolName.setClickable(false);


        return convertView;
    }
}

