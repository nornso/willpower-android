package nornso.android.willpower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class TaskSettingsActivity extends AppCompatActivity {

    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_setting);

        mToolBar = (Toolbar) findViewById(R.id.task_setting_toolbar);
        setSupportActionBar(mToolBar);
        
    }
}
