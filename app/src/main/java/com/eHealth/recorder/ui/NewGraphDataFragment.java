package com.eHealth.recorder.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.eHealth.recorder.R;
import com.eHealth.recorder.dto.GraphData;
import com.eHealth.recorder.parseoperation.GraphDataParseOperation;
import com.eHealth.recorder.util.DatePicker_Dialog_Fragment;
import com.eHealth.recorder.util.GettingDateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by electrorobo on 4/29/16.
 */
public class NewGraphDataFragment extends BaseFragment {

    private Spinner spinnerDiseaseType;
    private LinearLayout linearLayout_bloodPressure;
    private Button buttonSubmitReading;
    private EditText editTextReadingDate, editTextSBP, editTextDBP;
    private InputMethodManager inputMethodManager;

    private GettingDateTime gettingDateTime;
    private List<String> disease_type_list;
    private String selected_disease_type, text_ReadingDate, text_SBP, text_DBP;
    private GraphData graphData;
    private GraphDataParseOperation graphDataParseOperation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        graphDataParseOperation = GraphDataParseOperation.getInstance((AppCompatActivity) getActivity());
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        gettingDateTime = GettingDateTime.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_graph_data, container, false);

        spinnerDiseaseType = (Spinner) view.findViewById(R.id.spinner_disease_type);
        linearLayout_bloodPressure = (LinearLayout) view.findViewById(R.id.bloodPressure_layout);
        editTextReadingDate = (EditText) view.findViewById(R.id.editText_ReadingDate);
        editTextSBP = (EditText) view.findViewById(R.id.editText_SBP);
        editTextDBP = (EditText) view.findViewById(R.id.editText_DBP);
        buttonSubmitReading = (Button) view.findViewById(R.id.submitReading_button);

        // Spinner Drop down elements
        disease_type_list = new ArrayList<String>();
        disease_type_list.add("Choose disease type");
        disease_type_list.add("Blood Pressure");

        // Creating adapter for spinner
        ArrayAdapter<String> diseaseTypedataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, disease_type_list);
        diseaseTypedataAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        // attaching data adapter to spinner
        spinnerDiseaseType.setAdapter(diseaseTypedataAdapter);
        // Spinner click listener
        spinnerDiseaseType.setOnItemSelectedListener(diseaseTypeListener);
        buttonSubmitReading.setOnClickListener(submitReadingListener);
        editTextReadingDate.setOnClickListener(readingDateListener);

        return view;
    }

    android.view.View.OnClickListener readingDateListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            android.app.DialogFragment datePickerFragment = new DatePicker_Dialog_Fragment() {
                int yy, mm, dd;

                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    Calendar c = Calendar.getInstance();
                    c.set(year, month, day);
                    yy = year;
                    mm = month + 1;
                    dd = day;

                    if (mm < 10 && dd < 10) {
                        editTextReadingDate.setText("0" + dd + "/0" + mm + "/" + yy);
                    } else if (mm < 10 || dd < 10) {
                        if (mm < 10)
                            editTextReadingDate.setText(dd + "/0" + mm + "/" + yy);
                        else if (dd < 10)
                            editTextReadingDate.setText("0" + dd + "/" + mm + "/" + yy);
                    } else {
                        editTextReadingDate.setText(dd + "/" + mm + "/" + yy);
                    }
                }
            };
            datePickerFragment.show(getActivity().getFragmentManager(), "datePicker");
        }
    };

    AdapterView.OnItemSelectedListener diseaseTypeListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selected_disease_type = parent.getItemAtPosition(position).toString();

            switch (selected_disease_type) {
                case "Blood Pressure":
                    linearLayout_bloodPressure.setVisibility(View.VISIBLE);
                    break;
                default:
                    linearLayout_bloodPressure.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    View.OnClickListener submitReadingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            text_ReadingDate = editTextReadingDate.getText().toString();

            if (TextUtils.isEmpty(text_ReadingDate)) {
                Toast.makeText(getContext(), "Choose date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selected_disease_type.equals("Choose disease type")) {
                Toast.makeText(getContext(), "Choose disease type", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (selected_disease_type) {
                case "Blood Pressure":
                    bloodPressureGraphProcess();
                    break;
                default:
                    break;
            }

        }
    };

    private void bloodPressureGraphProcess() {
        text_SBP = editTextSBP.getText().toString();
        text_DBP = editTextDBP.getText().toString();

        if (TextUtils.isEmpty(text_SBP)) {
            Toast.makeText(getContext(), "Enter SBP", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(text_DBP)) {
            Toast.makeText(getContext(), "Enter DBP", Toast.LENGTH_SHORT).show();
            return;
        }

        graphData = new GraphData();
        graphData.setReadingDate(text_ReadingDate);
        graphData.setSBPvalue(text_SBP);
        graphData.setDBPvalue(text_DBP);
        graphData.setRecordCreationDate(gettingDateTime.getDate());

        graphDataParseOperation.putBPGraphData(graphData, this);
    }
}
