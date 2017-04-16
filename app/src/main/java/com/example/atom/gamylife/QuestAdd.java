package com.example.atom.gamylife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.guna.libmultispinner.MultiSelectionSpinner;

import java.util.List;

/**
 * Created by Atom on 16/04/2017.
 */

public class QuestAdd extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_add);

        String[] array = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",  "eight2", "nine2", "ten2"};
        String[] array2 = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",  "eight2", "nine2", "ten2", "seven", "eight", "nine", "ten",  "eight2", "nine2", "ten2"};
        MultiSelectionSpinner skillSelector = (MultiSelectionSpinner) findViewById(R.id.spinnerSkillSelectorQuestAdd);
        Spinner parentSelector = (Spinner) findViewById(R.id.spinnerParentSelectorQuestAdd);

        skillSelector.setItems(array);
        skillSelector.setSelection(new int[]{2, 6});
        skillSelector.setListener(this);

        ArrayAdapter<String> questAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array2);

        // Drop down layout style - list view with radio button
        questAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        parentSelector.setAdapter(questAdapter);

    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }
}
