package com.gmail.nichyekdev.healthtoolbox;

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

public class Menu_CreditsSources extends AppCompatActivity {

    TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Attributions");
        setContentView(R.layout.activity_menu__credits_sources);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        Spannable imgTitle =  new SpannableString("\nVector Images");
        imgTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, imgTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Spannable imgDetails = new SpannableString(
                "\n\nIcons made by and/or adapted from these artists at www.flaticon.com:\n\n" +
                        "1. Freepik" +
                        "\nhttps://www.flaticon.com/authors/freepik\n" +
                        "2. Pixel Perfect" +
                        "\nhttps://www.flaticon.com/authors/pixel-perfect\n" +
                        "3. bqlqn" +
                        "\nhttps://www.flaticon.com/authors/bqlqn\n" +
                        "4. Pixelmeetup" +
                        "\nhttps://www.flaticon.com/authors/pixelmeetup\n\n"
        );


        infoText = findViewById(R.id.informationText);
        Spannable sfxTitle = new SpannableString("Sound Effects");
        sfxTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, sfxTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Spannable sfxDetails = new SpannableString(
                "\n\nSound Effects have been made by and/or adapted from these artists:\n\n" +
                        "1. Beep Ping\n" +
                        "\nhttps://freesound.org/people/qubodup/sounds/238995" +
                        "\nby qubodup https://freesound.org/people/qubodup" +
                        "\nunder CC-BY 3.0 License\n\n" +
                        "2. Boxing Bell 1\n" +
                        "\nhttps://freesound.org/s/66952 " +
                        "\nby Benboncan https://freesound.org/people/Benboncan" +
                        "\nunder CC-BY 3.0 License\n" +
                        "\nLicense: http://creativecommons.org/licenses/by/3.0/legalcode\n"
        );

        infoText.setText(imgTitle);
        infoText.append(imgDetails);
        infoText.append(sfxTitle);
        infoText.append(sfxDetails);
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