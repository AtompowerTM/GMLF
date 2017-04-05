package com.example.atom.gamylife;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import Database.GamylifeDB;
import Database.GamylifeDbHelper;
import Skill.Skill;

public class SkillAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_add);

        final Button AddSkill = (Button) findViewById(R.id.buttonSkillDoneSkillAdd);
        final GamylifeDbHelper mDbHelper = new GamylifeDbHelper(this);

        AddSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSkill(mDbHelper);
            }
        });
    }

    private void addSkill(GamylifeDbHelper mDbHelper) {

        long skillID;
        final EditText textName = (EditText) findViewById(R.id.editTextNameSkillAdd);
        final EditText textDescription = (EditText) findViewById(R.id.editTextDescriptionSkillAdd);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        String name = textName.getText().toString();
        String description = textDescription.getText().toString();

        values.put(GamylifeDB.GamylifeSkillEntry.COLUMN_NAME_NAME, name);
        values.put(GamylifeDB.GamylifeSkillEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(GamylifeDB.GamylifeSkillEntry.COLUMN_NAME_EXPERIENCE, 0);

        skillID = db.insert(GamylifeDB.GamylifeSkillEntry.TABLE_NAME, null, values);

        //Add the new skill's values as a return intent to update the skill entries array list
        Intent returnIntent = new Intent();
        /*
        returnIntent.putExtra("SKILL_NAME", name);!!!!!!!!!!!!
        returnIntent.putExtra("SKILL_DESCRIPTION", description);
        returnIntent.putExtra("SKILL_EXP", 0);
        */

        Skill newSkill = new Skill(skillID, name, description, 0);

        Bundle bundle = new Bundle();
        bundle.putParcelable("skill", newSkill);
        returnIntent.putExtra("bundle", bundle);

        setResult(Activity.RESULT_OK, returnIntent);

        //Finish this activity and return to Skills
        finish();
    }
}
