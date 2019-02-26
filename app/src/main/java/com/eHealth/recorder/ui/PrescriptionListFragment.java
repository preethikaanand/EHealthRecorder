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
import com.eHealth.recorder.adapters.SavedPrescriptionList_CustomAdapter;
import com.eHealth.recorder.parseoperation.MedicalPrescriptionParseOperation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by electrorobo on 3/30/16.
 */
public class PrescriptionListFragment extends BaseFragment implements MedicalPrescriptionParseOperation.MedicalPrescriptionListener, SavedPrescriptionList_CustomAdapter.SavedPrescriptionListListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SavedPrescriptionList_CustomAdapter mAdapter;
    private TextView textView_noPrescriptionData;

    private MedicalPrescriptionParseOperation medicalPrescriptionParseOperation;
    private HashMap<String, String> hashMap;

    public PrescriptionListFragment(){
        hashMap = new HashMap<String, String>();
    }

    public  PrescriptionListFragment(HashMap<String, String> hashMap){
        this.hashMap = hashMap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        medicalPrescriptionParseOperation = MedicalPrescriptionParseOperation.getInstance((AppCompatActivity) getActivity(), getContext());
        if (hashMap.isEmpty())
            hashMap.put("objectId", ParseUser.getCurrentUser().getObjectId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prescription_list, container, false);

        textView_noPrescriptionData = (TextView) view.findViewById(R.id.noSavedPrescription);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_prescription_listing);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        medicalPrescriptionParseOperation.getMedicalPrescriptionData(this, hashMap);

        return view;
    }

    @Override
    public void savedPrescriptionList(ArrayList<HashMap<String, String>> medicalPrescriptionList) {
        if (medicalPrescriptionList.isEmpty()) {
            textView_noPrescriptionData.setVisibility(View.VISIBLE);
        } else {
            textView_noPrescriptionData.setVisibility(View.GONE);
        }

        mAdapter = new SavedPrescriptionList_CustomAdapter((AppCompatActivity) getActivity(), medicalPrescriptionList, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPrescriptionClick(HashMap<String, String> hashMap) {
        enterNextFragment(hashMap);
    }

    private void enterNextFragment(HashMap<String, String> hashMap) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("selected_prescription_data", hashMap);

        SavedPrescriptionDetailFragment savedPrescriptionDetailFragment = new SavedPrescriptionDetailFragment();
        savedPrescriptionDetailFragment.setArguments(bundle);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        // Store the Fragment in stack
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_PrescriptionListFrameLayout, savedPrescriptionDetailFragment).commit();
    }
}