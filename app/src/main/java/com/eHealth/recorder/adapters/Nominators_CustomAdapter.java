package com.eHealth.recorder.adapters;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eHealth.recorder.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by electrorobo on 6/5/16.
 */
public class Nominators_CustomAdapter extends RecyclerView.Adapter<Nominators_CustomAdapter.ViewHolder>  {

    private AppCompatActivity appCompatActivity;
    private int position;
    private ArrayList<HashMap<String, String>> list;

    private SavedNominatorListListener savedNominatorListListener;

    public Nominators_CustomAdapter(AppCompatActivity appCompatActivity, ArrayList<HashMap<String, String>> list, Fragment fragment) {
        this.appCompatActivity = appCompatActivity;
        this.list = list;

        try {
            savedNominatorListListener = ((SavedNominatorListListener) fragment);
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement onNominatorClick.");
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
        arg0.text_NominatorName.setText(map.get("Name"));
        arg0.text_NominatorEmail.setText(map.get("email"));

        arg0.setClickListener(new ViewHolder.ClickListener() {

            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                HashMap<String, String> map = list.get(position);
                if (isLongClick) {
                    setPosition(arg0.getPosition());
                } else {
                    savedNominatorListListener.onNominatorClick(map);
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        View v = LayoutInflater.from(arg0.getContext()).inflate(R.layout.saved_nominator_recycler_view_item, arg0, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private ClickListener clickListener;
        private TextView text_NominatorName, text_NominatorEmail;

        public ViewHolder(View itemView) {
            super(itemView);

            text_NominatorName = (TextView) itemView.findViewById(R.id.NominatorNameText);
            text_NominatorEmail = (TextView) itemView.findViewById(R.id.NominatorEmailText);

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

    public interface SavedNominatorListListener {
        public void onNominatorClick(HashMap<String, String> hashMap);
    }
}
