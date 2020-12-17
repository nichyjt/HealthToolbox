package com.gmail.dev.nichyek.HealthToolbox;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class OneRepMaxValues extends Fragment {

    boolean isRepChosen = true;
    Double customValue = null;
    double vOneRepMax;
    TextView nscaPickerText;
    TextView nscaOutputLoad;
    EditText vCustomPerc;
    RadioGroup rg;
    NumberPicker nscaPicker;

    ClipboardManager clipboardManager;
    ImageView copy95;
    ImageView copy90;
    ImageView copy85;
    ImageView copy75;
    ImageView copy70;
    ImageView copyCustom;
    CardView mainCard;

    String[] repVal = {"1","2","3","4","5","6","7","8","9","10","12"};
    String[] weightPickerVal = {"100", "95", "93", "90", "87", "85","83","80","77","75","70"};
    double[] defaultPercVal = {95,90,80,75,70};

    public OneRepMaxValues() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView = inflater.inflate(R.layout.fragment_one_rep_max_values, container, false);
        clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        vOneRepMax = getArguments().getDouble("getORM");

        for(int i=0; i<defaultPercVal.length; ++i){
            defaultPercVal[i] = findXpercent1RM(defaultPercVal[i],vOneRepMax);
        }
        //MAIN UI AND LOGIC
        CheckBox nscaHelper = mView.findViewById(R.id.oneRM_Values_nscaHelperOption);
        final TextView mainOutput = mView.findViewById(R.id.oneRM_Values_outputValue);
        mainOutput.setText(String.valueOf(vOneRepMax));
        TextView v95perc = mView.findViewById(R.id.oneRM_Values_95pVal);
        v95perc.setText(String.valueOf(defaultPercVal[0]));
        TextView v90perc = mView.findViewById(R.id.oneRM_Values_90pVal);
        v90perc.setText(String.valueOf(defaultPercVal[1]));
        TextView v80perc = mView.findViewById(R.id.oneRM_Values_80pVal);
        v80perc.setText(String.valueOf(defaultPercVal[2]));
        TextView v75perc = mView.findViewById(R.id.oneRM_Values_75pVal);
        v75perc.setText(String.valueOf(defaultPercVal[3]));
        TextView v70perc = mView.findViewById(R.id.oneRM_Values_70pVal);
        v70perc.setText(String.valueOf(defaultPercVal[4]));
        vCustomPerc = mView.findViewById(R.id.oneRM_Values_customPercent);
        final TextView vCustomVal = mView.findViewById(R.id.oneRM_Values_customVal);
        mainCard = mView.findViewById(R.id.oneRM_values_ContainerCard);
        final CardView nscaCard = mView.findViewById(R.id.oneRM_Values_nscaContainerCard);

        //Listener(s) to handle custom percentage events
        mView.findViewById(R.id.oneRM_Values_FrameLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vCustomPerc.getText().toString().trim().length()!=0){
                    //Calculate vCustomVal, format the input text
                    Double interm = Double.parseDouble(vCustomPerc.getText().toString());
                    interm = findXpercent1RM(interm, vOneRepMax);
                    vCustomVal.setText(interm.toString());
                    customValue = interm;
                    copyCustom.setOnClickListener(setCopyClickListener(customValue));
                }
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                if(getActivity().getCurrentFocus()!=null){
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }
            }
        });
        vCustomPerc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    mView.performClick();
                }
                return false;
            }
        });

        copy95 = mView.findViewById(R.id.oneRM_Values_95copy);
        copy90 = mView.findViewById(R.id.oneRM_Values_90copy);
        copy85 = mView.findViewById(R.id.oneRM_Values_80copy);
        copy75 = mView.findViewById(R.id.oneRM_Values_75copy);
        copy70 = mView.findViewById(R.id.oneRM_Values_70copy);
        copyCustom = mView.findViewById(R.id.oneRM_Values_customCopy);
        copy95.setOnClickListener(setCopyClickListener(defaultPercVal[0]));
        copy90.setOnClickListener(setCopyClickListener(defaultPercVal[1]));
        copy85.setOnClickListener(setCopyClickListener(defaultPercVal[2]));
        copy75.setOnClickListener(setCopyClickListener(defaultPercVal[3]));
        copy70.setOnClickListener(setCopyClickListener(defaultPercVal[4]));
        copyCustom.setOnClickListener(setCopyClickListener(customValue));

        //NSCA UI AND LOGIC
        rg = mView.findViewById(R.id.oneRM_Values_nscaOptions);
        nscaCard.setVisibility(View.INVISIBLE);
        nscaPicker = mView.findViewById(R.id.oneRM_Values_nscaValuePicker);
        nscaPickerText = mView.findViewById(R.id.oneRM_Values_nscaPickerText);
        nscaPicker.setDisplayedValues(repVal);
        nscaPicker.setMinValue(0);
        nscaPicker.setMaxValue(10);
        nscaOutputLoad = mView.findViewById(R.id.oneRM_Values_nscaLoadOutput);
        nscaPickerText.setText("Reps");
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedBtn) {
                if(checkedBtn == R.id.oneRM_Values_nscaRepOption){
                    nscaPicker.setDisplayedValues(repVal);
                    nscaPickerText.setText("Reps");
                    isRepChosen = true;
                }else{
                    nscaPicker.setDisplayedValues(weightPickerVal);
                    nscaPickerText.setText("% 1RM");
                    isRepChosen = false;
                }

            }
        });

        //Card Visibility logic
        final Animation slideIn = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in);
        final Animation slideOut = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out);
        nscaHelper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    nscaCard.setVisibility(View.VISIBLE);
                    nscaCard.startAnimation(slideIn);
                    mainCard.startAnimation(slideOut);
                    mainCard.setVisibility(View.INVISIBLE);
                }else{
                    mainCard.setVisibility(View.VISIBLE);
                    mainCard.startAnimation(slideIn);
                    nscaCard.startAnimation(slideOut);
                    nscaCard.setVisibility(View.INVISIBLE);
                }
            }
        });

        //NSCA Picker Output logic
        nscaOutputLoad.setText(getRecommendedLoadString(0));
        nscaPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                String outputText;
                outputText = getRecommendedLoadString(i1);
                nscaOutputLoad.setText(outputText);
            }
        });

        return mView;
    }

    //Calculate and estimate percentages logic
    private double findXpercent1RM(double percent, double oneRM){
        double vPercent = (double) percent/100;
        double ans = vPercent*oneRM;
        DecimalFormat oneDP = new DecimalFormat("#.#");
        ans = Double.parseDouble(oneDP.format(ans));
        return ans;
    }


    private String getRecommendedLoadString(int pickerIndex){
        String reps = repVal[pickerIndex];
        double percent = Double.parseDouble(weightPickerVal[pickerIndex]);
        double load = findXpercent1RM(percent, vOneRepMax);


        return String.format(Locale.getDefault(), "%s Reps at %s kg/lbs\n(%s%% 1RM)", reps, load, percent);
    }

    private View.OnClickListener setCopyClickListener(@Nullable final Double valueToCopy){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(valueToCopy==null){
                    Snackbar.make(mainCard, "Did you calculate a value yet?", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                ClipData data = ClipData.newPlainText("RepMaxValue", String.valueOf(valueToCopy));
                clipboardManager.setPrimaryClip(data);
                Snackbar.make(mainCard, valueToCopy + " Copied to clipboard", Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    public void resetOneRepMaxValues(){
        double[] reset = new double[]{95,90,80,75,70};
        System.arraycopy(reset, 0, defaultPercVal, 0, 5);
        vCustomPerc.setText("");
    }

}