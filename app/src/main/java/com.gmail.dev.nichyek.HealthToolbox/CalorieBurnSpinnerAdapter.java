package com.gmail.dev.nichyek.HealthToolbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CalorieBurnSpinnerAdapter extends ArrayAdapter<String> {

    ArrayList<String> type;
    LayoutInflater inflater;

    public CalorieBurnSpinnerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.type =  objects;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //No need for viewholder as list is very small.
        convertView = inflater.inflate(R.layout.spinner_dropdownitem, parent, false);
        ImageView imageView = convertView.findViewById(R.id.spinner_icon);
        TextView textView = convertView.findViewById(R.id.spinner_text);

        String exType = type.get(position);
        textView.setText(exType);
        //Set Image Resources.
        switch (exType) {
            case "Running":
                imageView.setImageResource(R.drawable.ic_running_man);
                break;
            case "Cycling":
                imageView.setImageResource(R.drawable.ic_bicycle);
                break;
            case "Sports":
                imageView.setImageResource(R.drawable.ic_soccer_player);
                break;
            case "Training":
                imageView.setImageResource(R.drawable.ic_man_lifting_weight);
                break;
            case "Water Activities":
                imageView.setImageResource(R.drawable.ic_swimmer);
                break;
            case "Special":
                imageView.setImageResource(R.drawable.ic_stretching_exercises);
                break;
            default:  //custom
                imageView.setImageResource(R.drawable.ic_menu_threedot);
                break;
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}
