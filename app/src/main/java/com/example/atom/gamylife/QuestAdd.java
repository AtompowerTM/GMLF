package com.example.atom.gamylife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
        MultiSelectionSpinner multiSelectionSpinner = (MultiSelectionSpinner)
                findViewById(R.id.spinnerSkillSelectorQuestAdd);
        multiSelectionSpinner.setItems(array);
        multiSelectionSpinner.setSelection(new int[]{2, 6});
        multiSelectionSpinner.setListener(this);
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }
}
