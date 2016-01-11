package nornso.android.willpower.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import nornso.android.willpower.data.WillpowerContact.ProjectEntry;
import nornso.android.willpower.data.WillpowerContact.AlarmEntry;

/**
 * Created by Wu on 2016/1/1.
 */
public class WillpowerDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    long i = System.currentTimeMillis();
    static final String DATABASE_NAME = "willpower.db";

    private static void createProjectTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ProjectEntry.TABLE_NAME + " (" +
                        ProjectEntry._ID + " INTEGER PRIMARY KEY," +
                        ProjectEntry.COLUMN_PROJECT_NAME + " TEXT NOT NULL, " +
                        ProjectEntry.COLUMN_COLOR + " TEXT NOT NULL, " +
                        ProjectEntry.COLUMN_CREATE_TIME + " INTEGER UNIQUE NOT NULL); "
        );
    }

    private static void createAlarmTable (SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + AlarmEntry.TABLE_NAME + " (" +
                        AlarmEntry._ID + " INTEGER PRIMARY KEY," +
                        AlarmEntry.COLUMN_HOUR + " INTEGER NOT NULL, " +
                        AlarmEntry.COLUMN_MINUTES + " INTEGER NOT NULL, " +
                        AlarmEntry.COLUMN_DAYS_OF_WEEK + " INTEGER NOT NULL, " +
                        AlarmEntry.COLUMN_ENABLED + " INTEGER NOT NULL, " +
                        AlarmEntry.COLUMN_CREATE_TIME + " INTEGER NOT NULL); "
        );
    }

    public WillpowerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createProjectTable(db);
        createAlarmTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
