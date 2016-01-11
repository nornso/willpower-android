package nornso.android.willpower;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;
import java.util.TimeZone;

import nornso.android.willpower.data.WillpowerContact.ProjectEntry;

public class ManageActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);


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
}
