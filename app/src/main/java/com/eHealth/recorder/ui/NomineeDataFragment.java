package com.eHealth.recorder.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eHealth.recorder.R;
import com.eHealth.recorder.adapters.Nominees_CustomAdapter;
import com.eHealth.recorder.parseoperation.NominationDataParseOperation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by electrorobo on 6/5/16.
 */
public class NomineeDataFragment extends BaseFragment implements NominationDataParseOperation.NomineeListener{

    private FloatingActionButton floatingActionButton_addNominee;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Nominees_CustomAdapter mAdapter;
    private TextView textView_noNomineeData;

    private NominationDataParseOperation nominationDataParseOperation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nominationDataParseOperation = NominationDataParseOperation.getInstance((AppCompatActivity) getActivity(), getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nominee_data, container, false);

        floatingActionButton_addNominee = (FloatingActionButton) view.findViewById(R.id.addNominee_button);
        textView_noNomineeData = (TextView) view.findViewById(R.id.noSavedNominees);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_nominee_listing);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        nominationDataParseOperation.getNomineeData(this);

        floatingActionButton_addNominee.setOnClickListener(addNomineeListener);

        return view;
    }

    View.OnClickListener addNomineeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switchToFragemnt();
        }
    };

    private void switchToFragemnt() {
        AddNomineeFragment addNomineeFragment = new AddNomineeFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        // Store the Fragment in stack
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_NomineeFrameLayout, addNomineeFragment).commit();
    }

    @Override
    public void savedNomineeList(ArrayList<HashMap<String, String>> nomineeList) {
        if (nomineeList.isEmpty()) {
            textView_noNomineeData.setVisibility(View.VISIBLE);
        } else {
            textView_noNomineeData.setVisibility(View.GONE);
        }

        mAdapter = new Nominees_CustomAdapter((AppCompatActivity) getActivity(), nomineeList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
