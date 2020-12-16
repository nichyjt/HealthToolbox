package com.gmail.dev.nichyek.HealthToolbox;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Menu_ResearchSources extends AppCompatActivity {

    TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Research Sources");
        setContentView(R.layout.activity_information__sources);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        infoText = findViewById(R.id.informationText);

        //VO2 Details
        Spannable vo2Title = new SpannableString("VO2 Max Calculator");
        vo2Title.setSpan(new StyleSpan(Typeface.BOLD), 0, vo2Title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Spannable vo2Details = new SpannableString(
                "\n\nCooper 2.4km Test formula adapted from: \n" +
                        "\nBURGER, S.C. et al. (1990) Assessment of the 2.4 km run as a predictor of aerobic capacity. S Afr Med J. 15 (78), p. 327-329." +
                        "\n\nACSM 2.4km Test formula adapted from: \n" +
                        "\nACSM's Complete Guide to Fitness & Health (2011)" +
                        "\n\nCooper 12 minute Test formula adapted from: \n" +
                        "\nCooper KH. A Means of Assessing Maximal Oxygen Intake: Correlation Between Field and Treadmill Testing. JAMA. 1968;203(3):201–204." +
                        "\n\nBeep Test normative data adapted from: \n" +
                        "\nLEGER, L.A. and LAMBERT, J. (1982) A maximal multistage 20m shuttle run test to predict VO2max. European Journal of Applied Physiology, 49, p. 1-5\n\n"
        );
        //One Rep Max Details
        Spannable ormTitle = new SpannableString("One Rep Max Calculator");
        ormTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, ormTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Spannable ormDetails = new SpannableString(
                "\n\nBryzki Equation adapted from:\n" +
                        "\nBrzycki, Matt (1998). A Practical Approach To Strength Training. McGraw-Hill. ISBN 978-1-57028-018-4." +
                        "\n\nEpley Equation adapted from:\n" +
                        "\nEpley, Boyd (1985). \"Poundage Chart\". Boyd Epley Workout. Lincoln, NE: Body Enterprises. p. 86" +
                        "\n\nMayhew Equation adapted from:\n" +
                        "\nMAYHEW, J.L., T. BALL, M. ARNOLD, AND J. BOWEN. Relative muscular endurance performance as a predictor of bench press strength" +
                        " in college men and women. J. Appl. Sport Sci. Res. 6: 200–206. 1992." +
                        "\n\nWathen Equation adapted from:\n" +
                        "\nWathen, D. Load assignment. In: Essentials of Strength Training and Conditioning. T.R. Baechle (ed.). Champaign, IL: Human Kinetics, pp: 435-439, 1994." +
                        "\n\nO'Connor Equation adapted from:\n" +
                        "\nO’CONNOR, B., J. SIMMONS, AND P. O’SHEA. Weight Training Today. St. Paul, MN: West Publishing, 1989." +
                        "\n\nEquation Recommendations adapted from findings in:\n" +
                        "\n1. LeSuer Dale A.; McCormick, James H.; Mayhew, Jerry L.; Wasserstein, Ronald L.; Arnold, Michael D. Journal of Strength and Conditioning Research: November 1997 - p 211-213" +
                        "\n\n2. Reynolds JM, Gordon TJ, Robergs RA. Prediction of one repetition maximum strength from multiple repetition maximum testing and anthropometry. J Strength Cond Res. 2006;20(3):584-592. doi:10.1519/R-15304.1" +
                        "\n\n3. DiStasio, Thomas J. \"Validation of the Brzycki and Epley Equations for the 1 Repetition Maximum Back Squat Test in Division I College Football Players.\" (Jan 2014).\n\n"
        );
        //Calorie Burn Details
        Spannable calorieBurnTitle = new SpannableString("Calorie Burn Calculator");
        calorieBurnTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, calorieBurnTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Spannable calorieBurnDetails = new SpannableString(
                "\n\nMET Values adapted from:\n" +
                        "\n2011 Compendium of Physical Activities\n\n"
        );
        //BFP Details
        Spannable bfpTitle = new SpannableString("Body Fat %");
        bfpTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, bfpTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Spannable bfpDetails = new SpannableString(
          "\n\nBody Fat Percentage Formula adapted from:\n" +
                  "\nHistory of the U.S. Navy Body Composition program. Peterson DD. Mil Med. 2015;180:91–96.\n\n"
        );

        Spannable bmrTitle = new SpannableString("Basal Metabolic Rate");
        bmrTitle.setSpan(new StyleSpan(Typeface.BOLD), 00, bmrTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Spannable bmrDetails =  new SpannableString(
                "\n\nBasal Metabolic Rate equation adapted from:\n" +
                        "\nMifflin MD, St Jeor ST, Hill LA, Scott BJ, Daugherty SA, and Koh YO, (1990)." +
                        "A new predictive equation for resting energy expenditure in healthy individuals." +
                        "The American Journal of Clinical Nutrition, 51(2):241-247.\n\n"
        );

        infoText.setText(vo2Title);
        infoText.append(vo2Details);
        infoText.append(ormTitle);
        infoText.append(ormDetails);
        infoText.append(calorieBurnTitle);
        infoText.append(calorieBurnDetails);
        infoText.append(bfpTitle);
        infoText.append(bfpDetails);
        infoText.append(bmrTitle);
        infoText.append(bmrDetails);
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
