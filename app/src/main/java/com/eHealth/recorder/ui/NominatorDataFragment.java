package com.eHealth.recorder.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eHealth.recorder.R;
import com.eHealth.recorder.adapters.Nominators_CustomAdapter;
import com.eHealth.recorder.parseoperation.NominationDataParseOperation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by electrorobo on 6/5/16.
 */
public class NominatorDataFragment extends BaseFragment implements NominationDataParseOperation.NominatorListener, Nominators_CustomAdapter.SavedNominatorListListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Nominators_CustomAdapter mAdapter;
    private TextView textView_noNominatorData;

    private NominationDataParseOperation nominationDataParseOperation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nominationDataParseOperation = NominationDataParseOperation.getInstance((AppCompatActivity) getActivity(), getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nominator_data, container, false);

        textView_noNominatorData = (TextView) view.findViewById(R.id.noSavedNominators);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_nominator_listing);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        nominationDataParseOperation.getNominatorData(this);

        return view;
    }

    @Override
    public void savedNominatorList(ArrayList<HashMap<String, String>> nominatorList) {
        if (nominatorList.isEmpty()) {
            textView_noNominatorData.setVisibility(View.VISIBLE);
        } else {
            textView_noNominatorData.setVisibility(View.GONE);
        }

        mAdapter = new Nominators_CustomAdapter((AppCompatActivity) getActivity(), nominatorList, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onNominatorClick(HashMap<String, String> hashMap) {
        nominatorProfileData(hashMap);
    }

    private void nominatorProfileData(HashMap<String, String> hashMap){
            Bundle bundle = new Bundle();
            bundle.putSerializable("selected_nominator_data", hashMap);

            NominatorProfileDataFragment nominatorProfileDataFragment = new NominatorProfileDataFragment();
            nominatorProfileDataFragment.setArguments(bundle);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

            // Store the Fragment in stack
            transaction.addToBackStack(null);
            transaction.replace(R.id.fragment_NominatorFrameLayout, nominatorProfileDataFragment).commit();
    }
}
