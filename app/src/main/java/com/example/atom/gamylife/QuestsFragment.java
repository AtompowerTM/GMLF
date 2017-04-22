package com.example.atom.gamylife;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;

import java.util.ArrayList;
import java.util.Date;

import Database.GamylifeDB;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Atom on 11/04/2017.
 */

public class QuestsFragment extends Fragment{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private SQLiteDatabase db;
    private ArrayList<Quest> questEntries;
    private ArrayList<Skill> skillEntries;
    private ArrayList<Quest> mainQuestEntries;

    private Toast levelUpToast;
    private Toast questCompleteToast;
    private TextView toastText;
    private TextView questCompleteText;

    /*
    private static final String SQL_SELECT_ALL_QUESTS = "SELECT * FROM " +
            GamylifeDB.GamylifeQuestEntry.TABLE_NAME;
    */

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View layout= inflater.inflate(R.layout.activity_quests_fragment, viewGroup, false);

        //Fetch the database
        db = ((MainActivity)getActivity()).db;

        questEntries = ((MainActivity) getActivity()).questEntries;
        skillEntries = ((MainActivity) getActivity()).skillEntries;
        mainQuestEntries = new ArrayList<>();

        for(int i = 0; i < questEntries.size(); i++) {
            if(!questEntries.get(i).getCompleted()) {
                //if(questEntries.get(i).getParentID() == -1) { //this will be used to show only the main
                    //quests in the list

                    //but, for now, just show both main and subquests
                    mainQuestEntries.add(questEntries.get(i));
                //}
            }
        }

        //Fill the array list with the quests in the database
        //questEntries = new ArrayList<>();
        //REMOVE populateQuestEntries();

        final Context recyclerViewContext = layout.getContext();

        recyclerView = (RecyclerView) layout.findViewById(R.id.RecyclerViewQuests);
        layoutManager = new LinearLayoutManager(recyclerViewContext);
        recyclerView.setLayoutManager(layoutManager);

        //Populate the recycler view with the skills
        adapter = new AdapterQuest(mainQuestEntries);
        recyclerView.setAdapter(adapter);

        View toastLayoutQuestComplete = inflater.inflate(R.layout.skill_levelup_toast,
                (ViewGroup) layout.findViewById(R.id.toastLevelUp));

        View toastLayout = inflater.inflate(R.layout.skill_levelup_toast,
                (ViewGroup) layout.findViewById(R.id.toastLevelUp));

        toastText = (TextView) toastLayout.findViewById(R.id.textToast);
        questCompleteText = (TextView) toastLayoutQuestComplete.findViewById(R.id.textToast);

        levelUpToast = new Toast(toastLayout.getContext());
        levelUpToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        levelUpToast.setDuration(Toast.LENGTH_LONG);
        levelUpToast.setView(toastLayout);

        questCompleteToast = new Toast(toastLayoutQuestComplete.getContext());
        questCompleteToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        questCompleteToast.setDuration(Toast.LENGTH_LONG);
        questCompleteToast.setView(toastLayoutQuestComplete);

        //Set an onClick listener for the recycler view
        recyclerView.addOnItemTouchListener(new SkillCustomOnClickListener(recyclerViewContext, recyclerView,
                new SkillCustomOnClickListener.RecyclerViewItemListener() {
                    @Override
                    public void onClick(View view, final int position) {

                        Log.d("QuestPosition", Integer.toString(position));

                        new AlertDialog.Builder(layout.getContext())
                                .setTitle("Complete")
                                .setMessage("Do you want to complete this quest?")
                                .setIcon(android.R.drawable.checkbox_on_background)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        completeQuest(position);
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();

                        //Translate mainQuestEntries position to questEntries position
                        //Log.d("oldPosition", Integer.toString(position));
                        /*Intent intent = new Intent(recyclerViewContext, SkillEdit.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("skill", skillEntries.get(position));
                        intent.putExtra("bundle", bundle);

                        startActivityForResult(intent, 1);*/
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                        editQuest(view, recyclerViewContext, position);
                    }
                }));

