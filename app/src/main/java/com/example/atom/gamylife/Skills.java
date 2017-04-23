package com.example.atom.gamylife;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabWidget;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;

import Database.GamylifeDB;
import Database.GamylifeDbHelper;

public class Skills extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public TabLayout tabLayout;
    public TabLayout.Tab skillTab;
    public TabLayout.Tab questTab;

    private GamylifeDbHelper mDbHelper;
    private SQLiteDatabase db;

    private ArrayList<Skill> skillEntries;

    private static final String SQL_SELECT_ALL_SKILLS = "SELECT * FROM " +
            GamylifeDB.GamylifeSkillEntry.TABLE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Skills"));
        tabLayout.addTab(tabLayout.newTab().setText("Quests"));
        tabLayout.addTab(tabLayout.newTab().setText("Calendar"));

        //Initialize SQLite DB
        mDbHelper = new GamylifeDbHelper(this);
        db = mDbHelper.getReadableDatabase();

        //Fill the array list with the skills in the database
        populateSkillEntries();

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewSkills);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final Context recyclerViewContext = this;

        //Set an onClick listener for the recycler view
        recyclerView.addOnItemTouchListener(new SkillCustomOnClickListener(recyclerViewContext, recyclerView,
                new SkillCustomOnClickListener.RecyclerViewItemListener() {
                    @Override
                    public void onClick(View view, int position) {
                        //Toast toast = Toast.makeText(recyclerViewContext, skillEntries.get(position)
                         //       .getName(), Toast.LENGTH_SHORT);
                        //toast.show();!!!!!!!!!!!!!

                        //skillEntries.get(position).getName();

                        Intent intent = new Intent(recyclerViewContext, SkillEdit.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("skill", skillEntries.get(position));
                        intent.putExtra("bundle", bundle);

                        startActivityForResult(intent, 1);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        //Populate the recycler view with the skills
        adapter = new AdapterSkill(skillEntries);
        recyclerView.setAdapter(adapter);

        //OnClick for skill add floating button
        FloatingActionButton bAddSkill = (FloatingActionButton) findViewById(R.id.addSkillButton);
        bAddSkill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                addSkill(view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_skills, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //After a skill has been added and the Skill add activity has ended update the skill entries
    //list and notify the recyclerView adapter
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bundle bundle;
        //Skill newSkill;!!!!!!!!!!!!!
        if (requestCode == 1 && resultCode == RESULT_OK) {
            /*!!!!!!!!!!!!!!!!
                newSkill = new Skill(data.getStringExtra("SKILL_NAME"),
                        data.getStringExtra("SKILL_DESCRIPTION"),
                        data.getIntExtra("SKILL_EXP", 0));
            skillEntries.add(newSkill);
            */
            bundle = data.getBundleExtra("bundle");
            skillEntries.add((Skill) bundle.getParcelable("skill"));
            //Toast toast = Toast.makeText(this, skillEntries.get(skillEntries.size()-1).getID() +
            //        " " + skillEntries.get(skillEntries.size()-1).getName(), Toast.LENGTH_LONG);
            //toast.show();
            adapter.notifyDataSetChanged();
        }
    }

    //Start the skill add activity, switching to that screen
    public void addSkill(View view) {

        Intent intent = new Intent(this, SkillAdd.class);
        startActivityForResult(intent, 1);
    }

    //Populate the skillEntries list with the skills saved in the database
    private void populateSkillEntries() {

        skillEntries = new ArrayList<Skill>();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_SKILLS, null);

        while(cursor.moveToNext()) {
            //cursor.getInt(0) is the ID of the skill (which is an autonum)
            skillEntries.add(new Skill((long) cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getInt(3)));
        }
    }
}
