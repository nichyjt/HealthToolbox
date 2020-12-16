package com.gmail.dev.nichyek.HealthToolbox;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Menu_DataUsage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__data_usage);
        setTitle("Data Usage");
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        TextView textView = findViewById(R.id.textView);
        String string = "About your saved data: " +
                "\n\n1. All saved health data (e.g. BMI, BFP etc) is stored locally on your phone, entirely offline." +
                "\n\n2. Your saved data is inaccessible to other applications." +
                "\n\n3. Your saved data will be deleted when this app is uninstalled.";

        textView.setText(string);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}