        FloatingActionButton addQuestButton = (FloatingActionButton) layout.findViewById(R.id.addQuestButton);
        addQuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addQuest(view, recyclerViewContext);
            }
        });
        return layout;
    }

    //Start the quest add activity, in add quest mode (mode 1)
    public void addQuest(View view, Context context) {

        Intent intent = new Intent(context, QuestAdd.class);

        //ArrayList<Skill> testSkill = new ArrayList<>();
        //testSkill.add(skillEntries.get(0));
        //Log.d("enteredSkill", testSkill.get(0).getName());
        //Date testDate = new Date();
        //Quest testQuest = new Quest(24, "pencho", "", 0, testSkill, 0, testDate, -1);
        //Quest testQuest2 = new Quest(25, "gosho", "", 0, testSkill, 0, testDate, -1);
        //questEntries.add(testQuest);
        //questEntries.add(testQuest2);

        Bundle bundle = new Bundle();
        bundle.putInt("mode", 1); //mode 1 is add quest; mode 2 is edit quest
        bundle.putParcelableArrayList("quests", questEntries);
        bundle.putParcelableArrayList("skills", skillEntries);
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, 1);
    }

    //Start the quest add activity, in add quest mode (mode 1)
    public void editQuest(View view, Context context, int position) {

        Intent intent = new Intent(context, QuestAdd.class);

        Bundle bundle = new Bundle();
        bundle.putInt("mode", 2); //mode 1 is add quest; mode 2 is edit quest
        bundle.putParcelableArrayList("quests", questEntries);
        bundle.putParcelableArrayList("skills", skillEntries);

        //Translate mainQuestEntries position to questEntries position
        Log.d("oldPosition", Integer.toString(position));
        position = questEntries.indexOf(mainQuestEntries.get(position));
        Log.d("newPosition", Integer.toString(position));

        bundle.putInt("questIndex", position);
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, 2);
    }


    //After a quest has been added and the QuestAdd activity has ended update the quest entries
    //list and notify the recyclerView adapter
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bundle bundle;
        if (requestCode == 1 && resultCode == RESULT_OK) {

            bundle = data.getBundleExtra("bundle");
            Quest newQuest = (Quest) bundle.getParcelable("quest");
            questEntries.add(newQuest);
            //if(newQuest.getParentID() == -1) {//for future implementation of subquests
                mainQuestEntries.add(newQuest);
            //}
            adapter.notifyDataSetChanged();

            Log.d("NEWQUEST", questEntries.get(questEntries.size()-1).getName());
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            //find quest ID in questEntries and replace that quest with the new one
            //do the same for mainQuestEntries if it's there (might need to add it/remove it from there
        }
    }

    public void completeQuest(int position) {

        int mainQuestEntriesPosition = position;
        position = questEntries.indexOf(mainQuestEntries.get(position));

        String SQL_UPDATE_QUEST_COMPLETED;
        String SQL_UPDATE_SKILL_EXP;
        StringBuffer toastString = new StringBuffer("Congratulations!\n");

        Quest completedQuest = questEntries.get(position);

        //Update affected skills' experience values in the db, the quest's affectedSkills list,
        // and in skillEntries
        for(int i = 0; i < completedQuest.getSkillAffected().size(); i++) {

            Skill affectedSkill = completedQuest.getSkillAffected().get(i);
            int skillPosition = findIndexOfSkill(affectedSkill.getID());
            int skillLevel = affectedSkill.getLevel();

            //update skill exp in questEntries's affectedSkills ArrayList
            int newSkillTotalExp = questEntries.get(position).getSkillAffected().get(i)
                    .gainExpAndReturnValue(completedQuest.getExperience());

            //update in db
            SQL_UPDATE_SKILL_EXP = "UPDATE " + GamylifeDB.GamylifeSkillEntry.TABLE_NAME +
                    " SET " + GamylifeDB.GamylifeSkillEntry.COLUMN_NAME_EXPERIENCE +
                    " = " + newSkillTotalExp + " WHERE " +
                    GamylifeDB.GamylifeSkillEntry.COLUMN_NAME_ID + " = " + affectedSkill.getID();

            db.execSQL(SQL_UPDATE_SKILL_EXP);

            //update in skillEntries
            skillEntries.get(skillPosition).setTotalExp(newSkillTotalExp);

            if(skillLevel < skillEntries.get(skillPosition).getLevel()) {
                toastString.append(skillEntries.get(skillPosition).getName() +
                        " leveled up to level " + skillEntries.get(skillPosition).getLevel() + "!\n");
            }
        }

        //Update quest's status in the db, mainQuestEntries, and questEntries
        SQL_UPDATE_QUEST_COMPLETED = "UPDATE " + GamylifeDB.GamylifeQuestEntry.TABLE_NAME +
                " SET " + GamylifeDB.GamylifeQuestEntry.COLUMN_NAME_COMPLETED +
                " = 1 " + " WHERE " + GamylifeDB.GamylifeQuestEntry.COLUMN_NAME_ID +
                " = " + completedQuest.getID();

        db.execSQL(SQL_UPDATE_QUEST_COMPLETED);
        mainQuestEntries.remove(mainQuestEntriesPosition);
        questEntries.get(position).setCompleted(true);

        adapter.notifyDataSetChanged();

        questCompleteText.setText(completedQuest.getName() + " completed!");
        questCompleteToast.show();

        if (!toastString.toString().equals("Congratulations!\n")){
            toastText.setText(toastString);
            levelUpToast.show();
        }

       /* Log.d("toasts", "TOASTING");
        Toast.makeText(this.getContext(), "smthing", Toast.LENGTH_LONG);
        Toast.makeText(this.getContext(), "smthing2", Toast.LENGTH_LONG);
        Toast.makeText(this.getContext(), "smthingororhet", Toast.LENGTH_LONG);*/
       /* Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    levelUpToast.show();
                    Thread.sleep(3500); // As I am using LENGTH_LONG in Toast
                    toastText.setText("text 2");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };*/
    }

    private int findIndexOfSkill(long ID) {

        for(int i = 0; i < skillEntries.size(); i++) {
            if(skillEntries.get(i).getID() == ID) {
                return i;
            }
        }
        return -1;
    }

    /*
    //Populate the skillEntries list with the skills saved in the database
    private void populateQuestEntries() {

        questEntries = new ArrayList<Quest>();
        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_QUESTS, null);

        while(cursor.moveToNext()) {
            //cursor.getInt(0) is the ID of the skill (which is an autonum)
            //questEntries.add(new Quest((long) cursor.getInt(0), cursor.getString(1),
            //        cursor.getString(2), cursor.getInt(3)));
        }
    }
    */
}
