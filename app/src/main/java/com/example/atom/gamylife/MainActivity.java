package com.example.atom.gamylife;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import Database.GamylifeDB;
import Database.GamylifeDbHelper;

/**
 * Created by Atom on 11/04/2017.
 */

public class MainActivity extends AppCompatActivity{

    ArrayList<Fragment> fragments;
    TabLayout tabLayout;

    public GamylifeDbHelper mDbHelper;
    public SQLiteDatabase db;

    private static final String SQL_SELECT_ALL_SKILLS = "SELECT * FROM " +
            GamylifeDB.GamylifeSkillEntry.TABLE_NAME;
    private static final String SQL_SELECT_ALL_QUESTS = "SELECT * FROM " +
            GamylifeDB.GamylifeQuestEntry.TABLE_NAME;

    public ArrayList<Skill> skillEntries;
    public ArrayList<Quest> questEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize SQLite DB
        mDbHelper = new GamylifeDbHelper(this);
        db = mDbHelper.getWritableDatabase();

        populateSkillEntries();
        populateQuestEntries();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Skills"));
        tabLayout.addTab(tabLayout.newTab().setText("Quests"));
        tabLayout.addTab(tabLayout.newTab().setText("Calendar"));

        fragments = new ArrayList<>();
        fragments.add(Fragment.instantiate(this, SkillsFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, QuestsFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, QuestsFragment.class.getName()));

        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(), fragments));
        tabLayout.setupWithViewPager(viewPager);
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

    //Populate the questEntries list with the skills saved in the database
    private void populateQuestEntries() {

        questEntries = new ArrayList<Quest>();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_QUESTS, null);

        while(cursor.moveToNext()) {
            //cursor.getInt(0) is the ID of the skill (which is an autonum)
            //questEntries.add(new Quest((long) cursor.getLong(0), cursor.getString(1),
            //        cursor.getString(2), cursor.getInt(3)));
            /*
            long newQuestID, String newName, String newDescr, int newExp,
                      ArrayList<Skill> newSkillsAffected, int newDuration, Date newScheduled,
                      long newParentID
             */

        }
    }
}
