package nornso.android.willpower.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Created by Wu on 2016/1/2.
 */
public class TestUtils extends AndroidTestCase {
    long timeMillis = System.currentTimeMillis();

    static ContentValues createProjectValues() {
        // Create a new map of values, where column names are the keys

        ContentValues testValues = new ContentValues();
        long timeMillis = System.currentTimeMillis();

        testValues.put(WillpowerContact.ProjectEntry.COLUMN_PROJECT_NAME, "写代码");
        testValues.put(WillpowerContact.ProjectEntry.COLUMN_COLOR, "#FFFFFF");
        testValues.put(WillpowerContact.ProjectEntry.COLUMN_CREATE_TIME, timeMillis);

        return testValues;
    }

    static ContentValues createAlarmValues() {
        // Create a new map of values, where column names are the keys

        ContentValues testValues = new ContentValues();
        long timeMillis = System.currentTimeMillis();

        testValues.put(WillpowerContact.AlarmEntry.COLUMN_HOUR, 12);
        testValues.put(WillpowerContact.AlarmEntry.COLUMN_MINUTES, 24);
        testValues.put(WillpowerContact.AlarmEntry.COLUMN_ENABLED, 1);
        testValues.put(WillpowerContact.AlarmEntry.COLUMN_DAYS_OF_WEEK, 96);
        testValues.put(WillpowerContact.AlarmEntry.COLUMN_CREATE_TIME, timeMillis);





        return testValues;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }
}
