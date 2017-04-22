package com.example.atom.gamylife;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Database.GamylifeDB;
import Database.GamylifeDbHelper;

/**
 * Created by Atom on 11/04/2017.
 */

public class MainActivity extends AppCompatActivity{

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    ArrayList<Fragment> fragments;
    TabLayout tabLayout;

    public GamylifeDbHelper mDbHelper;
    public SQLiteDatabase db;

    private static final String SQL_SELECT_ALL_SKILLS = "SELECT * FROM " +
            GamylifeDB.GamylifeSkillEntry.TABLE_NAME;
    private static final String SQL_SELECT_ALL_QUESTS = "SELECT * FROM " +
            GamylifeDB.GamylifeQuestEntry.TABLE_NAME;
    private static final String SQL_SELECT_ALL_QUEST_SKILL = "SELECT * FROM " +
            GamylifeDB.GamylifeQuestSkillEntry.TABLE_NAME;


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

        //populate quests' subquests lists
        for(int i = 0; i < questEntries.size(); i++) {
            Quest curQuest = questEntries.get(i);

            if(curQuest.getParentID() != -1) {
                questloop:
                for(Quest quest : questEntries) {
                    if(quest.getID() == curQuest.getParentID()) {
                        quest.addSubQuest(curQuest);
                        break questloop;
                    }
                }
            }
        }

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        final CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Skills"));
        tabLayout.addTab(tabLayout.newTab().setText("Quests"));
        tabLayout.addTab(tabLayout.newTab().setText("Calendar"));

        fragments = new ArrayList<>();
        fragments.add(Fragment.instantiate(this, SkillsFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, QuestsFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, CalendarFragment.class.getName()));

        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(), fragments));

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //Populate the skillEntries list with the skills saved in the database
    private void populateSkillEntries() {

        skillEntries = new ArrayList<Skill>();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_SKILLS, null);

        //Log.d("cursorSkill", Integer.toString(cursor.getCount()));

        while(cursor.moveToNext()) {
            skillEntries.add(new Skill( cursor.getLong(0), cursor.getString(1),
                    cursor.getString(2), cursor.getInt(3)));
        }
    }

    //Populate the questEntries list with the skills saved in the database
    private void populateQuestEntries() {

        questEntries = new ArrayList<Quest>();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_QUESTS, null);
        Cursor cursorQuestSkill = db.rawQuery(SQL_SELECT_ALL_QUEST_SKILL, null);

        ArrayList<Long> allQuestIDs = new ArrayList<>();
        ArrayList<Long> allSkillIDs = new ArrayList<>();

        //int k = 1;
        //Log.d("cursor", Integer.toString(cursorQuestSkill.getCount()));
        while(cursorQuestSkill.moveToNext()) {

            allQuestIDs.add(cursorQuestSkill.getLong(0));
            allSkillIDs.add(cursorQuestSkill.getLong(1));
        }

        while(cursor.moveToNext()) {

            long questID = cursor.getLong(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            int experience = cursor.getInt(3);

            Log.d("quest and skill", allQuestIDs.size() + " " + allSkillIDs.size());

            ArrayList<Skill> skillAffected = new ArrayList<>();
            for(int i = 0; i < allQuestIDs.size(); i++) {
                if(questID == allQuestIDs.get(i)) {
                    int j = 0;
                    while(skillEntries.get(j).getID() != allSkillIDs.get(i)) {
                        if( j < skillEntries.size()-1)
                            j++;
                        //else throw exception... skill not found
                    }
                    skillAffected.add(skillEntries.get(j));
                    //skillAffected.add(skillEntries.get((allSkillIDs.get(i).intValue())));
                }
            }

            int duration = cursor.getInt(4); //in minutes
            Date scheduled = new Date();
            try {
                scheduled = sdf.parse(cursor.getString(5));
            } catch (ParseException e) {
                e.printStackTrace();
                scheduled = null;
            }

            long parentID = cursor.getLong(6);
            boolean completed = cursor.getInt(7) != 0;

            questEntries.add(new Quest(questID, name, description, experience, skillAffected,
                    duration, scheduled, parentID, completed));
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
