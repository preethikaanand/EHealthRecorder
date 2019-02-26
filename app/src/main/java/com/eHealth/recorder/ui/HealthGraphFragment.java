package com.eHealth.recorder.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.eHealth.recorder.R;
import com.eHealth.recorder.config.MonthData_Info;
import com.eHealth.recorder.dto.GraphData;
import com.eHealth.recorder.parseoperation.GraphDataParseOperation;
import com.eHealth.recorder.util.GettingDateTime;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by electrorobo on 3/30/16.
 */
public class HealthGraphFragment extends BaseFragment implements GraphDataParseOperation.GraphDataListener {

    private Button button_show_graph;
    private FloatingActionButton floatingActionButton_add;
    private Spinner spinnerDiseaseTypeGraph, spinnerGraphDuration, spinnerGraphDay, spinnerGraphMonth, spinnerGraphYear;

    private HashMap<String, String> hashMap;
    private List<String> disease_type_graph_list, graph_duration_list, graph_day_list, graph_month_list, graph_year_list;
    private String selected_disease_type_graph, selected_graph_duration, selected_graph_day, selected_graph_month, selected_graph_year;
    private GraphData graphData;
    private GraphDataParseOperation graphDataParseOperation;
    private GettingDateTime gettingDateTime;

    private Fragment fragment;

    public HealthGraphFragment(){
        hashMap = new HashMap<String, String>();
    }

    public HealthGraphFragment(HashMap<String, String> hashMap){
        this.hashMap = hashMap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        graphDataParseOperation = GraphDataParseOperation.getInstance((AppCompatActivity) getActivity());
        gettingDateTime = GettingDateTime.getInstance();
        fragment = this;
        if (hashMap.isEmpty())
            hashMap.put("objectId", ParseUser.getCurrentUser().getObjectId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health_graph, container, false);

        floatingActionButton_add = (FloatingActionButton) view.findViewById(R.id.add_data_button);
        spinnerDiseaseTypeGraph = (Spinner) view.findViewById(R.id.spinner_diseaseType_graph);
        spinnerGraphDuration = (Spinner) view.findViewById(R.id.spinner_duration);
        spinnerGraphDay = (Spinner) view.findViewById(R.id.spinner_day_graph);
        spinnerGraphMonth = (Spinner) view.findViewById(R.id.spinner_month_graph);
        spinnerGraphYear = (Spinner) view.findViewById(R.id.spinner_year_graph);
        button_show_graph = (Button) view.findViewById(R.id.show_graph_button);

        if (hashMap.get("objectId").equals(ParseUser.getCurrentUser().getObjectId()))
            floatingActionButton_add.setVisibility(View.VISIBLE);
        else
            floatingActionButton_add.setVisibility(View.GONE);

        // Spinner Drop down elements
        initializeSpinnerList();

        // Creating adapter for spinner
        settingSpinnerAdapter();

        // Spinner click listener
        spinnerDiseaseTypeGraph.setOnItemSelectedListener(diseaseTypeGraphListener);
        spinnerGraphDuration.setOnItemSelectedListener(graphDurationListener);
        spinnerGraphDay.setOnItemSelectedListener(dayGraphListener);
        spinnerGraphMonth.setOnItemSelectedListener(monthGraphListener);
        spinnerGraphYear.setOnItemSelectedListener(yearGraphListener);

        button_show_graph.setOnClickListener(showGraphButtonListener);
        floatingActionButton_add.setOnClickListener(addDataListener);

        return view;
    }

    AdapterView.OnItemSelectedListener diseaseTypeGraphListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selected_disease_type_graph = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    AdapterView.OnItemSelectedListener graphDurationListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selected_graph_duration = parent.getItemAtPosition(position).toString();

