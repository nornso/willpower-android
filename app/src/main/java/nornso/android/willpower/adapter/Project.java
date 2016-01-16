package nornso.android.willpower.adapter;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.util.Log;

import nornso.android.willpower.data.WillpowerContact;
import nornso.android.willpower.data.WillpowerContact.ProjectEntry;

/**
 * Created by Wu on 2015/12/19.
 */
public class Project {

    private static final int ID_INDEX = 0;
    private static final int PROJECT_NAME_INDEX = 1;
    private static final int COLOR_INDEX = 2;
    private static final int CREATE_TIME_INDEX = 3;

    private static final int COLUMN_COUNT = CREATE_TIME_INDEX + 1;
    private static final String[] QUERY_COLUMNS = {
            ProjectEntry._ID,
            ProjectEntry.COLUMN_PROJECT_NAME,
            ProjectEntry.COLUMN_COLOR,
            ProjectEntry.COLUMN_CREATE_TIME
    };

    private static final String DEFAULT_SORT_ORDER =
            ProjectEntry._ID + " ASC";

    public long id;

    public String projectName;
    public int color;
    public long createTime;


    public Project(String projectName, int color, long createTime) {
        this.projectName = projectName;
        this.color = color;
        this.createTime = createTime;
    }


    public Project(Cursor c) {
        id = c.getLong(ID_INDEX);
        projectName = c.getString(PROJECT_NAME_INDEX);
        color = c.getInt(COLOR_INDEX);
        createTime = c.getLong(CREATE_TIME_INDEX);
    }

    public static ContentValues createContentValues(Project project) {
        ContentValues values = new ContentValues(COLUMN_COUNT);

        values.put(ProjectEntry.COLUMN_PROJECT_NAME, project.projectName);
        values.put(ProjectEntry.COLUMN_COLOR, project.color);
        values.put(ProjectEntry.COLUMN_CREATE_TIME, project.createTime);
        return values;
    }


    public static Project addOrUpdateProject(ContentResolver contentResolver, Project project, long createTime) {
        ContentValues values = createContentValues(project);

        String selection = ProjectEntry.COLUMN_CREATE_TIME + "=" + createTime;

        int rowID = contentResolver.update(ProjectEntry.CONTENT_URI, values, selection, null);
        if (rowID == 0) {
            Uri uri = contentResolver.insert(ProjectEntry.CONTENT_URI, values);
            project.id = getId(uri);
        }

        return project;
    }


    public static long getId(Uri contentUri) {
        return ContentUris.parseId(contentUri);
    }


    public static CursorLoader getProjectCursorLoader(Context context) {
        return new CursorLoader(context, ProjectEntry.CONTENT_URI,
                QUERY_COLUMNS, null, null, DEFAULT_SORT_ORDER);

    }
}
