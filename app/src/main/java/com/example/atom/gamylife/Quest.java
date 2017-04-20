package com.example.atom.gamylife;

import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Atom on 06/04/2017.
 */

public class Quest implements Parcelable {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private long questID;
    private String name;
    private String description;
    private int experience;
    private ArrayList<Skill> skillAffected;
    private int duration;   //in minutes
    private Date scheduled;
    private long parentID;
    private boolean completed;
    private int progress;
    private ArrayList<Quest> subquests;

    public Quest(long newQuestID, String newName, String newDescr, int newExp,
                      ArrayList<Skill> newSkillsAffected, int newDuration, Date newScheduled,
                      long newParentID, boolean newCompleted) {
        questID = newQuestID;
        name = newName;
        description = newDescr;
        experience = newExp;
        skillAffected = newSkillsAffected;
        duration = newDuration;
        scheduled = newScheduled;
        parentID = newParentID;
        completed = newCompleted;
        progress = 0;
        subquests = new ArrayList<>();
    }

    public Quest(long newQuestID, String newName, String newDescr, int newExp,
                 ArrayList<Skill> newSkillsAffected, int newDuration, String newScheduled,
                 long newParentID, boolean newCompleted) {
        questID = newQuestID;
        name = newName;
        description = newDescr;
        experience = newExp;
        skillAffected = newSkillsAffected;
        duration = newDuration;
        try {
            scheduled = sdf.parse(newScheduled);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("ErrorQuestID", Long.toString(questID));
            scheduled = new Date();
        }
        parentID = newParentID;
        completed = newCompleted;
        progress = 0;
        subquests = new ArrayList<>();
    }

    public Quest(Parcel in) {

        skillAffected = new ArrayList<Skill>();

        questID = in.readLong();
        name = in.readString();
        description = in.readString();
        experience = in.readInt();
        in.readTypedList(skillAffected, Skill.CREATOR);
        duration = in.readInt();
        scheduled = (Date) in.readSerializable();
        parentID = in.readLong();
        completed = in.readByte() != 0;
        progress = in.readInt();
        //in.readTypedList(subquests, Quest.CREATOR);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Quest createFromParcel(Parcel in) {

            return new Quest(in);
        }

        public Quest[] newArray(int size) {

            return new Quest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(questID);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(experience);
        dest.writeTypedList(skillAffected);
        dest.writeInt(duration);
        dest.writeSerializable(scheduled);
        dest.writeLong(parentID);
        dest.writeByte((byte) (completed ? 1 : 0));
        dest.writeInt(progress);
        //dest.writeTypedList(subquests);
    }

    public long getID() {

        return questID;
    }

    public String getName() {

        return name;
    }

    public String getDescription() {

        return description;
    }

    public Date getScheduled() {

        return scheduled;
    }

    public int getDuration() {

        return duration;
    }

    public int getExperience() {

        return  experience;
    }

    public long getParentID() {

        return parentID;
    }

    public ArrayList<Skill> getSkillAffected() {

        return skillAffected;
    }

    public boolean isCompleted(){

        return completed;
    }

    public void addSubQuest(Quest subquest) {

        subquests.add(subquest);
    }

    public ArrayList<Quest> getSubquests() {

        return subquests;
    }

    public void setCompleted(boolean status) {

        completed = status;
    }

    public boolean getCompleted() {

        return completed;
    }
}
