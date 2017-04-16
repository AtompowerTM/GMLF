package com.example.atom.gamylife;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.guna.libmultispinner.MultiSelectionSpinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Atom on 16/04/2017.
 */

public class QuestAdd extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    Button doneButton;
    TextView scheduleDate;
    NumberPicker hourPicker;
    NumberPicker minutePicker;
    Spinner parentSelector;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_add);

        //_____________________________________BUTTON_______________________________________________
        doneButton = (Button) findViewById(R.id.buttonDoneQuestAdd);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("parent", Long.toString(parentSelector.getSelectedItemId()));
            }
        });




        //

        //_____________________________________SKILL SELECTOR TBC_______________________________________
        String[] array = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eight2", "nine2", "ten2"};
        MultiSelectionSpinner skillSelector = (MultiSelectionSpinner) findViewById(R.id.spinnerSkillSelectorQuestAdd);

        skillSelector.setItems(array);
        skillSelector.setListener(this);

        //____________________________________QUEST SELECTOR TBC________________________________________
        String[] array2 = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eight2", "nine2", "ten2", "seven", "eight", "nine", "ten", "eight2", "nine2", "ten2"};
        ArrayAdapter<String> questAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array2);
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

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }
}
