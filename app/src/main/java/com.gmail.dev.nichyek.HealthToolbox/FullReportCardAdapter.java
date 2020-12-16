package com.gmail.dev.nichyek.HealthToolbox;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FullReportCardAdapter extends ArrayAdapter<String> {

    ClipboardManager clipboardManager;

    LayoutInflater inflater;
    ArrayList<String> months;
    ArrayList<Integer> indexes;
    FullReportCardItem[] arr;
    LinearLayout[] layoutHolder;
    Context context;
    Activity parentActivity;
    int type;
    public FullReportCardAdapter(@NonNull Context context, int resource, ArrayList<String> objects, ArrayList<Integer> indexes, FullReportCardItem[] arr) {
        super(context, resource, objects);
        this.months = objects;
        this.indexes = indexes;
        this.arr = arr;
        this.type = arr[0].type;
        this.context = context;
        inflater = LayoutInflater.from(context);
        layoutHolder = new LinearLayout[months.size()];
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }


    private static class ViewHolder{
        TextView monthText;
        LinearLayout recordContainer;
        TextView specialText;
        public ViewHolder(View view){
            monthText = view.findViewById(R.id.reportCardMonthYearText);
            recordContainer = view.findViewById(R.id.reportCardMonthItemContainer);
            specialText = view.findViewById(R.id.reportCardMonthYearSummaryText);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.report_card_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //Get the MONTH string and MONTHRECORDS
        holder.monthText.setText(months.get(position));
        holder.recordContainer.removeAllViews();
        int start = indexes.get(position);
        int end = (position+1>indexes.size()-1)? arr.length:indexes.get(position+1);
        for(int i=start; i<end; ++i){
            holder.recordContainer.addView(makeRecordView(i, start));
        }
        String string = getCustomText(start, end);
        if(string.equals("")){
            holder.specialText.setVisibility(View.GONE);
        }else{
            holder.specialText.setText(getCustomText(start, end));
        }
        return convertView;
    }

    private View makeRecordView(final int position, int firstPosition){
        View view = null;
        final FullReportCardItem item = arr[position];
        switch(type){
            case 0:
            case 1:
            case 2:
                view = inflater.inflate(R.layout.report_card_month_item, null);
                ((TextView) view.findViewById(R.id.reportCardMonthItem_MainText)).setText(item.value);
                ((TextView) view.findViewById(R.id.reportCardMonthItem_Date)).setText(item.date);
                view.findViewById(R.id.reportCardMonthItem_CopyButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipData data = ClipData.newPlainText("ReportItem", item.value);
                        clipboardManager.setPrimaryClip(data);
                        Snackbar.make(view, item.value + " Copied to clipboard", Snackbar.LENGTH_SHORT).show();
                    }
                });
                //Delete
                view.findViewById(R.id.reportCardMonthItem_DeleteButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmDeleteRecord(position);
                    }
                });
                int backgroundColor = getCatBackground(item.type, item.extraCategory);
                ((TextView) view.findViewById(R.id.reportCardMonthItem_MainText)).setTextColor(backgroundColor);
                if (position == firstPosition)
                    view.findViewById(R.id.divider).setVisibility(View.GONE);
                break;
            case 3: //BP Logger
                view = inflater.inflate(R.layout.report_card_month_item_description, null);
                ((TextView) view.findViewById(R.id.reportCardDescriptionItemDate)).setText(item.date);
                TextView valueText = view.findViewById(R.id.reportCardDescriptionItemValue);
                TextView descriptionText = view.findViewById(R.id.reportCardDescription);
                switch (item.extraCategory) {
                    case 0:
                        descriptionText.setText("Low Blood Pressure");
                        descriptionText.setTextColor(ContextCompat.getColor(getContext(), R.color.skyBlue));
                        valueText.setTextColor(ContextCompat.getColor(getContext(), R.color.skyBlue));
                        break;
                    case 1:
                        descriptionText.setText("Healthy Range");
                        descriptionText.setTextColor(Color.parseColor("#2ECC71"));
                        valueText.setTextColor(Color.parseColor("#2ECC71"));
                        break;
                    case 2:
                        descriptionText.setText("Elevated Range");
                        descriptionText.setTextColor(Color.parseColor("#F1C40F"));
                        valueText.setTextColor(Color.parseColor("#F1C40F"));
                        break;
                    case 3:
                        descriptionText.setText("Hypertension Stage 1");
                        descriptionText.setTextColor(Color.parseColor("#E59866"));
                        valueText.setTextColor(Color.parseColor("#E59866"));
                        break;
                    case 4:
                        descriptionText.setText("Hypertension Stage 2");
                        descriptionText.setTextColor(Color.parseColor("#EF5350"));
                        valueText.setTextColor(Color.parseColor("#EF5350"));
                        break;
                    case 5:
                        descriptionText.setText("Hypertentive Crisis");
                        descriptionText.setTextColor(Color.parseColor("#D32F2F"));
                        valueText.setTextColor(Color.parseColor("#D32F2F"));
                        break;
                }
                descriptionText.setTextSize(24);
                valueText.setText(item.value);
                view.findViewById(R.id.reportCardDescriptionItemDelete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmDeleteRecord(position);
                    }
                });
                if (position == firstPosition)
                    view.findViewById(R.id.divider).setVisibility(View.GONE);
                break;
            case 4: //Calorie Logger
                view = inflater.inflate(R.layout.report_card_month_item_description, null);
                ((TextView) view.findViewById(R.id.reportCardDescriptionItemValue)).setText(item.value+" kcal");
                ((TextView) view.findViewById(R.id.reportCardDescriptionItemDate)).setText(item.date);
                ((TextView) view.findViewById(R.id.reportCardDescription)).setText(item.exerciseName);
                view.findViewById(R.id.reportCardDescriptionItemDelete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmDeleteRecord(position);
                    }
                });
                if (position == firstPosition)
                    view.findViewById(R.id.divider).setVisibility(View.GONE);
                break;
        }
        return view;
    }

    private int getCatBackground(int type, @Nullable Integer cat){
        if(cat==null) return Color.parseColor("#000000");
        int color = Color.parseColor("#000000");
        if(type == FullReportCardItem.BMI_RECORD){
            switch(cat) {
                case 0:
                case 1:
                case 2:
                case 6:
                case 7:
                    color = ContextCompat.getColor(getContext(), R.color.crimsonError);
                    break;
                case 3:
                case 5:
                    color = ContextCompat.getColor(getContext(), R.color.orangeAlert);
                    break;
                case 4:
                    color = ContextCompat.getColor(getContext(), R.color.colorPrimary);
                    break;
            }
        }else if(type == FullReportCardItem.BP_RECORD){
                switch(cat){
                    case 0:
                        color = ContextCompat.getColor(getContext(), R.color.skyBlue);
                        break;
                    case 1:
                        color = ContextCompat.getColor(getContext(), R.color.colorPrimary);
                        break;
                    case 2:
                        color = ContextCompat.getColor(getContext(), R.color.lightOrange);
                        break;
                    case 3:
                        color = ContextCompat.getColor(getContext(), R.color.orangeAlert);
                        break;
                    case 4:
                        color = ContextCompat.getColor(getContext(), R.color.lightRed);
                        break;
                    case 5:
                        color = ContextCompat.getColor(getContext(), R.color.crimsonError);
                        break;
                    }
                }
        return color;
    }

    private String getCustomText(int start, int end){
        DecimalFormat twoDecimalPlaces = new DecimalFormat("##.##");
        String string = "";
        switch(type){
            case 0: //bmi
                Double avg = 0.0;
                int n = end-start;
                for(int i = start; i<end; ++i){
                    avg+=Double.parseDouble(arr[i].value);
                }
                avg/=n;
                string = "Monthly Average: "+twoDecimalPlaces.format(avg);
                break;
            case 4: //Calorie Calc
                double total = 0;
                for(int i=start; i<end; ++i){
                    total+=Integer.parseInt(arr[i].value);
                }
                string = "Monthly Total: "+total+ " kcal";
                break;
        }
        return string;
    }

    private void confirmDeleteRecord(int position){
        Dialog_DeleteConfirmation dialog = new Dialog_DeleteConfirmation();
        dialog.record_type = type;
        dialog.setRequiredParams(Dialog_DeleteConfirmation.DELETE_REPORT_ITEM, null,
                position, "WARNING", "Delete record?\nThis cannot be undone.");
        dialog.activityContext = parentActivity;
        dialog.show(((FullReportCardActivity)parentActivity).fm, "DELETE_DIALOG");
    }



}

