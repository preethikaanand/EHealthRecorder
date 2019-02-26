package com.eHealth.recorder.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eHealth.recorder.R;
import com.eHealth.recorder.config.OnBackPressListener;

/**
 * Created by electrorobo on 3/31/16.
 */
public class DoctorDataFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_data, container, false);

        enterNextFragment();

        return view;
    }

    private void enterNextFragment() {
        MedicalPrescriptionEntryFragment medicalPrescriptionEntryFragment = new MedicalPrescriptionEntryFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_DoctorDataFrameLayout, medicalPrescriptionEntryFragment).commit();
    }

    /**
     * Retrieve the currently visible Tab Fragment and propagate the onBackPressed callback
     *
     * @return true = if this fragment and/or one of its associates Fragment can handle the backPress
     */
    public boolean onBackPressed() {
        // currently visible tab Fragment
        OnBackPressListener currentFragment = (OnBackPressListener) getChildFragmentManager().getFragments().get(0);

        if (currentFragment != null) {
            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
            return currentFragment.onBackPressed();
        }

        // this Fragment couldn't handle the onBackPressed call
        return false;
    }
}
