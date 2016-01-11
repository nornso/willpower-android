package nornso.android.willpower.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Wu on 2016/1/1.
 */
public class WillpowerContact {


    public static final String CONTENT_AUTHORITY = "nornso.android.willpower";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //项目表名
    public static final String PATH_PROJECT = "project";
    //提醒表名
    public static final String PATH_ALARM = "alarm";

    public static final class ProjectEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROJECT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROJECT;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROJECT;


        public static final String TABLE_NAME = "project";
        //主题名称
        public static final String COLUMN_PROJECT_NAME = "project_name";
        //主题颜色
        public static final String COLUMN_COLOR = "color";
        //创建时间
        public static final String COLUMN_CREATE_TIME = "create_time";

        public static Uri buildProjectUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class AlarmEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ALARM).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ALARM;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ALARM;

        public static final String TABLE_NAME = "alarm";


        //项目创建时间
        public static final String COLUMN_CREATE_TIME = "create_time_id";
        //闹钟时
        public static final String COLUMN_HOUR = "hour";
        //闹钟分
        public static final String COLUMN_MINUTES = "minutes";
        //闹钟每周设定
        public static final String COLUMN_DAYS_OF_WEEK = "days_of_week";
        //是否激活闹钟
        public static final String COLUMN_ENABLED = "enabled";

        public static Uri buildAlarmUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildAlarmWithCreateTime(long createTime) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(createTime)).build();
        }


    }


}
