package nornso.android.willpower;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import nornso.android.willpower.adapter.ProjectItemAdapter;
import nornso.android.willpower.adapter.Project;

public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView mRecyclerView;
    private ProjectItemAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private Loader mCursorLoader;

    private int mSelectedId;
    private static final String SELECTED_ITEM_ID = "selected_item_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCursorLoader = getLoaderManager().initLoader(0, null, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_card);
        adapter = new ProjectItemAdapter(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


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
        switch (id) {
            case R.id.task_list:
                return true;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return Project.getProjectCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        adapter.swapCursor(data);
        Log.d("ddddddddddddddddddd", String.valueOf(data.moveToFirst()));
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }
}
