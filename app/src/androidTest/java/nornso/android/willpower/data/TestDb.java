package nornso.android.willpower.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by Wu on 2016/1/2.
 */
public class TestDb extends AndroidTestCase {

    void deleteTheDatabase() {
        mContext.deleteDatabase(WillpowerDbHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(WillpowerContact.ProjectEntry.TABLE_NAME);
        tableNameHashSet.add(WillpowerContact.AlarmEntry.TABLE_NAME);

        //数据库是否成功创建
        mContext.deleteDatabase(WillpowerDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WillpowerDbHelper(this.mContext).getWritableDatabase();

        assertEquals(true, db.isOpen());

        //数据库table是否创建
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue(c.moveToFirst());


        //table名称是否正确
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        assertTrue(tableNameHashSet.isEmpty());


        //table的colums是否正确
        c = db.rawQuery("PRAGMA table_info(" + WillpowerContact.ProjectEntry.TABLE_NAME + ")",
                null);
        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());


        final HashSet<String> projectColumnHashSet = new HashSet<String>();
        projectColumnHashSet.add(WillpowerContact.ProjectEntry._ID);
        projectColumnHashSet.add(WillpowerContact.ProjectEntry.COLUMN_PROJECT_NAME);
        projectColumnHashSet.add(WillpowerContact.ProjectEntry.COLUMN_COLOR);
        projectColumnHashSet.add((WillpowerContact.ProjectEntry.COLUMN_CREATE_TIME));

        int columnNameIndex = c.getColumnIndex("name");

        do {
            String columnName = c.getString(columnNameIndex);
            projectColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                projectColumnHashSet.isEmpty());

        db.close();
    }


    public void testProjectTable(){
        WillpowerDbHelper dbHelper = new WillpowerDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        ContentValues testValues = TestUtils.createAlarmValues();

        long rowId;
        rowId = db.insert(WillpowerContact.AlarmEntry.TABLE_NAME, null, testValues);

        assertTrue(rowId != -1);


        Cursor cursor = db.query(WillpowerContact.AlarmEntry.TABLE_NAME,
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );


        assertTrue("Error: No Records returned from location query", cursor.moveToFirst());

        TestUtils.validateCurrentRecord("1", cursor, testValues);

        assertFalse("Error: More than one record returned from location query",
                cursor.moveToNext());

        cursor.close();
        db.close();
    }


    public void testAlarmTable(){
        WillpowerDbHelper dbHelper = new WillpowerDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        ContentValues testValues = TestUtils.createProjectValues();

        long locationRowId;
        locationRowId = db.insert(WillpowerContact.ProjectEntry.TABLE_NAME, null, testValues);

        assertTrue(locationRowId != -1);


        Cursor cursor = db.query(WillpowerContact.ProjectEntry.TABLE_NAME,
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );


        assertTrue("Error: No Records returned from location query", cursor.moveToFirst());

        TestUtils.validateCurrentRecord("1", cursor, testValues);

        assertFalse("Error: More than one record returned from location query",
                cursor.moveToNext());

        cursor.close();
        db.close();
    }




}
