package nornso.android.willpower.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nornso.android.willpower.R;

/**
 * Created by Wu on 2015/12/21.
 */
public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.ChildViewHolder> {

    private Context mContext;
    private List<Task> mData;
    private LayoutInflater mInflater;


    public TaskItemAdapter(Context context, List<Task> data) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mData = data;
    }


    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_view_task, parent, false);
        ChildViewHolder holder = new ChildViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(ChildViewHolder holder, int position) {
        Task info = mData.get(position);
        holder.title3.setText(info.title3);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView title3;


        public ChildViewHolder(View itemView) {
            super(itemView);
            title3 = (TextView) itemView.findViewById(R.id.card_text_child);

        }
    }


}
