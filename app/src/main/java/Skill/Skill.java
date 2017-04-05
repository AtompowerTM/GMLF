package Skill;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Atom on 22/02/2017.
 */

public class Skill implements Parcelable{

    private String name;
    private String description;
    private int totalExp;   //total exp (saved in the database)
    private int exp;        //exp earned for the current level
    private long skillID;
    private int level;
    private int expForCurLevel;

    //Used when creating new skill from app!!!!!!!!!!!!
    public Skill(String newName, String newDescr, int newTotalExp) {

        name = newName;
        description = newDescr;
        totalExp = newTotalExp;
        initializeLevelAndExp();
    }

    //Used when getting skills from the database
    public Skill(long skillID, String newName, String newDescr, int newTotalExp) {

        this.skillID = skillID;
        name = newName;
        description = newDescr;
        totalExp = newTotalExp;
        initializeLevelAndExp();
    }

    /*initialize the current level, expForCurLevel, and exp gained in the current level
    by extracting that info from the total experience earned */
    private void initializeLevelAndExp() {

        exp = totalExp;

        expForCurLevel = 1000;
        level = 1;

        while( exp >= expForCurLevel ) {
            exp -= expForCurLevel;
            level++;
            expForCurLevel += 1000;
        }
    }

    public Skill(Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);
        skillID = Long.parseLong(data[0]);
        name = data[1];
        description = data[2];
        totalExp = Integer.parseInt(data[3]);
        initializeLevelAndExp();
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
                Integer.toString(totalExp)});
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

    public void setTotalExp(int newExp) {

        totalExp = newExp;
    }

    public int getTotalExp() {

        return totalExp;
    }

    public long getID() {

        return skillID;
    }

    public int getLevel() {

        return level;
    }

    public int getExpForCurLevel() {

        return expForCurLevel;
    }

    public int getExpTillNextLevel() {

        return expForCurLevel - exp;
    }
}
