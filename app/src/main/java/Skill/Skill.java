package Skill;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.StringBuilderPrinter;

/**
 * Created by Atom on 22/02/2017.
 */

public class Skill implements Parcelable{

    private String name;
    private String description;
    private int exp;
    private long skillID;

    //Used when creating new skill from app
    public Skill(String newName, String newDescr, int newExp) {

        name = newName;
        description = newDescr;
        exp = newExp;
    }

    //Used when getting skills from the database
    public Skill(long skillID, String newName, String newDescr, int newExp) {

        this.skillID = skillID;
        name = newName;
        description = newDescr;
        exp = newExp;
    }

    public Skill(Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);
        skillID = Long.parseLong(data[0]);
        name = data[1];
        description = data[2];
        exp = Integer.parseInt(data[3]);
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Skill createFromParcel(Parcel in) {

            return new Skill(in);
        }

        public Skill[] newArray(int size) {

            return new Skill[size];
        }
    };

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeStringArray(new String[] {Long.toString(skillID), name,
                description,
                Integer.toString(exp)});
    }

    public void setName(String newName) {

        name = newName;
    }

    public String getName(){

        return name;
    }

    public void setDescription(String newDescription) {

        description = newDescription;
    }

    public String getDescription(){

        return description;
    }

    public void setExp(int newExp) {

        exp = newExp;
    }

    public int getExp() {

        return exp;
    }

    public long getID() {

        return skillID;
    }

    public void setID(long skillID) {

        this.skillID = skillID;
    }

    public int getLevel() {

        int expForCurLevel = 1000;
        int level = 1;

        while( exp >= expForCurLevel ) {
            exp -= expForCurLevel;
            expForCurLevel += 1000;
            level++;
        }

        return level;
    }

}
