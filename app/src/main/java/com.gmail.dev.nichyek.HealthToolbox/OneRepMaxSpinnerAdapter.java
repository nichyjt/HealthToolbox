package com.gmail.dev.nichyek.HealthToolbox;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OneRepMaxSpinnerAdapter extends ArrayAdapter<String> {

    ArrayList<String> equations;
    LayoutInflater inflater;

    public OneRepMaxSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.equations = (ArrayList<String>) objects;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = inflater.inflate(R.layout.spinner_dropdownitem, parent, false);
        ((ImageView)convertView.findViewById(R.id.spinner_icon)).setVisibility(View.GONE);
        TextView textView = convertView.findViewById(R.id.spinner_text);
        if(position==0){
            textView.setTextColor(Color.parseColor("#808080"));
        }
        textView.setText(equations.get(position));
        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
