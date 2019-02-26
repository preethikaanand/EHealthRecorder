package com.eHealth.recorder.parseoperation;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.eHealth.recorder.dto.GraphData;
import com.eHealth.recorder.ui.BaseFragment;
import com.eHealth.recorder.ui.NewGraphDataFragment;
import com.eHealth.recorder.util.ProgressBarHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by electrorobo on 4/29/16.
 */
public class GraphDataParseOperation {

    private static AppCompatActivity activityContext;
    private static GraphDataParseOperation graphDataParseOperation;

    private GraphDataListener graphDataListener;

    private HashMap<String, String> temp;
    private ArrayList<HashMap<String, String>> graph_data_list_map;
    final ProgressBarHelper progressBarHelper = ProgressBarHelper.getSingletonInstance();
    private Handler handler = new Handler();

    public static GraphDataParseOperation getInstance(AppCompatActivity activity) {
        activityContext = activity;
        if (graphDataParseOperation == null) {
            graphDataParseOperation = new GraphDataParseOperation();
        }
        return graphDataParseOperation;
    }

    public void putBPGraphData(GraphData graphData, final BaseFragment fragment) {
        progressBarHelper.showProgressBarSmall("Storing graph data for BloodPressure", false, handler, activityContext);

        ParseObject parseObject = new ParseObject("blood_pressure_graph");
        parseObject.put("READING_DATE", graphData.getReadingDate());
        parseObject.put("SYSTOLIC_BLOOD_PRESSURE", graphData.getSBPvalue());
        parseObject.put("DIASTOLIC_BLOOD_PRESSURE", graphData.getDBPvalue());
        parseObject.put("GRAPH_USER", ParseUser.getCurrentUser());
        parseObject.put("RECORD_CREATION_TIME", graphData.getRecordCreationDate());
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    progressBarHelper.dismissProgressBar(handler);
                    fragment.onBackPressed();
                    Toast.makeText(activityContext, "Graph data for BloodPressure is stored", Toast.LENGTH_LONG).show();
                } else {
                    fragment.onBackPressed();
                    Toast.makeText(activityContext, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getGraphData(GraphData graphData, Fragment fragment) {
        switch (graphData.getDiseaseTypeForGraph()) {
            case "Blood Pressure":
                getBPGraphData(graphData, fragment);
                break;
        }
    }

    private void getBPGraphData(final GraphData graphData, Fragment fragment) {
        try {
            graphDataListener = ((GraphDataListener) fragment);
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement OnCardAddListener.");
        }

        temp = new HashMap<String, String>();
        graph_data_list_map = new ArrayList<HashMap<String, String>>();

        progressBarHelper.showProgressBarSmall("Please Wait while getting BP graph data..", false, handler, activityContext);

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("blood_pressure_graph");
        parseQuery.whereEqualTo("GRAPH_USER", ParseObject.createWithoutData("_User", graphData.getHashMap_data().get("objectId")));
        parseQuery.whereMatches("READING_DATE", graphData.getRegularExpression());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject parseObject : list) {
                        String[] date_array = parseObject.getString("READING_DATE").split("/");
                        temp = new HashMap<String, String>();
                        temp.put("DAY", date_array[0]);
                        temp.put("MONTH", date_array[1]);
                        temp.put("YEAR", date_array[2]);
                        temp.put("SBP", parseObject.getString("SYSTOLIC_BLOOD_PRESSURE"));
                        temp.put("DBP", parseObject.getString("DIASTOLIC_BLOOD_PRESSURE"));
                        graph_data_list_map.add(temp);
                    }
                    graphDataListener.savedGraphDataList(graph_data_list_map, graphData);
                    progressBarHelper.dismissProgressBar(handler);
                } else {
                    progressBarHelper.dismissProgressBar(handler);
                    Toast.makeText(activityContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public interface GraphDataListener {
        public void savedGraphDataList(ArrayList<HashMap<String, String>> graphDataList, GraphData graphData);
    }

}
