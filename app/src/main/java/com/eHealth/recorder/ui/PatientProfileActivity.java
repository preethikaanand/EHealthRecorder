package com.eHealth.recorder.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.eHealth.recorder.R;
import com.eHealth.recorder.config.PreferenceData_Info;
import com.eHealth.recorder.requestmanager.AlarmStarterActivity;
import com.eHealth.recorder.util.DialogHelper;
import com.eHealth.recorder.util.PreferenceOperation;

/**
 * Created by electrorobo on 3/30/16.
 */
public class PatientProfileActivity extends BaseActivity {

    private Toolbar mToolbar;
    private FrameLayout mContentFrame;
    private View view_shadow_toolbar;
    private PatientDataFragment patientDataFragment;
    private InputMethodManager inputMethodManager;

    private PreferenceOperation preferenceOperation;

    private int mCurrentSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        preferenceOperation = PreferenceOperation.getInstance(this);

        if (preferenceOperation.getFirstFlagRun(PreferenceData_Info.KEY_PREFERENCE_MAIN)) {
            preferenceOperation.setFlagRunned(PreferenceData_Info.KEY_PREFERENCE_MAIN);
            Intent intent2 = new Intent(PatientProfileActivity.this, AlarmStarterActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
        }

        setUpToolbar();

        view_shadow_toolbar = (View) findViewById(R.id.patProfile_toolbar_shadow);

        char current_device_version = Build.VERSION.RELEASE.charAt(0);
        if (Double.parseDouble("" + current_device_version) >= 5)
            view_shadow_toolbar.setVisibility(View.GONE);

        mContentFrame = (FrameLayout) findViewById(R.id.patProfile_contentframe);

        if (savedInstanceState == null) {
            // withholding the previously created fragment from being created again
            // On orientation change, it will prevent fragment recreation
            // its necessary to reserve the fragment stack inside each tab
            showFragmentView(0);

        } else {
            // restoring the previously created fragment
            // and getting the reference
            patientDataFragment = (PatientDataFragment) getSupportFragmentManager().getFragments().get(0);
        }
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.patProfile_toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    protected void showFragmentView(int position) {
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                mCurrentSelectedPosition = 0;
                title = getString(R.string.app_name);
                patientDataFragment = new PatientDataFragment();
                break;
            default:
                break;
        }

        if (patientDataFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.patProfile_contentframe, patientDataFragment);
            fragmentTransaction.commit();
            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patient_overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_settings:
                Intent i = new Intent(this, PreferenceSettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.action_logout:
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }

                DialogHelper dialogHelper = DialogHelper.getInstance(this);
                dialogHelper.showLogoutDialog("Would you like to logout?", "Logout", "Cancel");
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Only Activity has this special callback method
     * Fragment doesn't have any onBackPressed callback
     *
     * Logic:
     * Each time when the back button is pressed, this Activity will propagate the call to the
     * container Fragment and that Fragment will propagate the call to its each tab Fragment
     * those Fragments will propagate this method call to their child Fragments and
     * eventually all the propagated calls will get back to this initial method
     *
     * If the container Fragment or any of its Tab Fragments and/or Tab child Fragments couldn't
     * handle the onBackPressed propagated call then this Activity will handle the callback itself
     */
    @Override
    public void onBackPressed() {

        if (!patientDataFragment.onBackPressed()) {
            // container Fragment or its associates couldn't handle the back pressed task
            // delegating the task to super class
            super.onBackPressed();

        } else {
            // carousel handled the back pressed task
            // do not call super
        }
    }
}