            switch (selected_graph_duration) {
                case "Daily":
                    spinnerGraphDay.setVisibility(View.VISIBLE);
                    spinnerGraphMonth.setVisibility(View.VISIBLE);
                    spinnerGraphYear.setVisibility(View.VISIBLE);
                    break;
                case "Monthly":
                    spinnerGraphDay.setVisibility(View.GONE);
                    spinnerGraphMonth.setVisibility(View.VISIBLE);
                    spinnerGraphYear.setVisibility(View.VISIBLE);
                    break;
                case "Yearly":
                    spinnerGraphDay.setVisibility(View.GONE);
                    spinnerGraphMonth.setVisibility(View.GONE);
                    spinnerGraphYear.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    AdapterView.OnItemSelectedListener dayGraphListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selected_graph_day = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    AdapterView.OnItemSelectedListener monthGraphListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getItemAtPosition(position).equals("Choose month")) {
                selected_graph_month = parent.getItemAtPosition(position).toString();
            } else {
                selected_graph_month = MonthData_Info.getKeyFromValue(MonthData_Info.getMonthData(), parent.getItemAtPosition(position).toString());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    AdapterView.OnItemSelectedListener yearGraphListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selected_graph_year = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener showGraphButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (selected_disease_type_graph.equals("Choose disease type")) {
                Toast.makeText(getContext(), "Choose disease type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selected_graph_duration.equals("Choose duration")) {
                Toast.makeText(getContext(), "Choose duration", Toast.LENGTH_SHORT).show();
                return;
            } else if (selected_graph_duration.equals("Daily")) {
                if (selected_graph_day.equals("Choose day")) {
                    Toast.makeText(getContext(), "Choose day", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (selected_graph_duration.equals("Monthly")) {
                if (selected_graph_year.equals("Choose year")) {
                    Toast.makeText(getContext(), "Choose year", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selected_graph_month.equals("Choose month")) {
                    Toast.makeText(getContext(), "Choose month", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (selected_graph_duration.equals("Yearly")) {
                if (selected_graph_year.equals("Choose year")) {
                    Toast.makeText(getContext(), "Choose year", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selected_graph_month.equals("Choose month")) {
                    Toast.makeText(getContext(), "Choose month", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selected_graph_day.equals("Choose day")) {
                    Toast.makeText(getContext(), "Choose day", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            graphData = new GraphData();
            graphData.setDiseaseTypeForGraph(selected_disease_type_graph);
            graphData.setGraphDuration(selected_graph_duration);
            graphData.setGraphDay(selected_graph_day);
            graphData.setGraphMonth(selected_graph_month);
            graphData.setGraphYear(selected_graph_year);
            graphData.setRecordCreationDate(gettingDateTime.getDate());
            graphData.setRegularExpression(gettingDateTime.gettingRegularExpression(graphData));
            graphData.setHashMap_data(hashMap);

            graphDataParseOperation.getGraphData(graphData, fragment);
        }
    };

    View.OnClickListener addDataListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switchToFragemnt();
        }
    };

    private void switchToFragemnt() {
        NewGraphDataFragment newGraphDataFragment = new NewGraphDataFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        // Store the Fragment in stack
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_HealthGraphFrameLayout, newGraphDataFragment).commit();
    }

    private void initializeSpinnerList() {
        disease_type_graph_list = new ArrayList<String>();
        disease_type_graph_list.add("Choose disease type");
        disease_type_graph_list.add("Blood Pressure");

        graph_duration_list = new ArrayList<>();
        graph_duration_list.add("Choose duration");
        //graph_duration_list.add("Daily");
        graph_duration_list.add("Monthly");
        //graph_duration_list.add("Yearly");

        graph_day_list = new ArrayList<>();
        graph_day_list.add("Choose day");
        for (int i = 1; i <= 31; i++) {
            if (i <= 9)
                graph_day_list.add("0" + i + "");
            else
                graph_day_list.add("" + i + "");
        }

        graph_month_list = new ArrayList<>();
        graph_month_list.add("Choose month");
        for (String monthData : MonthData_Info.month) {
            graph_month_list.add(monthData);
        }

        graph_year_list = new ArrayList<>();
        graph_year_list.add("Choose year");
        graph_year_list.add("2016");
    }

    private void settingSpinnerAdapter() {
        ArrayAdapter<String> diseaseTypeGraphDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, disease_type_graph_list);
        diseaseTypeGraphDataAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);

        ArrayAdapter<String> graphDurationDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, graph_duration_list);
        graphDurationDataAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);

        ArrayAdapter<String> dayGraphDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, graph_day_list);
        dayGraphDataAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);

        ArrayAdapter<String> monthGraphDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, graph_month_list);
        monthGraphDataAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);

        ArrayAdapter<String> yearGraphDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, graph_year_list);
        yearGraphDataAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);

        // attaching data adapter to spinner
        spinnerDiseaseTypeGraph.setAdapter(diseaseTypeGraphDataAdapter);
        spinnerGraphDuration.setAdapter(graphDurationDataAdapter);
        spinnerGraphDay.setAdapter(dayGraphDataAdapter);
        spinnerGraphMonth.setAdapter(monthGraphDataAdapter);
        spinnerGraphYear.setAdapter(yearGraphDataAdapter);
    }

    @Override
    public void savedGraphDataList(ArrayList<HashMap<String, String>> graphDataList, GraphData graphData) {
        enterNextFragment(graphDataList, graphData);
    }

    private void enterNextFragment(ArrayList<HashMap<String, String>> graphDataList, GraphData graphData) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("graph_data_list", graphDataList);
        bundle.putParcelable("graph_data", graphData);

        ShowGraphFragment showGraphFragment = new ShowGraphFragment();
        showGraphFragment.setArguments(bundle);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        // Store the Fragment in stack
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_HealthGraphFrameLayout, showGraphFragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView() {
        spinnerDiseaseTypeGraph.setSelection(0, false);
        spinnerGraphDuration.setSelection(0);
        spinnerGraphDay.setVisibility(View.GONE);
        spinnerGraphDay.setSelection(0);
        spinnerGraphMonth.setVisibility(View.GONE);
        spinnerGraphMonth.setSelection(0);
        spinnerGraphYear.setVisibility(View.GONE);
        spinnerGraphYear.setSelection(0);
    }
}
