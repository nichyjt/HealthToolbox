# Health Toolbox

## Project Overview

A minimalistic, lightweight and function-oriented health & fitness application built with Android.
This project is meant as a personal learning exercise to:  
1. Get familiarised with the Android framework.
2. Practice OOP.
3. Learn to use various APIs, such as GSON and JSON which are used in this project.
4. Most importantly, create something I would actually use.

## Main Features
### Calculators
1. Body Mass Index
2. Body Fat Percentage
3. Basal Metabolic Rate
    - Based on the Mifflin St-Jeor Formula.
4. Calorie Burn
    - Based on 2011 Compendium of Physical Activities.
5. One Rep Max
    - Various formulae included to provide better 1 Rep Max estimations based on the exercise.
    - Comes with percentage tools to assist with whatever training requirements one may have.

### Various Health/Fitness Tools
1. Blood Pressure Tracker
    - Checks your Blood Pressure (BP) input to the BP chart.
    - Chart references based on the American Heart Association's guidelines as of 161220.
2. Interval Timer
    - Customisable and save-able intervals.
    - Comes with pre-set 'TABATA' option.
3. Workout Planner
    - Planning and keep track of workouts.
    - Customisable names, reps and weights used.
    - Extra options available to...  
        a. Create a timer for an exercise. (e.g. Planks)  
        b. Disable weights (For calisthenics/bodyweight exercises s/a Pullups)  
        c. Change number of reps to 'Do To Failure'.
4. VO2 Max Test Calculator
    - 4 Tests with different formulas and metrics to estimate VO2 Max.
5. Manual Heart Rate Timer

### Data Persistence
Information from the app user can be saved in the app via JSON files.
The JSON files are primarily parsed and modified with help from GSON.
1. 'Snapshot'
    - A simple compilation of various data saved.
    - Highlights the latest saved information and key information. (e.g. Monthly average BMI, latest Blood Pressure reading, etc.)
2. 'Report Card'
    - Full display of the health data being saved.
    - Organised by month.

CAA: 181220
