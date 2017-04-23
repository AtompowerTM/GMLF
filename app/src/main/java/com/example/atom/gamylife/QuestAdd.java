package com.example.atom.gamylife;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IntegerRes;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Database.GamylifeDB;
import Database.GamylifeDbHelper;

/**
 * Created by Atom on 16/04/2017.
 */

public class QuestAdd extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    int mode, position;
    double expReward, baseExp, averageLevel;
    int difficulty = 0, urgency = 0, priority = 0;

    SQLiteDatabase db;
    ArrayList<Skill> skillEntries;
    ArrayList<Quest> questEntries;

    EditText nameText, descriptionText;

    Button doneButton, editDoneButton, enableEditsButton;
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

        //get the database
        final GamylifeDbHelper mDbHelper = new GamylifeDbHelper(this);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //get the skill and quest entries
        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        if(bundle != null) {
            mode = bundle.getInt("mode"); //mode 1 -> add quest; mode 2 -> edit quest
            questEntries = bundle.getParcelableArrayList("quests");
            skillEntries = bundle.getParcelableArrayList("skills");
            if(mode == 2) {
                position = bundle.getInt("questIndex");
            } else {
                position = -1;
            }
        }

        //_____________________________________NAME AND DESCRIPTION_________________________________
        nameText = (EditText) findViewById(R.id.editTextNameQuestAdd);
        descriptionText = (EditText) findViewById(R.id.editTextDescriptionQuestAdd);

        if(mode == 2) {
            nameText.setText(questEntries.get(position).getName());
            descriptionText.setText(questEntries.get(position).getDescription());
        }

        //_____________________________________REWARD_______________________________________________
        expRewardText = (TextView) findViewById(R.id.labelExpQuestAdd);

        if(mode == 2) {
            expRewardText.setText(Integer.toString(questEntries.get(position).getExperience()));
        }

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

        //_____________________________________SKILL SELECTOR_______________________________________
        String[] skillNames = new String [skillEntries.size()];
        skillSelector = (MultiSelectionSpinner) findViewById(R.id.spinnerSkillSelectorQuestAdd);

        if(mode == 1){
            position = -1;
        }

        int numOfSelections;
        if(position > -1) { //if there are any quests entered
            numOfSelections = questEntries.get(position).getSkillAffected().size();
        } else {
            numOfSelections = 0;
        }
        int[] selectedSkills = new int[numOfSelections];

        int k = 0;
        for (int i = 0; i < skillNames.length; i++) {
            skillNames[i] = skillEntries.get(i).getName();

            if(mode == 2) {
                for(int j = 0; j < selectedSkills.length; j++) {
                    if(skillEntries.get(i).getID() == questEntries.get(position).getSkillAffected().get(j).getID()) {
                        selectedSkills[k] = i;
                        k++;
                    }
                }
            }
        }
        skillSelector.setItems(skillNames);
        if(mode == 2) {
            skillSelector.setSelection(selectedSkills);
        }
        skillSelector.setListener(this);

        //____________________________________QUEST SELECTOR________________________________________
        String[] questNames = new String[questEntries.size() + 1];
        questNames[0] = "None";

        for (int i = 1; i < questNames.length; i++) {
            questNames[i] = questEntries.get(i-1).getName();
        }

        ArrayAdapter<String> questAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, questNames);
        parentSelector = (Spinner) findViewById(R.id.spinnerParentSelectorQuestAdd);

        questAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        parentSelector.setAdapter(questAdapter);

        if(mode == 2) {
            if(questEntries.get(position).getParentID() != -1) {
                for(int i = 0; i < questEntries.size(); i++) {
                    if(questEntries.get(position).getParentID() == questEntries.get(i).getParentID()) {
                        parentSelector.setSelection(i);
                    }
                }
            }
        }


        //____________________________________DURATION______________________________________________
        hourPicker = (NumberPicker) findViewById(R.id.numberPickerHoursQuestAdd);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        minutePicker = (NumberPicker) findViewById(R.id.numberPickerMinutesQuestAdd);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);

        if(mode == 2) {
            int curDuration = questEntries.get(position).getDuration();
            hourPicker.setValue(curDuration / 60);
            minutePicker.setValue(curDuration % 60);
        }

        //____________________________________SCHEDULE______________________________________________
        scheduleDate = (TextView) findViewById(R.id.labelScheduledDateQuestAdd);
        scheduleDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(listener)
                        .setMinDate(new Date())
                        .setInitialDate(new Date())
                        .build()
                        .show();
            }
        });

        if(mode == 2) {
            try {
                scheduleDate.setText(sdf.format(questEntries.get(position).getScheduled()));
            } catch (Exception e) {
            }

        }

        if(mode == 2) {
            disableAllElements();
        }

        //_____________________________________BUTTON_______________________________________________

        if(mode == 1) {
            editDoneButton = (Button) findViewById(R.id.buttonEditDoneQuestAdd);
            editDoneButton.setEnabled(false);
            editDoneButton.setVisibility(View.INVISIBLE);
            enableEditsButton = (Button) findViewById(R.id.buttonEnableEditsQuestAdd);
            enableEditsButton.setEnabled(false);
            enableEditsButton.setVisibility(View.INVISIBLE);
            doneButton = (Button) findViewById(R.id.buttonDoneQuestAdd);
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    List<Integer> selectedSkills = skillSelector.getSelectedIndices();
                    if (selectedSkills.size() > 0 && !nameText.getText().toString().equals("") &&
                            (hourPicker.getValue() != 0 || minutePicker.getValue() != 0)) {

                        String questName = nameText.getText().toString();
                        String questDescription = descriptionText.getText().toString();

                        ArrayList<Skill> affectedSkills = new ArrayList<Skill>();

                        for (int i = 0; i < selectedSkills.size(); i++) {
                            affectedSkills.add(skillEntries.get(selectedSkills.get(i)));
                            Log.d("affected", skillEntries.get(selectedSkills.get(i)).getName());
                        }

                        calculateReward();
                        int experience = (int) expReward;
                        long parentQuest;
                        if ((parentSelector.getSelectedItemId() - 1) != -1) {
                            parentQuest = questEntries.get(
                                    (int) parentSelector.getSelectedItemId() - 1).getID(); //-1 for no
                        } else {
                            parentQuest = -1; // no parent
                        }

                        int duration = hourPicker.getValue() * 60 + minutePicker.getValue();
                        String scheduledForText = scheduleDate.getText().toString();
                        Date scheduledFor;
                        try {
                            scheduledFor = sdf.parse(scheduleDate.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            scheduledFor = null;
                            //Toast.makeText(v.getContext(), "Error parsing date.",
                            //        Toast.LENGTH_SHORT).show();
                        }

                        boolean completed = false;

                        ContentValues values = new ContentValues();
                        values.put(GamylifeDB.GamylifeQuestEntry.COLUMN_NAME_NAME, questName);
                        values.put(GamylifeDB.GamylifeQuestEntry.COLUMN_NAME_DESCRIPTION, questDescription);
                        values.put(GamylifeDB.GamylifeQuestEntry.COLUMN_NAME_EXPERIENCE, experience);
                        values.put(GamylifeDB.GamylifeQuestEntry.COLUMN_NAME_DURATION, duration);
                        values.put(GamylifeDB.GamylifeQuestEntry.COLUMN_NAME_SCHEDULED, scheduledForText);
                        values.put(GamylifeDB.GamylifeQuestEntry.COLUMN_NAME_PARENT, parentQuest);
                        values.put(GamylifeDB.GamylifeQuestEntry.COLUMN_NAME_COMPLETED, 0);

                        long questID = db.insert(GamylifeDB.GamylifeQuestEntry.TABLE_NAME, null, values);

                        ContentValues valuesQuestSkill = new ContentValues();
                        for (int i = 0; i < affectedSkills.size(); i++) {
                            valuesQuestSkill.put(GamylifeDB.GamylifeQuestSkillEntry.COLUMN_NAME_QUEST_ID, questID);
                            valuesQuestSkill.put(GamylifeDB.GamylifeQuestSkillEntry.COLUMN_NAME_SKILL_ID, affectedSkills.get(i).getID());
                            db.insert(GamylifeDB.GamylifeQuestSkillEntry.TABLE_NAME, null, valuesQuestSkill);
                            valuesQuestSkill.clear();
                        }

                        Quest newQuest = new Quest(questID, questName, questDescription, experience,
                                affectedSkills, duration, scheduledFor, parentQuest, completed);

                        Intent returnIntent = new Intent();

                        Bundle bundle = new Bundle();
                        bundle.putParcelable("quest", newQuest);
                        returnIntent.putExtra("bundle", bundle);

                        setResult(Activity.RESULT_OK, returnIntent);

                        //Finish this activity and return to QuestFragment
                        finish();

                    } else {
                        if(selectedSkills.size() <= 0) {
                            Toast.makeText(v.getContext(), "Please choose at least one skill.",
                                    Toast.LENGTH_SHORT).show();
                        } else if(nameText.getText().toString().equals("")) {
                            Toast.makeText(v.getContext(), "Please enter a skill name.",
                                    Toast.LENGTH_SHORT).show();
                        } else if((hourPicker.getValue() == 0 && minutePicker.getValue() == 0)) {
                            Toast.makeText(v.getContext(), "Duration cannot be 0h 0m.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else if(mode == 2) {
            doneButton = (Button) findViewById(R.id.buttonDoneQuestAdd);
            doneButton.setEnabled(false);
            doneButton.setVisibility(View.INVISIBLE);

            editDoneButton = (Button) findViewById(R.id.buttonEditDoneQuestAdd);
            editDoneButton.setEnabled(false);
            editDoneButton.setVisibility(View.INVISIBLE);
            editDoneButton.setText("Export DB");
            editDoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Exporting database to SD card.", Toast.LENGTH_LONG).show();
                    exportDatabse("GamylifeDB.db");
                }
            });

            enableEditsButton = (Button) findViewById(R.id.buttonEnableEditsQuestAdd);
            enableEditsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Editing not yet implemented.", Toast.LENGTH_LONG).show();

                    enableEditsButton.setVisibility(View.INVISIBLE);
                    enableEditsButton.setEnabled(false);

                    editDoneButton.setVisibility(View.VISIBLE);
                    editDoneButton.setEnabled(true);

                }
            });
        }
    }

    public void exportDatabse(String databaseName) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//"+getPackageName()+"//databases//"+databaseName+"";
                String backupDBPath = "GamylifeBackup.db";
                Log.d("SD CARD: ", Environment.getExternalStorageDirectory().getAbsolutePath());
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Log.d("SuccessfulExport", "Done");
                }
            }
        } catch (Exception e) {
            Log.d("FailExport", "Not Done");
        }
    }

    private void disableAllElements() {
        nameText.setFocusable(false);
        nameText.setEnabled(false);
        descriptionText.setFocusable(false);
        descriptionText.setEnabled(false);
        skillSelector.setEnabled(false);
        difficultySlider.setEnabled(false);
        urgencySlider.setEnabled(false);
        prioritySlider.setEnabled(false);
        parentSelector.setEnabled(false);
        hourPicker.setEnabled(false);
        hourPicker.setFocusable(false);
        hourPicker.setDescendantFocusability(hourPicker.FOCUS_BLOCK_DESCENDANTS);
        minutePicker.setEnabled(false);
        minutePicker.setFocusable(false);
        minutePicker.setDescendantFocusability(hourPicker.FOCUS_BLOCK_DESCENDANTS);
        scheduleDate.setEnabled(false);

        //Undo the block descendants with these:
        //hourPicker.setDescendantFocusability(hourPicker.FOCUS_AFTER_DESCENDANTS);
        //minutePicker.setDescendantFocusability(hourPicker.FOCUS_AFTER_DESCENDANTS);
    }

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.SECOND, 0);
            scheduleDate.setText(sdf.format(calendar.getTime()).toString());
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

        List<Integer> selectedSkills = skillSelector.getSelectedIndices();
        averageLevel = 0;
        for (int i = 0; i < selectedSkills.size(); i++) {
            averageLevel += skillEntries.get(selectedSkills.get(i)).getLevel();
        }

        averageLevel = averageLevel / selectedSkills.size();
        Log.d("avgLvl", Double.toString(averageLevel));
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }
}
