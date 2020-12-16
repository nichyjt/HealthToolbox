# HealthToolbox

## Project Overview

A minimalistic, lightweight and function-oriented health & fitness application built with Android.
This project is meant as a personal learning exercise to:  
1. Get familiarised with the Android framework and APIs.
2. Practice OOP (have not formally started University yet).
3. Learn to use various APIs, including GSON and JSON which are extensively used in this project.

## Main Features
### Calculators
1. Body Mass Index
2. Body Fat Percentage
    - Based on 
3. Basal Metabolic Rate
    - Based on the more recent Mifflin St-Jeor Formula.
4. Calorie Burn
    - Based on 2011 Compendium of Physical Activities.
5. One Rep Max
    - Various formulae included to better provide estimates for 1 Rep Max Calculations.

### Various Health/Fitness Tools
1. Blood Pressure Tracker
    - Checks your Blood Pressure (BP) input to the BP chart.
    - Chart references based on the American Heart Association's guidelines as of 161220.
2. Interval Timer
    - Customisable and save-able intervals.
    - Comes with pre-set 'TABATA' option.
3. Workout Planner
    - Customisable and saveable.
    - Options available to...  
        a. Create a timer for an exercise. (e.g. Planks)  
        b. Disable weights used. (e.g. Bodyweight exercises s/a Pullups)  
        c. Disable number of reps to allow foor 'To Failure' exercises.  
4. VO2 Max Test Calculator
    - 4 Variants with different formulas and metrics.
5. Manual Heart Rate Timer

### Data Persistence
Information from the app user can be saved in the app via JSON files.
The JSON files are primarily accessed and modified with help from GSON.
1. 'Snapshot'
    - A simple compilation of various data saved.
    - Highlights the latest information or key information. (e.g. Monthly average BMI, latest Blood Pressure reading, etc.)
2. 'Report Card'
    - Full display of the health data being saved.
    - Organised by month.

CAA: 161220
