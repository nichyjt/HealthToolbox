package com.gmail.dev.nichyek.HealthToolbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {


    FragmentManager fm = getSupportFragmentManager();
    ActionBar actionBar;
    Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create a navigation bar on the bottom, referencing the nav_view XML resource
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavBarListener);
        switchToMyStats();

        //actionBar = getSupportActionBar();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    } //end of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menu_sources){
            Intent intent = new Intent(getApplicationContext(), Menu_ResearchSources.class);
            startActivity(intent);
        }else if (item.getItemId() ==  R.id.menu_credits){
            Intent intent = new Intent(getApplicationContext(), Menu_CreditsSources.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.menu_data_usage){
            Intent intent = new Intent(getApplicationContext(), Menu_DataUsage.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    //create a listener for bottomNav, to do stuff when an item is clicked
    BottomNavigationView.OnNavigationItemSelectedListener bottomNavBarListener = new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                //The following cases dictate what happens when each menu item is clicked
                case R.id.myStats:
                    //Run some code that relates to myStats fragments
                    switchToMyStats();
                    break;
                case R.id.healthTools:
                    //Run some code that relates to healthTools fragments
                    switchToHealthToolsFragment();
                    break;

                case R.id.workoutTools:
                    //Run some code that relates to workoutTools fragments
                    switchToWorkoutToolsFragment();
                    break;
            }
            return true;
        }
    };


        Fragment fakeFragment = new DebugFragment();
        Fragment healthToolsFragment = new HealthToolFragment();
        Fragment workoutToolsFragment = new WorkoutToolsFragment();
        Fragment myStatsFragment = new MyStatisticsFragment();


    //switch fragments when its respective menu item is clicked
    private void switchToFakeFragment(){
        fm.beginTransaction().replace(R.id.mainActivityFrame, fakeFragment).commit();
    }
    private void switchToHealthToolsFragment(){
        fm.beginTransaction().replace(R.id.mainActivityFrame, healthToolsFragment).commit();
    }
    private void switchToWorkoutToolsFragment(){
        fm.beginTransaction().replace(R.id.mainActivityFrame, workoutToolsFragment).commit();
    }
    private void switchToMyStats(){
        fm.beginTransaction().replace(R.id.mainActivityFrame, myStatsFragment, "MY_STATS_FRAGMENT").commit();
    }


}





