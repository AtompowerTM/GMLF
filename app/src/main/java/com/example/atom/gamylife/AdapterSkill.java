package com.example.atom.gamylife;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;

import Skill.Skill;

/**
 * Created by Atom on 26/03/2017.
 */

public class AdapterSkill extends RecyclerView.Adapter<AdapterSkill.SkillViewHolder> {

    ArrayList<Skill> skillList;

    public AdapterSkill(ArrayList<Skill> skillList) {

        this.skillList = skillList;
    }

    @Override
    public AdapterSkill.SkillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.custom_skill_recycler_view, parent, false);
        SkillViewHolder viewHolder = new SkillViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterSkill.SkillViewHolder holder, int position) {

        Skill skill = skillList.get(position);

        int expForCurLevel = skill.getExpForCurLevel();
        int expTillNextLevel = skill.getExpTillNextLevel();
        int level = skill.getLevel();
        int expProgress = expForCurLevel - expTillNextLevel;

        holder.progressBar.setMax(expForCurLevel);
        holder.progressBar.setProgress(expProgress);

        holder.textLevel.setText("Level " + Integer.toString(level) + " ");
        holder.textName.setText(skill.getName().toString());
        holder.textExp.setText(expTillNextLevel + " EXP to next level");
    }

    @Override
    public int getItemCount() {
        return skillList.size();
    }

    public static class SkillViewHolder extends RecyclerView.ViewHolder{

        protected TextView textName;
        protected TextView textExp;
        protected ProgressBar progressBar;
        protected TextView textLevel;

        public SkillViewHolder(View itemView) {
            super(itemView);
            textLevel = (TextView) itemView.findViewById(R.id.textLevelSkillRecycler);
            textName = (TextView) itemView.findViewById(R.id.textNameSkillRecycler);
            textExp = (TextView) itemView.findViewById(R.id.textExpSkillRecycler);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBarSkill);
        }
    }
}
