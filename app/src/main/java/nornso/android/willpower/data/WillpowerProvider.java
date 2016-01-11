package nornso.android.willpower.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class WillpowerProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private WillpowerDbHelper mOpenHelper;

    static final int PROJECT = 100;
    static final int ALARM = 101;
    static final int ALARM_ID = 102;
    static final int ALARM_WITH_CREATE_TIME = 103;

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = WillpowerContact.CONTENT_AUTHORITY;
        matcher.addURI(authority, WillpowerContact.PATH_ALARM, ALARM);
        matcher.addURI(authority, WillpowerContact.PATH_PROJECT, PROJECT);
        matcher.addURI(authority, WillpowerContact.PATH_ALARM + "/*", ALARM_ID);
        matcher.addURI(authority, WillpowerContact.PATH_ALARM + "/#", ALARM_WITH_CREATE_TIME);

        return matcher;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String primaryKey;
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection) selection = "1";
        switch (match) {
            case PROJECT:
                rowsDeleted = db.delete(WillpowerContact.ProjectEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ALARM:
                rowsDeleted = db.delete(WillpowerContact.AlarmEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ALARM_ID:
                Log.d("AAAAAAAAAAAAAA","aaaaaa");
                primaryKey = uri.getLastPathSegment();
                selection = WillpowerContact.AlarmEntry._ID + "=" + primaryKey;
                rowsDeleted = db.delete(WillpowerContact.AlarmEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                Log.d("BBBBBBB","BBBBBBB");
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case PROJECT: {
                long _id = db.insert(WillpowerContact.ProjectEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = WillpowerContact.ProjectEntry.buildProjectUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case ALARM: {
                long _id = db.insert(WillpowerContact.AlarmEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = WillpowerContact.AlarmEntry.buildAlarmUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new WillpowerDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            //project
            case PROJECT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        WillpowerContact.ProjectEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            //alarm
            case ALARM: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        WillpowerContact.AlarmEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case PROJECT:
                return WillpowerContact.ProjectEntry.CONTENT_TYPE;
            case ALARM:
                return WillpowerContact.AlarmEntry.CONTENT_TYPE;
            case ALARM_ID:
                return WillpowerContact.AlarmEntry.CONTENT_ITEM_TYPE;
            case ALARM_WITH_CREATE_TIME:
                return WillpowerContact.AlarmEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated;

        switch (sUriMatcher.match(uri)) {
            case PROJECT:
                rowsUpdated = db.update(WillpowerContact.ProjectEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case ALARM:
                rowsUpdated = db.update(WillpowerContact.AlarmEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;

    }


}
