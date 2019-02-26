package com.eHealth.recorder.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eHealth.recorder.R;
import com.eHealth.recorder.dto.GraphData;
import com.eHealth.recorder.ui.SavedPrescriptionDetailFragment;
import com.eHealth.recorder.ui.ShowGraphFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by electrorobo on 4/20/16.
 */
public class SavedPrescriptionList_CustomAdapter extends RecyclerView.Adapter<SavedPrescriptionList_CustomAdapter.ViewHolder> {

    private AppCompatActivity appCompatActivity;
    private int position;
    private ArrayList<HashMap<String, String>> list;

    private SavedPrescriptionListListener savedPrescriptionListListener;

    public SavedPrescriptionList_CustomAdapter(AppCompatActivity appCompatActivity, ArrayList<HashMap<String, String>> list, Fragment fragment) {
        this.appCompatActivity = appCompatActivity;
        this.list = list;

        try {
            savedPrescriptionListListener = ((SavedPrescriptionListListener) fragment);
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement onPrescriptionClick.");
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder arg0, final int arg1) {
        HashMap<String, String> map = list.get(arg1);
        arg0.text_diseaseName.setText(map.get("DISEASE_NAME"));

        String prescribedText = appCompatActivity.getResources().getString(R.string.prescribedByHeading, map.get("PRESCRIBED_BY"));
        arg0.text_prescribedBy.setText(Html.fromHtml(prescribedText));

        //arg0.edit_precautions.setText(map.get("PRECAUTIONS"));

        arg0.setClickListener(new ViewHolder.ClickListener() {

            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                HashMap<String, String> map = list.get(position);
                if (isLongClick) {
                    setPosition(arg0.getPosition());
                } else {
                    savedPrescriptionListListener.onPrescriptionClick(map);
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        View v = LayoutInflater.from(arg0.getContext()).inflate(R.layout.saved_prescription_recycler_view_item, arg0, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private ClickListener clickListener;
        private TextView text_diseaseName, text_prescribedBy;

        public ViewHolder(View itemView) {
            super(itemView);

            text_diseaseName = (TextView) itemView.findViewById(R.id.diseaseNameTextView);
            text_prescribedBy = (TextView) itemView.findViewById(R.id.prescribedByText);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public interface ClickListener {
            public void onClick(View v, int position, boolean isLongClick);
        }

        public void setClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(v, getPosition(), true);
            return false;
        }
    }

    public interface SavedPrescriptionListListener {
        public void onPrescriptionClick(HashMap<String, String> hashMap);
    }
}
