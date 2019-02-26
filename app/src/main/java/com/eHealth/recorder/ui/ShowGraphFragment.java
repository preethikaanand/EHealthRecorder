package com.eHealth.recorder.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eHealth.recorder.R;
import com.eHealth.recorder.dto.GraphData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by electrorobo on 4/30/16.
 */
public class ShowGraphFragment extends BaseFragment {

    private LineChart lineChart;
    private LineData lineData;

    private static ArrayList<HashMap<String, String>> hashMapArrayList;
    private static GraphData graph_data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        hashMapArrayList = (ArrayList<HashMap<String, String>>) getArguments().getSerializable("graph_data_list");
        graph_data = getArguments().getParcelable("graph_data");
        View view = inflater.inflate(R.layout.fragment_show_graph, container, false);

        lineChart = (LineChart) view.findViewById(R.id.line_chart);

        lineData = new LineData(getXAxisValues(), getDataSet());

        lineChart.setData(lineData);
        lineChart.setDescription("Blood Pressure Chart");
        lineChart.animateXY(2000, 2000);
        lineChart.invalidate();
        lineChart.setNoDataText("No data found");

        return view;
    }

    private ArrayList<ILineDataSet> getDataSet() {
        ArrayList<ILineDataSet> lineDataSets = null;

        ArrayList<Entry> lineEntries1 = new ArrayList<>();
        ArrayList<Entry> lineEntries2 = new ArrayList<>();

        for (HashMap<String, String> stringHashMap : hashMapArrayList) {
            lineEntries1.add(new Entry(Float.valueOf(stringHashMap.get("SBP")), Integer.parseInt(stringHashMap.get("DAY")) - 1));
            lineEntries2.add(new Entry(Float.valueOf(stringHashMap.get("DBP")), Integer.parseInt(stringHashMap.get("DAY")) - 1));
        }

        LineDataSet lineDataSet1 = new LineDataSet(lineEntries1, "SBP");
        lineDataSet1.setColor(Color.parseColor("#f75d59"));
        LineDataSet lineDataSet2 = new LineDataSet(lineEntries2, "DBP");
        lineDataSet2.setColor(Color.parseColor("#728c00"));

        lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);
        return lineDataSets;
    }


    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        switch (graph_data.getGraphDuration()) {
            case "Daily":
                break;
            case "Monthly":
                xAxis = getMonthlyXAxisValue();
                break;
            case "Yearly":
                break;
        }
        return xAxis;
    }

    private ArrayList<String> getMonthlyXAxisValue() {
        ArrayList<String> xAxis = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            if (i <= 9)
                xAxis.add("0" + i + "");
            else
                xAxis.add("" + i + "");
        }
        return xAxis;
    }
}
