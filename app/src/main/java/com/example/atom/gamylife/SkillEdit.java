package com.example.atom.gamylife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Database.GamylifeDbHelper;
import Skill.Skill;

/**
 * Created by Atom on 04/04/2017.
 */

public class SkillEdit extends AppCompatActivity {

    EditText name = null;
    EditText description = null;
    TextView levelLabel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_edit);

        Skill currentSkill = null;
        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        if(bundle != null) {
            currentSkill = bundle.getParcelable("skill");
        }

        name = (EditText) findViewById(R.id.editTextNameSkillEdit);
        description = (EditText) findViewById(R.id.editTextDescriptionSkillEdit);
        levelLabel = (TextView) findViewById(R.id.textViewLevelSkillEdit);

        if(currentSkill != null) {
            name.setText(currentSkill.getName());
            description.setText(currentSkill.getDescription());
            levelLabel.setText("Level: " + Long.toString(currentSkill.getLevel()));
        } else {
            Toast toast = Toast.makeText(this, "Error! Could not retreive skill.", Toast.LENGTH_LONG);
            toast.show();
        }

        final Button DoneEditSkill = (Button) findViewById(R.id.buttonSkillDoneSkillEdit);
        final GamylifeDbHelper mDbHelper = new GamylifeDbHelper(this);

        DoneEditSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
