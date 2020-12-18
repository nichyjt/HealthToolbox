package com.gmail.nichyekdev.healthtoolbox;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class HealthToolAdapter extends ArrayAdapter<HealthTool> {

    public HealthToolAdapter(Activity context, ArrayList<HealthTool> healthToolList){
        super(context, 0, healthToolList);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.health_tool_list_item, parent, false);

        TextView myHealthToolName =convertView.findViewById(R.id.healthToolName);
        ImageView myHealthToolIcon = convertView.findViewById(R.id.healthToolIcon);
        TextView myHealthToolDescription = convertView.findViewById(R.id.healthToolDescription);

        myHealthToolName.setText(getItem(position).getHealthToolName());
        myHealthToolIcon.setImageResource(getItem(position).getHealthToolImageId());
        myHealthToolDescription.setText(getItem(position).getHealthToolDescription());

        myHealthToolDescription.setClickable(false);
        myHealthToolName.setClickable(false);


        return convertView;
    }
}

