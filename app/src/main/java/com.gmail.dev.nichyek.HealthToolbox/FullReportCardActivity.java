package com.gmail.nichyekdev.healthtoolbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class FullReportCardActivity extends AppCompatActivity {

    //This class does the following,
    //1. Load JSON data as required
    //2. Generate a listview representation of the data
    //3. Each cardview contains the month's recordings, and the details inside

    /*
    For reference
    public static int BMI_RECORD = 0;
    public static int BMR_RECORD = 1;
    public static int BFP_RECORD = 2;
    public static int BP_RECORD = 3;
     */

    int type;
    FullReportCardFileManager dataMgr;
    ArrayList<String> monthString;
    ArrayList<Integer> monthIndexes;
    FullReportCardAdapter adapter;
    ListView listView;
    FragmentManager fm = getSupportFragmentManager();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_report_card);
        setTitle("Full Report");
        toolbar =  findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        dataMgr =  new FullReportCardFileManager(getApplicationContext());
        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);

        FullReportCardItem[] data = dataMgr.prepareReportDataForAdapter(type);
        if(data==null || data.length==0){
            ((TextView)findViewById(R.id.FullReportCardErrorText)).setText("Hmm, looks like you haven't saved any records yet.");
        }else{
            monthString = dataMgr.getMonthStrings();
            monthIndexes = dataMgr.getNewMonthIndexes();
            adapter = new FullReportCardAdapter(getApplicationContext(), R.layout.report_card_item, monthString, monthIndexes, data);
            adapter.parentActivity = this;
            listView = findViewById(R.id.FullReportCardListView);
            listView.setAdapter(adapter);
        }

    }

    public void redrawReportCard(){
        finish();
        startActivity(getIntent());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }else{
            super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}