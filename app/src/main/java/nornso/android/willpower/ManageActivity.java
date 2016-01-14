package nornso.android.willpower;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;
import java.util.TimeZone;

import nornso.android.willpower.adapter.Project;
import nornso.android.willpower.adapter.ProjectItemAdapter;
import nornso.android.willpower.data.WillpowerContact.ProjectEntry;

public class ManageActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView mRecyclerView;
    private ProjectItemAdapter adapter;
    private Loader mCursorLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        mCursorLoader = getLoaderManager().initLoader(0, null, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.manage_recycler_view_card);
        adapter = new ProjectItemAdapter(this, ProjectItemAdapter.MANAGE_TYPE);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                long time = Calendar.getInstance(TimeZone.getTimeZone("Etc/Greenwich")).getTimeInMillis();
                intent.putExtra(ProjectEntry.COLUMN_CREATE_TIME, time);
                intent.setClass(ManageActivity.this, ProjectSettingsActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_manage)
            return true;

        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return Project.getProjectCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }
}

