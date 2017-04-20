package com.example.atom.gamylife;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import Database.GamylifeDB;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Atom on 11/04/2017.
 */

public class SkillsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private SQLiteDatabase db;
    private ArrayList<Skill> skillEntries;

    /*
    private static final String SQL_SELECT_ALL_SKILLS = "SELECT * FROM " +
            GamylifeDB.GamylifeSkillEntry.TABLE_NAME;
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View layout= inflater.inflate(R.layout.activity_skills_fragment, viewGroup, false);

        //Fetch the database
        db = ((MainActivity)getActivity()).db;

        //Fill the array list with the skills in the database
        skillEntries = (((MainActivity) getActivity()).skillEntries);
        //populateSkillEntries();

        final Context recyclerViewContext = layout.getContext();

        recyclerView = (RecyclerView) layout.findViewById(R.id.RecyclerViewSkills);
        layoutManager = new LinearLayoutManager(recyclerViewContext);
        recyclerView.setLayoutManager(layoutManager);

        //Populate the recycler view with the skills
        adapter = new AdapterSkill(skillEntries);
        recyclerView.setAdapter(adapter);

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

        //OnClick for skill add floating button
        FloatingActionButton bAddSkill = (FloatingActionButton) layout.findViewById(R.id.addSkillButton);
        bAddSkill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                addSkill(view, recyclerViewContext);
            }
        });

        return layout;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible()) {
            if (isVisibleToUser) {
                adapter.notifyDataSetChanged();
            }
        }
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will !!!!!!
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

    //After a skill has been added and the Skill add activity has ended update the skill entries
    //list and notify the recyclerView adapter
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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

    public void getQuestCompletedNotification() {

        adapter.notifyDataSetChanged();
    }

    //Start the skill add activity, switching to that screen
    public void addSkill(View view, Context context) {

        Intent intent = new Intent(context, SkillAdd.class);
        startActivityForResult(intent, 1);
    }
    /*
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
    */
}
