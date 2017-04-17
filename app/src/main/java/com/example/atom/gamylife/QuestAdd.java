package com.example.atom.gamylife;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.guna.libmultispinner.MultiSelectionSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Atom on 16/04/2017.
 */

public class QuestAdd extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    double expReward, baseExp, averageLevel;
    int difficulty = 0, urgency = 0, priority = 0;
    ArrayList<Quest> questEntries;


    EditText nameText;

    Button doneButton;
    TextView scheduleDate;
    NumberPicker hourPicker;
    NumberPicker minutePicker;
    MultiSelectionSpinner skillSelector;
    Spinner parentSelector;
    SeekBar difficultySlider, urgencySlider, prioritySlider;
    TextView difficultyText, urgencyText, priorityText;
    TextView difficultyPercent, urgencyPercent, priorityPercent;
    TextView expRewardText;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_add);

        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        if(bundle != null) {
            questEntries = bundle.getParcelableArrayList("quests");
        }

        nameText = (EditText) findViewById(R.id.editTextNameQuestAdd);

        nameText.setText(questEntries.get(0).getName());

        //_____________________________________BUTTON_______________________________________________
        doneButton = (Button) findViewById(R.id.buttonDoneQuestAdd);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("parent", Long.toString(parentSelector.getSelectedItemId()));

                List<Integer> selectedSkills = skillSelector.getSelectedIndices();
                for (int i = 0; i < selectedSkills.size(); i++)
                    Log.d("Skills" + i, Integer.toString(selectedSkills.get(i)));
            }
        });



        //_____________________________________REWARD_______________________________________________
        expRewardText = (TextView) findViewById(R.id.labelExpQuestAdd);

        //_____________________________________SLIDERS______________________________________________

        //Difficulty--------------------------------
        difficultySlider = (SeekBar) findViewById(R.id.seekBarDifficultyQuestAdd);
        difficultyText = (TextView) findViewById(R.id.labelDifficultyTextQuestAdd);
        difficultyPercent = (TextView) findViewById(R.id.labelDifficultyPercentQuestAdd);

        difficultySlider.setMax(100);
        difficultySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                difficultyPercent.setText(Integer.toString(progress) + "%");
                if (progress == 0) difficultyText.setText("None");
                else if (progress < 25) difficultyText.setText("Very Easy");
                else if (progress < 50) difficultyText.setText("Easy");
                else if (progress < 75) difficultyText.setText("Moderate");
                else if (progress < 90) difficultyText.setText("Hard");
                else difficultyText.setText("Extreme");

                difficulty = progress;
                calculateReward();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Urgency------------------------------------
        urgencySlider = (SeekBar) findViewById(R.id.seekBarUrgencyQuestAdd);
        urgencyText = (TextView) findViewById(R.id.labelUrgencyTextQuestAdd);
        urgencyPercent = (TextView) findViewById(R.id.labelUrgencyPercentQuestAdd);

        urgencySlider.setMax(100);
        urgencySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser){

                urgencyPercent.setText(Integer.toString(progress) + "%");
                if (progress == 0) urgencyText.setText("None");
                else if (progress < 25) urgencyText.setText("Low");
                else if (progress < 50) urgencyText.setText("Mild");
                else if (progress < 75) urgencyText.setText("Moderate");
                else if (progress < 90) urgencyText.setText("Severe");
                else urgencyText.setText("Now!");

                urgency = progress;
                calculateReward();
            }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
        });

        //Priority------------------------------------
        prioritySlider = (SeekBar) findViewById(R.id.seekBarPriorityQuestAdd);
        priorityText = (TextView) findViewById(R.id.labelPriorityTextQuestAdd);
        priorityPercent = (TextView) findViewById(R.id.labelPriorityPercentQuestAdd);

        prioritySlider.setMax(100);
        prioritySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser){

                priorityPercent.setText(Integer.toString(progress) + "%");
                if (progress == 0) priorityText.setText("None");
                else if (progress < 25) priorityText.setText("Very Low");
                else if (progress < 50) priorityText.setText("Low");
                else if (progress < 75) priorityText.setText("Moderate");
                else if (progress < 90) priorityText.setText("Severe");
                else priorityText.setText("Critical");

                priority = progress;
                calculateReward();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //_____________________________________SKILL SELECTOR TBC_______________________________________
        String[] array = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eight2", "nine2", "ten2"};
        skillSelector = (MultiSelectionSpinner) findViewById(R.id.spinnerSkillSelectorQuestAdd);

        skillSelector.setItems(array);
        skillSelector.setListener(this);

        //____________________________________QUEST SELECTOR TBC________________________________________
        String[] questNames = new String[questEntries.size() + 1];
        questNames[0] = "None";
        for (int i = 1; i < questNames.length; i++) {
            questNames[i] = questEntries.get(i-1).getName();
        }
        //String[] array2 = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eight2", "nine2", "ten2", "seven", "eight", "nine", "ten", "eight2", "nine2", "ten2"};
        ArrayAdapter<String> questAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, questNames);
        parentSelector = (Spinner) findViewById(R.id.spinnerParentSelectorQuestAdd);

        questAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        parentSelector.setAdapter(questAdapter);

        //____________________________________DURATION______________________________________________
        hourPicker = (NumberPicker) findViewById(R.id.numberPickerHoursQuestAdd);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        minutePicker = (NumberPicker) findViewById(R.id.numberPickerMinutesQuestAdd);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);


        //____________________________________SCHEDULE______________________________________________
        scheduleDate = (TextView) findViewById(R.id.labelScheduledDateQuestAdd);
        scheduleDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date())
                        .build()
                        .show();
            }
        });

    }

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) { //!!!!!!!!!!!!!!

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.SECOND, 0);
            scheduleDate.setText(dateFormat.format(calendar.getTime()).toString());
        }

        @Override
        public void onDateTimeCancel() {

            scheduleDate.setText("");
        }
    };

    private void calculateReward() {

        setAverageLevel();
        calculateBaseExp();
        expReward = Math.floor((baseExp*averageLevel) - (0.75*(baseExp*averageLevel)*(1 - ((100 - averageLevel + 1) / 100))));
        expRewardText.setText(Integer.toString((int)expReward));
    }

    private void calculateBaseExp() {
        //at all sliders maxed, baseExp = 1000;
        baseExp = (difficulty * 10) * (urgency + priority) / 200;
    }

    private void setAverageLevel() {

        averageLevel = 2;

        /*
        List<Integer> selectedSkills = skillSelector.getSelectedIndices();
        for (int i = 0; i < selectedSkills.size(); i++) {

        }
        */
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }
}
