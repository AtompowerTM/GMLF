package com.example.atom.gamylife;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import Database.GamylifeDbHelper;

/**
 * Created by Atom on 11/04/2017.
 */

public class MainActivity extends AppCompatActivity{

    ArrayList<Fragment> fragments;
    TabLayout tabLayout;

    public GamylifeDbHelper mDbHelper;
    public SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize SQLite DB
        mDbHelper = new GamylifeDbHelper(this);
        db = mDbHelper.getReadableDatabase();

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
}
