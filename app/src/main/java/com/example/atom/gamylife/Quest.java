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
    private long parentID;

    public Quest(long newQuestID, String newName, String newDescr, int newExp,
                      ArrayList<Skill> newSkillsAffected, int newDuration, Date newScheduled,
                      long newParentID) {
        questID = newQuestID;
        name = newName;
        description = newDescr;
        experience = newExp;
        skillAffected = newSkillsAffected;
        duration = newDuration;
        scheduled = newScheduled;
        parentID = newParentID;
    }

    public Quest(Parcel in) {

        questID = in.readLong();
        name = in.readString();
        description = in.readString();
        experience = in.readInt();
        skillAffected = in.readArrayList(null);
        duration = in.readInt();
        scheduled = (Date) in.readSerializable();
        parentID = in.readLong();
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
        dest.writeList(skillAffected);
        dest.writeInt(duration);
        dest.writeSerializable(scheduled);
        dest.writeLong(parentID);
    }

    public String getName() {

        return name;
    }
}
