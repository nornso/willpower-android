package nornso.android.willpower;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import nornso.android.willpower.adapter.ProjectItemAdapter;
import nornso.android.willpower.adapter.Project;

public class MainActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private ProjectItemAdapter adapter;
    private DrawerLayout mDrawerLayout;

    private int mSelectedId;
    private static final String SELECTED_ITEM_ID = "selected_item_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_card);
        adapter = new ProjectItemAdapter(this, getData());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    public static List<Project> getData() {
        String[] titles1 = {"吃饭", "睡觉", "打豆豆", "学习", "喝水"};
        String[] titles2 = {"喝水", "吃饭", "睡觉", "打豆豆", "学习"};
        String[] id = {"titles3a1","titles3a2","titles3a2","titles3a3","titles3a5"};
        List<Project> data = new ArrayList<>();
        for (int i = 0; i < titles1.length; i++) {
            Project current = new Project();
            current.title1 = titles1[i];
            current.title2 = titles2[i];
            current.id = id[i];
            data.add(current);
        }
        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.task_list:return true;


        }
        return super.onOptionsItemSelected(item);
    }


}
