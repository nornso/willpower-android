package nornso.android.willpower.adapter;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import nornso.android.willpower.data.WillpowerContact.AlarmEntry;

/**
 * Created by Wu on 2016/1/4.
 */
public class Alarm {

    //未保存到数据库时候，Alarm ID默认为-1
    public static final long INVALID_ID = -1;

    private static final String DEFAULT_SORT_ORDER =
            AlarmEntry._ID + " ASC";

    private static final String[] QUERY_COLUMNS = {
            AlarmEntry._ID,
            AlarmEntry.COLUMN_CREATE_TIME,
            AlarmEntry.COLUMN_HOUR,
            AlarmEntry.COLUMN_MINUTES,
            AlarmEntry.COLUMN_DAYS_OF_WEEK,
            AlarmEntry.COLUMN_ENABLED,
    };

    private static final int ID_INDEX = 0;
    private static final int CREATE_TIME_INDEX = 1;
    private static final int HOUR_INDEX = 2;
    private static final int MINUTES_INDEX = 3;
    private static final int DAYS_OF_WEEK_INDEX = 4;
    private static final int ENABLED_INDEX = 5;


    private static final int COLUMN_COUNT = DAYS_OF_WEEK_INDEX + 1;


    public long createTime;
    public long id;
    public int hour;
    public int minutes;
    public boolean enabled;
    public DaysOfWeek daysOfWeek;


    public Alarm() {

    }

    public Alarm(Cursor c) {
        id = c.getLong(ID_INDEX);
        createTime = c.getLong(CREATE_TIME_INDEX);
        hour = c.getInt(HOUR_INDEX);
        minutes = c.getInt(MINUTES_INDEX);
        daysOfWeek = new DaysOfWeek(c.getInt(DAYS_OF_WEEK_INDEX));
        enabled = c.getInt(ENABLED_INDEX) == 1;
    }


    public Alarm(int hour, int minutes) {
        this.hour = hour;
        this.minutes = minutes;
        this.daysOfWeek = new DaysOfWeek(31);
    }

    public static ContentValues createContentValues(Alarm alarm) {
        ContentValues values = new ContentValues(COLUMN_COUNT);

        values.put(AlarmEntry.COLUMN_CREATE_TIME, alarm.createTime);
        values.put(AlarmEntry.COLUMN_HOUR, alarm.hour);
        values.put(AlarmEntry.COLUMN_MINUTES, alarm.minutes);
        values.put(AlarmEntry.COLUMN_DAYS_OF_WEEK, alarm.daysOfWeek.getBitSet());
        values.put(AlarmEntry.COLUMN_ENABLED, alarm.enabled ? 1 : 0);
        return values;
    }

    public static Alarm addAlarm(ContentResolver contentResolver, Alarm alarm) {
        ContentValues values = createContentValues(alarm);
        Uri uri = contentResolver.insert(AlarmEntry.CONTENT_URI, values);
        alarm.id = getId(uri);
        return alarm;
    }


    public static boolean deleteAlarm(ContentResolver contentResolver, long alarmId) {

        int deletedRows = contentResolver.delete(AlarmEntry.buildAlarmUri(alarmId), null, null);

        Log.d("Alarm_id_uri", String.valueOf(AlarmEntry.buildAlarmUri(alarmId)));
        return deletedRows == 1;
    }

    public static boolean updateAlarm(ContentResolver contentResolver, Alarm alarm) {
        if (alarm.id == Alarm.INVALID_ID) return false;
        ContentValues values = createContentValues(alarm);
        long rowsUpdated = contentResolver.update(AlarmEntry.buildAlarmUri(alarm.id), values, null, null);
        return rowsUpdated == 1;
    }

    public static boolean deleteAlarmByCreateTime(ContentResolver contentResolver, long createTime) {
        String selection = AlarmEntry.COLUMN_CREATE_TIME + "=" + createTime;
        int deletedRows = contentResolver.delete(AlarmEntry.CONTENT_URI, selection, null);


        return deletedRows >= 1;
    }


    public static long getId(Uri contentUri) {
        return ContentUris.parseId(contentUri);
    }

    public static CursorLoader getAlarmsCursorLoader(Context context, long createTime) {
        String selection = AlarmEntry.COLUMN_CREATE_TIME + "=" + createTime;
        Log.d("create_time", Long.toString(createTime));
        return new CursorLoader(context, AlarmEntry.CONTENT_URI,
                QUERY_COLUMNS, selection, null, DEFAULT_SORT_ORDER);

    }
}
