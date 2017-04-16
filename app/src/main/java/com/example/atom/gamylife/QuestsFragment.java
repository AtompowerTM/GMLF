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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import Database.GamylifeDB;

/**
 * Created by Atom on 11/04/2017.
 */

public class QuestsFragment extends Fragment{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private SQLiteDatabase db;
    private ArrayList<Quest> questEntries;

    private static final String SQL_SELECT_ALL_QUESTS = "SELECT * FROM " +
            GamylifeDB.GamylifeQuestEntry.TABLE_NAME;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View layout= inflater.inflate(R.layout.activity_quests_fragment, viewGroup, false);

        //Fetch the database
        db = ((MainActivity)getActivity()).db;

        //Fill the array list with the quests in the database
        //REMOVE populateQuestEntries();

        final Context recyclerViewContext = layout.getContext();

        recyclerView = (RecyclerView) layout.findViewById(R.id.RecyclerViewQuests);
        layoutManager = new LinearLayoutManager(recyclerViewContext);
        recyclerView.setLayoutManager(layoutManager);

        //Populate the recycler view with the quests
        //REMOVE adapter = new AdapterQuest(questEntries);
        //REMOVE recyclerView.setAdapter(adapter);

        FloatingActionButton addQuestButton = (FloatingActionButton) layout.findViewById(R.id.addQuestButton);
        addQuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addQuest(view, recyclerViewContext);
            }
        });

        return layout;
    }

    //Start the quest add activity, switching to that screen
    public void addQuest(View view, Context context) {

        Intent intent = new Intent(context, QuestAdd.class);
        startActivityForResult(intent, 1);
    }

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
}
