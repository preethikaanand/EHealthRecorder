package com.eHealth.recorder.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eHealth.recorder.R;
import com.eHealth.recorder.adapters.ViewPagerAdapter;
import com.eHealth.recorder.config.OnBackPressListener;

import java.util.HashMap;

/**
 * Created by electrorobo on 6/6/16.
 */
public class NominatorProfileDataFragment extends BaseFragment implements ViewPager.OnPageChangeListener  {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private HashMap<String, String> hashMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        hashMap = (HashMap<String, String>) getArguments().getSerializable("selected_nominator_data");
        View view = inflater.inflate(R.layout.fragment_nominator_profile_data, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.nominator_profile_data_viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.nominator_profile_data_tabs);

        setUpTabLayout();

        viewPager.addOnPageChangeListener(this);

        return view;
    }

    private void setUpTabLayout() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor("#9b9b9b"), Color.parseColor("#737373"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Fragment fragment = adapter.getFragment(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        if (fragment != null) {
                            fragment.onResume();
                        }
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new PrescriptionListFragment(hashMap), "PRESCRIPTION LIST");
        adapter.addFragment(new UploadDocumentFragment(hashMap), "UPLOAD");
        adapter.addFragment(new HealthGraphFragment(hashMap), "HEALTH GRAPH");
        viewPager.setAdapter(adapter);
    }

    /**
     * Retrieve the currently visible Tab Fragment and propagate the onBackPressed callback
     *
     * @return true = if this fragment and/or one of its associates Fragment can handle the backPress
     */
    public boolean onBackPressed() {
        // currently visible tab Fragment
        OnBackPressListener currentFragment = (OnBackPressListener) adapter.getFragment(viewPager.getCurrentItem());

        if (currentFragment != null) {
            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
            adapter.getFragment(viewPager.getCurrentItem()).onResume();
            return currentFragment.onBackPressed();
        }

        // this Fragment couldn't handle the onBackPressed call
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Fragment fragment = adapter.getFragment(position);
        if (fragment != null) {
            fragment.onResume();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}
