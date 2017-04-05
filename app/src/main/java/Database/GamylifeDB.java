package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Atom on 04/03/2017.
 */

public final class GamylifeDB {

    private GamylifeDB() {}

    /*Inner class that defines the Skills table contents*/
    public static class GamylifeSkillEntry implements BaseColumns {

        public static final String TABLE_NAME = "Skills";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_EXPERIENCE = "Experience";
    }

    /*Inner class that defines the Skills table contents TBC*/
    public static class GamylifeQuestEntry implements  BaseColumns {

        public static final String TABLE_NAME = "Quests";
        public static final String COLUMN_NAME_NAME = "Name";

    }
}

