package com.example.atom.gamylife;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Atom on 18/04/2017.
 */

public class AdapterQuest extends RecyclerView.Adapter<AdapterQuest.QuestViewHolder>{

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    ArrayList<Quest> questList;

    public AdapterQuest(ArrayList<Quest> questList) {

        this.questList = questList;
    }

    @Override
    public AdapterQuest.QuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.custom_quest_recycler_view, parent, false);
        AdapterQuest.QuestViewHolder viewHolder = new AdapterQuest.QuestViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterQuest.QuestViewHolder holder, int position) {

        Quest quest = questList.get(position);

        long parentID = quest.getParentID();

        if(parentID == -1) {

            String questName = quest.getName();
            String dueDate;
            if (quest.getScheduled() != null) {
                dueDate = sdf.format(quest.getScheduled());
            } else {
                dueDate = "Not scheduled";
            }

            int duration = quest.getDuration();
            int hours = duration / 60;
            int minutes = duration % 60;
            int progress = 0;
            int experience = quest.getExperience();


            holder.progressBar.setMax(100);
            holder.progressBar.setProgress(progress);

            holder.textName.setText(questName);
            holder.textDueDate.setText(dueDate);
            holder.textDuration.setText(hours + "h " + minutes + "m ");
            holder.textProgress.setText(Integer.toString(progress) + "%");
            holder.textExperience.setText(Integer.toString(experience) + "xp");
        }
    }

    @Override
    public int getItemCount() {
        return questList.size();
    }

    public static class QuestViewHolder extends RecyclerView.ViewHolder{

        protected TextView textName;
        protected TextView textDueDate;
        protected ProgressBar progressBar;
        protected TextView textProgress;
        protected TextView textDuration;
        protected TextView textExperience;

        public QuestViewHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.textNameQuestRecycler);
            textDueDate = (TextView) itemView.findViewById(R.id.textDueDateRecycler);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBarQuestRecycler);
            textProgress = (TextView) itemView.findViewById(R.id.textQuestProgressRecycler);
            textDuration = (TextView) itemView.findViewById(R.id.textQuestDurationRecycler);
            textExperience = (TextView) itemView.findViewById(R.id.textQuestRewardRecycler);
        }
    }
}
