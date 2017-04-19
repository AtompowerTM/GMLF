package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.atom.gamylife.Skill;

/**
 * Created by Atom on 04/03/2017.
 */

public final class GamylifeDB {

    private GamylifeDB() {}

    /*Inner class that defines the Skills table contents*/
    public static class GamylifeSkillEntry implements BaseColumns {

        public static final String TABLE_NAME = "Skills";
        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_EXPERIENCE = "Experience";
    }

    /*Inner class that defines the Quests table contents*/
    public static class GamylifeQuestEntry implements  BaseColumns {

        public static final String TABLE_NAME = "Quests";
        public static final String COLUMN_NAME_ID = "ID";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_EXPERIENCE = "Experience";
        public static final String COLUMN_NAME_DURATION = "Duration";
        public static final String COLUMN_NAME_SCHEDULED = "Scheduled";
        public static final String COLUMN_NAME_PARENT = "Parent";
        public static final String COLUMN_NAME_COMPLETED = "Completed";
    }

    /*Inner class that defines QuestSkill table contents*/
    public static class GamylifeQuestSkillEntry implements  BaseColumns {

        public static final String TABLE_NAME = "QuestSkill";
        public static final String COLUMN_NAME_QUEST_ID = "QuestID";
        public static final String COLUMN_NAME_SKILL_ID = "SkillID";
    }
}

