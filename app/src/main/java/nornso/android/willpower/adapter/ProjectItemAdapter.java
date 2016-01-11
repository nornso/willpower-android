package nornso.android.willpower.adapter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
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
public class ProjectItemAdapter extends RecyclerView.Adapter<ProjectItemAdapter.MyViewHolder> implements LoaderManager.LoaderCallbacks<Cursor> {
    private LayoutInflater mInflater;
    private Context mContext;
    private static TaskItemAdapter taskItemAdapter;


    List<Project> mData = Collections.emptyList();
    List<Task> tasks = Collections.emptyList();


    public ProjectItemAdapter(Context context, List<Project> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
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
    public void onBindViewHolder (ProjectItemAdapter.MyViewHolder holder, int position) {
        Project current = mData.get(position);
        holder.title1.setText(current.title1);
        holder.title2.setText(current.title2);

        taskItemAdapter = new TaskItemAdapter(mContext, getData(current.id));
        holder.mChildRecyclerView.setAdapter(taskItemAdapter);


    }


    public static List<Task> getData(String id) {
        String[] titles3a1 = {"吃饭1", "睡觉1", "打豆豆1"};
        String[] titles3a2 = {"吃饭2", "睡觉2", "打豆豆2"};
        String[] titles3a3 = {"吃饭3", "睡觉3", "打豆豆3"};
        List<Task> data = new ArrayList<>();
        String[] titles;
        switch (id){
            case "titles3a1":
                titles=titles3a1;
                break;
            case "titles3a2":
                titles=titles3a2;
                break;
            default:
                titles=titles3a3;
        }
        for (int i = 0; i < titles.length; i++) {
            Task current = new Task();
            current.title3 = titles[i];
            data.add(current);
        }
        return data;
    }

    @Override
    public int getItemCount() {
        return mData.size();
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
        TextView title1;
        TextView title2;
        RecyclerView mChildRecyclerView;

        boolean tigger;

        public MyViewHolder(View itemView) {
            super(itemView);
            title1 = (TextView) itemView.findViewById(R.id.card_text1);
            title2 = (TextView) itemView.findViewById(R.id.card_text2);
            mChildRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_card);

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
