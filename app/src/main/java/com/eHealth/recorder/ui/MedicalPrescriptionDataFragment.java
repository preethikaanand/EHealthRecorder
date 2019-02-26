package com.eHealth.recorder.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.eHealth.recorder.R;
import com.eHealth.recorder.dto.MedicalPrescription;
import com.eHealth.recorder.dto.User;
import com.eHealth.recorder.parseoperation.MedicalPrescriptionParseOperation;
import com.eHealth.recorder.util.DatePicker_Dialog_Fragment;
import com.parse.ParseUser;

import java.util.Calendar;

/**
 * Created by electrorobo on 4/1/16.
 */
public class MedicalPrescriptionDataFragment extends BaseFragment {

    private EditText editText_prescPatEmail, editText_prescPatName, editText_prescPatDob, editText_prescDiseaseName, editText_prescMediTests, editText_prescPrecautions, editText_prescEndDate, editText_morningMedicine, editText_noonMedicine, editText_eveMedicine;
    private Button button_prescribeIt;
    private InputMethodManager inputMethodManager;

    private User user;
    private ParseUser currentUser;
    private MedicalPrescriptionParseOperation medicalPrescriptionParseOperation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = ParseUser.getCurrentUser();
        medicalPrescriptionParseOperation = MedicalPrescriptionParseOperation.getInstance((AppCompatActivity) getActivity(), getContext());
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        user = (User) getArguments().getSerializable("userData");

        View view = inflater.inflate(R.layout.fragment_medical_prescription_data, container, false);

        editText_prescPatEmail = (EditText) view.findViewById(R.id.edittext_PrescEmail);
        editText_prescPatName = (EditText) view.findViewById(R.id.edittext_PrescName);
        editText_prescPatDob = (EditText) view.findViewById(R.id.editText_PrescDob);
        editText_prescDiseaseName = (EditText) view.findViewById(R.id.editText_PrescDisease);
        editText_prescMediTests = (EditText) view.findViewById(R.id.editText_PrescMedTests);
        editText_prescPrecautions = (EditText) view.findViewById(R.id.editText_PrescPrecaution);
        editText_prescEndDate = (EditText) view.findViewById(R.id.editText_PrescEndDate);
        editText_morningMedicine = (EditText) view.findViewById(R.id.editText_PrescMorningMedName);
        editText_noonMedicine = (EditText) view.findViewById(R.id.editText_PrescNoonMedName);
        editText_eveMedicine = (EditText) view.findViewById(R.id.editText_PrescEveMedName);
        button_prescribeIt = (Button) view.findViewById(R.id.prescibeItButton);

        editText_prescPatEmail.setText(user.getUserEmail());
        editText_prescPatEmail.setEnabled(false);
        editText_prescPatName.setText(user.getUserName());
        editText_prescPatName.setEnabled(false);
        editText_prescPatDob.setText(user.getUserDob());
        editText_prescPatDob.setEnabled(false);

        editText_prescEndDate.setOnClickListener(prescriptionEndDateListener);
        button_prescribeIt.setOnClickListener(prescribeItListener);

        return view;
    }

    android.view.View.OnClickListener prescriptionEndDateListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DialogFragment datePickerFragment = new DatePicker_Dialog_Fragment() {
                int yy, mm, dd;
                String new_mm, new_dd;

                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    Calendar c = Calendar.getInstance();
                    c.set(year, month, day);
                    yy = year;
                    mm = month + 1;
                    dd = day;

                    if (mm < 10 && dd < 10) {
                        editText_prescEndDate.setText("0" + dd + "/0" + mm + "/" + yy);
                    } else if (mm < 10 || dd < 10) {
                        if (mm < 10)
                            editText_prescEndDate.setText(dd + "/0" + mm + "/" + yy);
                        else if (dd < 10)
                            editText_prescEndDate.setText("0" + dd + "/" + mm + "/" + yy);
                    } else {
                        editText_prescEndDate.setText(dd + "/" + mm + "/" + yy);
                    }
                }
            };
            datePickerFragment.show(getActivity().getFragmentManager(), "datePicker");
        }
    };

    View.OnClickListener prescribeItListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            if (TextUtils.isEmpty(editText_prescDiseaseName.getText())) {
                Toast.makeText(getActivity(), "Enter disease name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(editText_prescPrecautions.getText())) {
                Toast.makeText(getActivity(), "Suggest precautions", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(editText_prescEndDate.getText())) {
                Toast.makeText(getActivity(), "Enter end date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(editText_morningMedicine.getText()) && TextUtils.isEmpty(editText_noonMedicine.getText()) && TextUtils.isEmpty(editText_eveMedicine.getText())) {
                Toast.makeText(getActivity(), "Suggest medicines", Toast.LENGTH_SHORT).show();
                return;
            }

            MedicalPrescription medicalPrescription = new MedicalPrescription();
            medicalPrescription.setDiseaseName(editText_prescDiseaseName.getText().toString());
            medicalPrescription.setMedicalTests(editText_prescMediTests.getText().toString());
            medicalPrescription.setPrecautions(editText_prescPrecautions.getText().toString());
            medicalPrescription.setPrescriptionEndDate(editText_prescEndDate.getText().toString());
            medicalPrescription.setMorningMedicine(editText_morningMedicine.getText().toString());
            medicalPrescription.setNoonMedicine(editText_noonMedicine.getText().toString());
            medicalPrescription.setEveMedicine(editText_eveMedicine.getText().toString());
            medicalPrescription.setPrescribedBy(currentUser.getObjectId());
            medicalPrescription.setPrescribedTo(user.getUserId());

            medicalPrescriptionParseOperation.saveMedicalPrescriptionData(medicalPrescription);
        }
    };
}
