package nornso.android.willpower.adapter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nornso.android.willpower.R;
import nornso.android.willpower.utils.Utils;

/**
 * Created by Wu on 2015/12/18.
 */
public class ProjectItemAdapter extends CursorRecyclerViewAdapter<ProjectItemAdapter.MyViewHolder> implements LoaderManager.LoaderCallbacks<Cursor> {
    private LayoutInflater mInflater;
    private Context mContext;
    private static TaskItemAdapter taskItemAdapter;


    List<Project> mData = Collections.emptyList();
    List<Task> tasks = Collections.emptyList();


    public ProjectItemAdapter(Context context) {
        super(context, null);
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public ProjectItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.card_view_project, parent, false);

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
        boolean tigger;

        public MyViewHolder(View itemView) {
            super(itemView);
            projectName = (TextView) itemView.findViewById(R.id.card_text1);
            title2 = (TextView) itemView.findViewById(R.id.card_text2);
            mChildRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_card);
            projectCardView = (CardView) itemView.findViewById(R.id.project_card_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            tigger = mChildRecyclerView.getVisibility() == View.GONE ? true : false;
            if (tigger) {
                Utils.expand(mChildRecyclerView);

            } else {
                Utils.collapse(mChildRecyclerView);
            }

        }

    }
}
