package com.example.atom.gamylife;

import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Atom on 06/04/2017.
 */

public class Quest implements Parcelable {

    private long questID;
    private String name;
    private String description;
    private int experience;
    private ArrayList<Skill> skillAffected;
    private int duration;   //in minutes
    private Date scheduled;
    private Quest parent;


    public void Quest(long newQuestID, String newName, String newDescr, int newExp,
                      ArrayList<Skill> newSkillsAffected, int newDuration, Date newScheduled,
                      Quest newParent) {
        questID = newQuestID;
        name = newName;
        description = newDescr;
        experience = newExp;
        skillAffected = newSkillsAffected;
        duration = newDuration;
        scheduled = newScheduled;
        parent = newParent;
    }

    public Quest(Parcel in) {

        String data [] = new String[1];

        in.readStringArray(data);
        name = data[0];
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

    }
}
