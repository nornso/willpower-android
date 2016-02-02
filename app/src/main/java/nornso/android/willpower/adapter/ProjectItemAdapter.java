package nornso.android.willpower.adapter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nornso.android.willpower.ProjectSettingsActivity;
import nornso.android.willpower.R;
import nornso.android.willpower.TaskSettingsActivity;
import nornso.android.willpower.data.WillpowerContact;
import nornso.android.willpower.utils.Utils;

/**
 * Created by Wu on 2015/12/18.
 */
public class ProjectItemAdapter extends CursorRecyclerViewAdapter<ProjectItemAdapter.MyViewHolder> implements LoaderManager.LoaderCallbacks<Cursor> {
    private LayoutInflater mInflater;
    private Context mContext;
    private static TaskItemAdapter taskItemAdapter;
    public static final int PROJECT_TYPE = 0;
    public static final int MANAGE_TYPE = 1;
    private int mType;


    List<Project> mData = Collections.emptyList();
    List<Task> tasks = Collections.emptyList();


    public ProjectItemAdapter(Context context, int type) {
        super(context, null);
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mType = type;
    }

    public static List<Task> getData(long id) {
        String[] titles3a1 = {"吃饭1", "睡觉1", "打豆豆1"};
        String[] titles3a2 = {"吃饭2", "睡觉2", "打豆豆2"};
        String[] titles3a3 = {"吃饭3", "睡觉3", "打豆豆3"};
        List<Task> data = new ArrayList<>();
        String[] titles;
        switch ((int) (id % 3)) {
            case 1:
                titles = titles3a1;
                break;
            case 2:
                titles = titles3a2;
                break;
            default:
                titles = titles3a3;
        }
        for (int i = 0; i < titles.length; i++) {
            Task current = new Task();
            current.title3 = titles[i];
            data.add(current);
        }
        return data;
    }


    @Override
    public ProjectItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutID = -1;
        switch (mType) {
            case PROJECT_TYPE:
                layoutID = R.layout.card_view_project;
                break;
            case MANAGE_TYPE:
                layoutID = R.layout.card_view_project_manage;
                break;
        }


        View view = mInflater.inflate(layoutID, parent, false);


        Log.d("ddddddd", String.valueOf(layoutID));

        MyViewHolder holder = new MyViewHolder(view);
        MyLinearLayoutManager mLayoutManager = new MyLinearLayoutManager(mContext);
        holder.mChildRecyclerView.setLayoutManager(mLayoutManager);
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, Cursor cursor) {
        final Project project = new Project(cursor);
        viewHolder.projectName.setText(project.projectName);
        viewHolder.projectCardView.setCardBackgroundColor(project.color);

        taskItemAdapter = new TaskItemAdapter(mContext, getData(project.id));
        viewHolder.mChildRecyclerView.setAdapter(taskItemAdapter);
        viewHolder.title2.setText("副标题");

        if (viewHolder.editButton != null) {
            viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();

                    long time = project.createTime;
                    String text = project.projectName;
                    long color = project.color;

                    intent.putExtra(WillpowerContact.ProjectEntry.COLUMN_CREATE_TIME, time);
                    intent.putExtra(WillpowerContact.ProjectEntry.COLUMN_PROJECT_NAME, text);
                    intent.putExtra(WillpowerContact.ProjectEntry.COLUMN_COLOR,color);
                    intent.setClass(mContext, ProjectSettingsActivity.class);

                    mContext.startActivity(intent);
                }
            });
        }

        if (viewHolder.addTaskButton != null) {
            viewHolder.addTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, TaskSettingsActivity.class);

                    mContext.startActivity(intent);
                }
            });

        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView projectName;
        TextView title2;
        RecyclerView mChildRecyclerView;
        CardView projectCardView;
        boolean trigger;
        AppCompatImageButton editButton;
        AppCompatImageButton addTaskButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            projectName = (TextView) itemView.findViewById(R.id.card_text1);
            title2 = (TextView) itemView.findViewById(R.id.card_text2);
            mChildRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_card);
            projectCardView = (CardView) itemView.findViewById(R.id.project_card_view);


            editButton = (AppCompatImageButton) itemView.findViewById(R.id.edit_button);
            addTaskButton = (AppCompatImageButton) itemView.findViewById(R.id.add_task_button);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            trigger = mChildRecyclerView.getVisibility() == View.GONE ? true : false;
            if (trigger) {
                Utils.expand(mChildRecyclerView);

            } else {
                Utils.collapse(mChildRecyclerView);
            }

        }

    }
}
