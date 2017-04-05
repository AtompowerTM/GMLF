package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Atom on 17/03/2017.
 */

public class GamylifeDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GamylifeDB.db";

    // -------------------------- CREATE TABLES ----------------------------------------------------
    private static final String SQL_CREATE_SKILLS_ENTRIES = "CREATE TABLE " +
            GamylifeDB.GamylifeSkillEntry.TABLE_NAME + " (" + GamylifeDB.GamylifeSkillEntry._ID +
            " INTEGER PRIMARY KEY," + GamylifeDB.GamylifeSkillEntry.COLUMN_NAME_NAME +
            " TEXT," + GamylifeDB.GamylifeSkillEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
            GamylifeDB.GamylifeSkillEntry.COLUMN_NAME_EXPERIENCE + " INTEGER)";

    public static final String SQL_CREATE_QUESTS_ENTRIES = "CREATE TABLE " +
            GamylifeDB.GamylifeQuestEntry.TABLE_NAME + " (" + GamylifeDB.GamylifeQuestEntry._ID +
            " INTEGER PRIMARY KEY," + GamylifeDB.GamylifeQuestEntry.COLUMN_NAME_NAME + " TEXT) ";
    // -------------------------- CREATE TABLES END ------------------------------------------------

    // ---------------------------- DELETE TABLES --------------------------------------------------
    private static String SQL_DELETE_SKILLS_ENTRIES = "DROP TABLE IF EXISTS " +
            GamylifeDB.GamylifeSkillEntry.TABLE_NAME;

    private static String SQL_DELETE_QUESTS_ENTRIES = "DROP TABLE IF EXISTS " +
            GamylifeDB.GamylifeQuestEntry.TABLE_NAME;
    // -------------------------- DELETE TABLES END ------------------------------------------------

    public GamylifeDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_SKILLS_ENTRIES);
        db.execSQL(SQL_CREATE_QUESTS_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int OldVerion, int NewVersion) {

        db.execSQL(SQL_DELETE_QUESTS_ENTRIES);
        db.execSQL(SQL_DELETE_SKILLS_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onUpgrade(db, oldVersion, newVersion);
    }
}
