package com.eHealth.recorder.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.eHealth.recorder.R;

/**
 * Created by electrorobo on 5/3/16.
 */
public class PreferenceSettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private View view_shadow_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_settings);

        view_shadow_toolbar = (View) findViewById(R.id.settings_toolbar_shadow);

        char current_device_version = Build.VERSION.RELEASE.charAt(0);
        if (Double.parseDouble("" + current_device_version) >= 5)
            view_shadow_toolbar.setVisibility(View.GONE);

        setUpToolbar();

        addPreferenceSettingsFragment();
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void addPreferenceSettingsFragment() {
        getFragmentManager().beginTransaction().replace(R.id.settings_contentframe, new PreferenceSettingsFragment())
                .commit();
    }
}
