package nornso.android.willpower;

import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashSet;

import android.content.Loader;


import nornso.android.willpower.adapter.Alarm;
import nornso.android.willpower.adapter.CursorRecyclerViewAdapter;
import nornso.android.willpower.adapter.MyLinearLayoutManager;
import nornso.android.willpower.adapter.DaysOfWeek;
import nornso.android.willpower.adapter.Project;
import nornso.android.willpower.data.WillpowerContact.ProjectEntry;
import nornso.android.willpower.utils.ColorUtils;
import nornso.android.willpower.utils.Utils;
import nornso.android.willpower.widget.CircleView;
import nornso.android.willpower.widget.TextTime;
import nornso.android.willpower.widget.colorpicker.ColorPickerDialog;
import nornso.android.willpower.widget.colorpicker.ColorPickerSwatch;


public class ProjectSettingsActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener, ActionMenuView.OnMenuItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private EditText mProjectTitle;
    private CircleView mCircleColorView;
    private RecyclerView mRecyclerView;

    private Loader mCursorLoader = null;

    private AlarmItemAdapter mAdapter;
    private ActionMenuView mSaveMenuItem;
    private Button mAddAlarm;
    private Alarm mSelectedAlarm;


    private long mProjectCreateTime;
    private String mProjectName;
    private long mColor;


    private Toolbar mToolBar;
    private int mSelectedColor = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_setting);

        Intent intent = this.getIntent();
        mProjectCreateTime = intent.getLongExtra(ProjectEntry.COLUMN_CREATE_TIME, 0);
        mProjectName = intent.getStringExtra(ProjectEntry.COLUMN_PROJECT_NAME);
        mColor = intent.getLongExtra(ProjectEntry.COLUMN_COLOR,0);

        mCursorLoader = getLoaderManager().initLoader(0, null, this);

        initToolBar();

        //标题EditText
        mProjectTitle = (EditText) findViewById(R.id.project_title);
        mProjectTitle.addTextChangedListener(textWatcher);
        if (mProjectName != null) {
            mProjectTitle.setText(mProjectName);
        }

        //颜色选择
        mCircleColorView = (CircleView) findViewById(R.id.color_picker_button);
        if (mColor != 0) {
            mCircleColorView.setCircleColor((int) mColor);
        }

        mCircleColorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });

        mAddAlarm = (Button) findViewById(R.id.add_alarm);
        mAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();

                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        ProjectSettingsActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.show(getFragmentManager(), "ss");
            }
        });

        //闹钟
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_alarm);
        mAdapter = new AlarmItemAdapter(this, getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new MyLinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /*自定义ToolBar*/
    //初始化自定义ToolBar
    private void initToolBar() {
        mToolBar = (Toolbar) findViewById(R.id.project_setting_toolbar);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        mToolBar.setTitle(R.string.title_activity_project_settings);
        mToolBar.setNavigationIcon(R.drawable.ic_close_24dp);
        mSaveMenuItem = (ActionMenuView) findViewById(R.id.ic_confirm);
        mSaveMenuItem.setOnMenuItemClickListener(this);
        initNotSaveMenu();
    }

    //主题有内容时，MenuItem状态可以保存
    private void initSaveMenu() {
        mSaveMenuItem.getMenu().clear();
        getMenuInflater().inflate(R.menu.menu_project_save, mSaveMenuItem.getMenu());
    }

    //主题栏没内容时，MenuItem状态不可保存
    private void initNotSaveMenu() {
        mSaveMenuItem.getMenu().clear();
        getMenuInflater().inflate(R.menu.menu_project_not_save, mSaveMenuItem.getMenu());
    }


    //监听主题栏是否有字符来切换MenuItem状态
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (s.length() == 0)
                initNotSaveMenu();
            else initSaveMenu();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0)
                initNotSaveMenu();
            else initSaveMenu();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    //自定义MenuItem点击事件
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.button_save:
                Project project = new Project(mProjectTitle.getText().toString(), mCircleColorView.getCircleColor(), mProjectCreateTime);
                asyncAddProject(project,mProjectCreateTime);
                onBackPressed();
                break;
            case R.id.button_not_save:
                Snackbar.make(mSaveMenuItem, "主题名称不能为空", Snackbar.LENGTH_SHORT)
                        .setAction("action", null).show();
                break;
        }
        return true;
    }

    //返回菜单
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            asyncDeleteAlarmByCreateTime();
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    /*颜色选择器*/
    //打开颜色选择器
    public void showColorPickerDialog() {
        int[] mColor = ColorUtils.colorChoice(this);

        ColorPickerDialog colorCalendar = ColorPickerDialog.newInstance(
                R.string.color_picker_default_title, mColor,
                mSelectedColor, 5,
                ColorUtils.isTablet(this) ? ColorPickerDialog.SIZE_LARGE
                        : ColorPickerDialog.SIZE_SMALL);

        colorCalendar.setOnColorSelectedListener(colorListener);
        colorCalendar.show(getFragmentManager(), "color_pick");
    }

    ColorPickerSwatch.OnColorSelectedListener colorListener = new ColorPickerSwatch.OnColorSelectedListener() {
        @Override
        public void onColorSelected(int color) {
            mCircleColorView.setCircleColor(color);
        }
    };


    /*时间选择器*/
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        if (mSelectedAlarm == null) {
            Alarm a = new Alarm();
            a.hour = hourOfDay;
            a.minutes = minute;
            a.createTime = mProjectCreateTime;
            a.enabled = true;
            a.daysOfWeek = new DaysOfWeek(31);
            asyncAddAlarm(a);
        } else {
            mSelectedAlarm.hour = hourOfDay;
            mSelectedAlarm.minutes = minute;
            asyncUpdateAlarm(mSelectedAlarm);
            mSelectedAlarm = null;
        }

    }

    private void asyncAddProject(final Project project, final long createTime) {
        final Context context = this.getApplicationContext();

        final AsyncTask<Void, Void, Void> updateTask =
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        ContentResolver cr = context.getContentResolver();

                        Project.addOrUpdateProject(cr, project,createTime);

                        return null;
                    }
                };
        updateTask.execute();
    }




    private void asyncAddAlarm(final Alarm alarm) {
        final Context context = this.getApplicationContext();

        final AsyncTask<Void, Void, Void> updateTask =
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        ContentResolver cr = context.getContentResolver();

                        Alarm.addAlarm(cr, alarm);

                        return null;
                    }
                };
        updateTask.execute();

    }

    private void asyncDeleteAlarm(final Alarm alarm) {
        final Context context = this.getApplicationContext();
        final AsyncTask<Void, Void, Void> deleteTask =
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        ContentResolver cr = context.getContentResolver();

                        Alarm.deleteAlarm(cr, alarm.id);
                        return null;
                    }
                };
        deleteTask.execute();
    }

    private void asyncUpdateAlarm(final Alarm alarm) {
        final Context context = this.getApplicationContext();
        final AsyncTask<Void, Void, Void> deleteTask =
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        ContentResolver cr = context.getContentResolver();

                        Alarm.updateAlarm(cr, alarm);
                        return null;
                    }
                };
        deleteTask.execute();
    }

    private void asyncDeleteAlarmByCreateTime() {
        final Context context = this.getApplicationContext();
        final AsyncTask<Void, Void, Void> deleteTask =
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        ContentResolver cr = context.getContentResolver();

                        Alarm.deleteAlarmByCreateTime(cr, mProjectCreateTime);
                        return null;
                    }
                };
        deleteTask.execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return Alarm.getAlarmsCursorLoader(this, mProjectCreateTime);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    /**
     * Created by Wu on 2016/1/1.
     */
    public class AlarmItemAdapter extends CursorRecyclerViewAdapter<AlarmItemAdapter.AlarmViewHolder> {
        final LayoutInflater mFactory;
        private final Context mContext;
        private final String[] mShortWeekDayStrings;
        private final String[] mLongWeekDayStrings;

        private final Resources mResources;
        FragmentManager mFragmentManager;


        private final int[] DAY_ORDER = new int[]{
                Calendar.MONDAY,
                Calendar.TUESDAY,
                Calendar.WEDNESDAY,
                Calendar.THURSDAY,
                Calendar.FRIDAY,
                Calendar.SATURDAY,
                Calendar.SUNDAY,
        };


        public AlarmItemAdapter(Context context, FragmentManager fragmentManager) {
            super(context, null);

            mFactory = LayoutInflater.from(context);
            mContext = context;
            mFragmentManager = fragmentManager;
            mResources = mContext.getResources();

            DateFormatSymbols dfs = new DateFormatSymbols();
            mShortWeekDayStrings = Utils.getShortWeekdays();
            mLongWeekDayStrings = dfs.getWeekdays();
        }


        @Override
        public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = mFactory.inflate(R.layout.item_alarm, parent, false);
            AlarmViewHolder holder = new AlarmViewHolder(view);

            for (int i = 0; i < 7; i++) {
                final Button dayButton = (Button) mFactory.inflate(
                        R.layout.day_button, holder.repeatDays, false /* attachToRoot */);
                dayButton.setBackground(getDrawable(R.drawable.toggle_circle));

                int weight = Utils.dip2px(mContext, 42);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        weight,
                        weight);
                lp.setMargins(12, 0, 0, 0);
                dayButton.setLayoutParams(lp);


                dayButton.setText(mShortWeekDayStrings[i]);
                dayButton.setContentDescription(mLongWeekDayStrings[DAY_ORDER[i]]);
                holder.repeatDays.addView(dayButton);
                holder.dayButtons[i] = dayButton;
            }


            return holder;
        }


        @Override
        public void onBindViewHolder(final AlarmViewHolder holder, Cursor cursor) {
            final Alarm alarm = new Alarm(cursor);
            holder.alarm = alarm;

            holder.clock.setFormat((int) mContext.getResources().getDimension(R.dimen.alarm_label_size));
            holder.clock.setTime(alarm.hour, alarm.minutes);
            holder.clock.setClickable(true);
            holder.clock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedAlarm = holder.alarm;
                    Calendar now = Calendar.getInstance();

                    TimePickerDialog tpd = TimePickerDialog.newInstance(
                            ProjectSettingsActivity.this,
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            false
                    );
                    tpd.show(mFragmentManager, "ss");
                }
            });


            updateDaysOfWeekButtons(holder, alarm.daysOfWeek);
            for (int i = 0; i < 7; i++) {
                final int buttonIndex = i;

                holder.dayButtons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final boolean isActivated =
                                holder.dayButtons[buttonIndex].isActivated();
                        alarm.daysOfWeek.setDaysOfWeek(!isActivated, DAY_ORDER[buttonIndex]);
                        if (!isActivated) {
                            turnOnDayOfWeek(holder, buttonIndex);
                        } else {
                            turnOffDayOfWeek(holder, buttonIndex);
                        }
                        asyncUpdateAlarm(alarm);
                    }
                });
            }

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    asyncDeleteAlarm(alarm);
                }
            });
        }


        public class AlarmViewHolder extends RecyclerView.ViewHolder {
            TextTime clock;
            LinearLayout repeatDays;
            ImageButton delete;
            LinearLayout alarmItem;
            Button[] dayButtons = new Button[7];
            Alarm alarm;

            public AlarmViewHolder(View itemView) {
                super(itemView);

                repeatDays = (LinearLayout) itemView.findViewById(R.id.repeat_days);
                delete = (ImageButton) itemView.findViewById(R.id.delete);
                clock = (TextTime) itemView.findViewById(R.id.alarm_clock);
                alarmItem = (LinearLayout) itemView.findViewById(R.id.item_alarm);
            }

        }


        private void updateDaysOfWeekButtons(AlarmViewHolder holder, DaysOfWeek daysOfWeek) {
            HashSet<Integer> setDays = daysOfWeek.getSetDays();
            for (int i = 0; i < 7; i++) {
                if (setDays.contains(DAY_ORDER[i])) {
                    turnOnDayOfWeek(holder, i);
                } else {
                    turnOffDayOfWeek(holder, i);
                }
            }
        }

        private void turnOffDayOfWeek(AlarmViewHolder holder, int dayIndex) {
            final Button dayButton = holder.dayButtons[dayIndex];
            dayButton.setActivated(false);
            dayButton.setTextColor(mResources.getColor(R.color.colorButtonFontGray));
        }

        private void turnOnDayOfWeek(AlarmViewHolder holder, int dayIndex) {
            final Button dayButton = holder.dayButtons[dayIndex];
            dayButton.setActivated(true);
            dayButton.setTextColor(mResources.getColor(R.color.colorWhite));
        }

    }


}