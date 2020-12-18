package com.gmail.nichyekdev.healthtoolbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;


public class MyStatisticsFragment extends Fragment {

    FullReportCardFileManager fileManager;
    ConstraintLayout constraintLayout;
    LinearLayout linearLayout;
    FullReportCardItem[] bmiData;
    FullReportCardItem[] bmrData;
    FullReportCardItem[] bfpData;
    FullReportCardItem[] bpData;
    FullReportCardItem[] calBurnData;
    boolean needsRefresh = false;
    boolean snapshotOpen = false;
    LayoutInflater statsInflater;

    public MyStatisticsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_my_statistics, container, false);
        statsInflater = LayoutInflater.from(getContext());
        fileManager = new FullReportCardFileManager(getContext());
        bmiData = fileManager.prepareReportDataForAdapter(FullReportCardItem.BMI_RECORD);
        bmrData = fileManager.prepareReportDataForAdapter(FullReportCardItem.BMR_RECORD);
        bfpData = fileManager.prepareReportDataForAdapter(FullReportCardItem.BFP_RECORD);
        bpData = fileManager.prepareReportDataForAdapter(FullReportCardItem.BP_RECORD);
        calBurnData = fileManager.prepareReportDataForAdapter(FullReportCardItem.CALORIE_BURN_RECORD);

        //Inflate BMI, BMR, BFP, BP, Calburn
        linearLayout = mView.findViewById(R.id.linearLayout);
        View welcomeView = getWelcomeView();
        linearLayout.addView(welcomeView);
        Button openSnapshot = welcomeView.findViewById(R.id.openSnapshot);
        openSnapshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!snapshotOpen){
                    snapshotOpen = true;
                    inflateSnapshotViews(linearLayout);
                }
            }
        });
        return mView;
    }

    private void inflateSnapshotViews(LinearLayout layout) {
        int i = 0;
        if (bmiData != null && bmiData.length > 0) {
            layout.addView(getBmiView());
            ++i;
        }
        if (bmrData != null && bmrData.length > 0) {
            layout.addView(getBmrView());
            ++i;
        }
        if (bfpData != null && bfpData.length > 0) {
            layout.addView(getBfpView());
            ++i;
        }
        if (bpData != null && bpData.length > 0) {
            layout.addView(getBpView());
            ++i;
        }
        if (calBurnData != null && calBurnData.length > 0) {
            layout.addView(getCalBurnView());
            ++i;
        }
        if (i == 0) {
            Snackbar.make(linearLayout, "You did not save anything yet!", Snackbar.LENGTH_SHORT).setAnchorView(R.id.nav_view).show();
        }
    }

    private void refreshSnapshot() {
        linearLayout.removeAllViews();
        linearLayout.addView(getWelcomeView());
        bmiData = fileManager.prepareReportDataForAdapter(FullReportCardItem.BMI_RECORD);
        bmrData = fileManager.prepareReportDataForAdapter(FullReportCardItem.BMR_RECORD);
        bfpData = fileManager.prepareReportDataForAdapter(FullReportCardItem.BFP_RECORD);
        bpData = fileManager.prepareReportDataForAdapter(FullReportCardItem.BP_RECORD);
        calBurnData = fileManager.prepareReportDataForAdapter(FullReportCardItem.CALORIE_BURN_RECORD);
        inflateSnapshotViews(linearLayout);
    }

    private View getWelcomeView() {
        return statsInflater.inflate(R.layout.my_stats_item_preamble, constraintLayout, false);
    }

    private View getBmiView() {
        View statView = statsInflater.inflate(R.layout.my_stats_item_description, constraintLayout, false);
        TextView prevBmi = statView.findViewById(R.id.myStats_bmi_prev);
        TextView monthAvg = statView.findViewById(R.id.myStats_bmi_month);

        statView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in));
        ((TextView) statView.findViewById(R.id.myStats_ItemTitle)).setText("BMI");
        TextView valueText = statView.findViewById(R.id.myStats_ItemValue);
        valueText.setText(bmiData[0].value);
        switch (bmiData[0].extraCategory) {
            case 0:
            case 1:
            case 2:
            case 6:
            case 7:
                valueText.setTextColor(ContextCompat.getColor(getContext(), R.color.crimsonError));
                break;
            case 3:
            case 5:
                valueText.setTextColor(ContextCompat.getColor(getContext(), R.color.orangeAlert));
                break;
            case 4:
                valueText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                break;
        }
        ((TextView) statView.findViewById(R.id.myStats_date)).setText("As of: " + bmiData[0].date);
        statView.findViewById(R.id.myStats_openReport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FullReportCardActivity.class);
                intent.putExtra("type", FullReportCardItem.BMI_RECORD);
                needsRefresh = true;
                startActivity(intent);
            }
        });
        //Previous BMI
        if (bmiData.length > 1) {
            prevBmi.setText("Previous: " + bmiData[1].value);
        }else{
            prevBmi.setVisibility(View.GONE);
        }
        //Monthly BMI
        String month = bmiData[0].date.substring(3, 5);
        int i = 0;
        double avg = 0;
        while (true) {
            if (!month.equals(bmiData[i].date.substring(3, 5))) {
                break;
            }
            avg += Double.parseDouble(bmiData[i].value);
            if (i == bmiData.length - 1) {
                ++i;
                break;
            }
            ++i;
        }
        DecimalFormat twoDecimalPlaces = new DecimalFormat("##.##");
        avg /= i;
        String str = "Month Avg: " + twoDecimalPlaces.format(avg);
        monthAvg.setText(str);
        return statView;
    }

    private View getBmrView() {
        View statView = statsInflater.inflate(R.layout.my_stats_item, constraintLayout, false);
        statView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in));
        ((TextView) statView.findViewById(R.id.myStats_ItemTitle)).setText("BMR");
        TextView valueText = statView.findViewById(R.id.myStats_ItemValue);
        valueText.setText(bmrData[0].value + " kcal/day");
        ((TextView) statView.findViewById(R.id.myStats_date)).setText("As of: " + bmrData[0].date);
        statView.findViewById(R.id.myStats_openReport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FullReportCardActivity.class);
                intent.putExtra("type", FullReportCardItem.BMR_RECORD);
                needsRefresh = true;
                startActivity(intent);
            }
        });
        return statView;
    }

    //Uses BMI format with no Month text
    private View getBfpView() {
        View statView = statsInflater.inflate(R.layout.my_stats_item_description, constraintLayout, false);
        statView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in));
        ((TextView) statView.findViewById(R.id.myStats_ItemTitle)).setText("BFP%");
        TextView valueText = statView.findViewById(R.id.myStats_ItemValue);
        valueText.setText(bfpData[0].value);
        valueText.setTextSize(52);
        if (bfpData.length > 1) {
            TextView previousText = statView.findViewById(R.id.myStats_bmi_prev);
            String str = "Previous: " + bfpData[1].value;
            previousText.setText(str);
        } else {
            statView.findViewById(R.id.myStats_bmi_prev).setVisibility(View.GONE);
        }
        statView.findViewById(R.id.myStats_bmi_month).setVisibility(View.GONE);

        ((TextView) statView.findViewById(R.id.myStats_date)).setText("As of: " + bfpData[0].date);
        statView.findViewById(R.id.myStats_openReport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FullReportCardActivity.class);
                intent.putExtra("type", FullReportCardItem.BFP_RECORD);
                needsRefresh = true;
                startActivity(intent);
            }
        });
        return statView;
    }

    private View getBpView() {
        View statView = statsInflater.inflate(R.layout.my_stats_item_bp, constraintLayout, false);
        statView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in));
        ((TextView) statView.findViewById(R.id.myStats_ItemTitle)).setText("Blood Pressure");
        TextView valueText = statView.findViewById(R.id.myStats_ItemValue);
        valueText.setText(bpData[0].value);
        valueText.setTextSize(34);
        ((TextView) statView.findViewById(R.id.myStats_date)).setText("As of: " + bpData[0].date);
        statView.findViewById(R.id.myStats_openReport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FullReportCardActivity.class);
                intent.putExtra("type", FullReportCardItem.BP_RECORD);
                startActivity(intent);
                needsRefresh = true;
            }
        });
        TextView bpCatText = statView.findViewById(R.id.myStats_bpCat);
        switch (bpData[0].extraCategory) {
            case 0:
                bpCatText.setText("Low Blood Pressure");
                bpCatText.setTextColor(ContextCompat.getColor(getContext(), R.color.skyBlue));
                valueText.setTextColor(ContextCompat.getColor(getContext(), R.color.skyBlue));
                break;
            case 1:
                bpCatText.setText("Healthy Range");
                valueText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                bpCatText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                break;
            case 2:
                bpCatText.setText("Elevated Range");
                bpCatText.setTextColor(ContextCompat.getColor(getContext(), R.color.lightOrange));
                valueText.setTextColor(ContextCompat.getColor(getContext(), R.color.lightOrange));
                break;
            case 3:
                bpCatText.setText("Hypertension Stage 1");
                bpCatText.setTextColor(ContextCompat.getColor(getContext(), R.color.orangeAlert));
                valueText.setTextColor(ContextCompat.getColor(getContext(), R.color.orangeAlert));
                break;
            case 4:
                bpCatText.setText("Hypertension Stage 2");
                bpCatText.setTextColor(ContextCompat.getColor(getContext(), R.color.lightRed));
                valueText.setTextColor(ContextCompat.getColor(getContext(), R.color.lightRed));
                break;
            case 5:
                bpCatText.setText("Hypertentive Crisis");
                bpCatText.setTextColor(ContextCompat.getColor(getContext(), R.color.crimsonError));
                valueText.setTextColor(ContextCompat.getColor(getContext(), R.color.crimsonError));
                break;
        }
        return statView;
    }

    private View getCalBurnView() {
        View statView = statsInflater.inflate(R.layout.my_stats_item, constraintLayout, false);
        statView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in));
        ((TextView) statView.findViewById(R.id.myStats_ItemTitle)).setText("Monthly Calories Burnt");
        int i = 0;
        double total = 0;
        String month = calBurnData[0].date.substring(3, 5);
        while (true) {
            if (!month.equals(calBurnData[i].date.substring(3, 5))) {
                break;
            }
            total += Double.parseDouble(calBurnData[i].value);
            if (i == calBurnData.length - 1) {
                break;
            }
            ++i;
        }
        TextView valueText = statView.findViewById(R.id.myStats_ItemValue);
        String output = total + " kcal";
        valueText.setText(output);
        ((TextView) statView.findViewById(R.id.myStats_date)).setText("As of: " + calBurnData[0].date);
        statView.findViewById(R.id.myStats_openReport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FullReportCardActivity.class);
                intent.putExtra("type", FullReportCardItem.CALORIE_BURN_RECORD);
                startActivity(intent);
                needsRefresh = true;
            }
        });
        return statView;
    }

    @Override
    public void onResume() {
        if(needsRefresh){
            refreshSnapshot();
            needsRefresh = false;
            snapshotOpen = true;
        }
        snapshotOpen = false;
        super.onResume();
    }


}