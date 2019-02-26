package com.eHealth.recorder.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eHealth.recorder.R;

import java.util.HashMap;

/**
 * Created by electrorobo on 5/4/16.
 */
public class SavedPrescriptionDetailFragment extends BaseFragment {

    private TextView text_medicalTest, text_precaution, text_endDate, text_morningMedicine, text_noonMedicine, text_eveMedicine;

    private HashMap<String, String> hashMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        hashMap = (HashMap<String, String>) getArguments().getSerializable("selected_prescription_data");
        View view = inflater.inflate(R.layout.fragment_saved_prescription_detail, container, false);

        text_medicalTest = (TextView) view.findViewById(R.id.medicalTestText);
        text_precaution = (TextView) view.findViewById(R.id.precautionText);
        text_endDate = (TextView) view.findViewById(R.id.endDateText);
        text_morningMedicine = (TextView) view.findViewById(R.id.morningMedicineText);
        text_noonMedicine = (TextView) view.findViewById(R.id.noonMedicineText);
        text_eveMedicine = (TextView) view.findViewById(R.id.eveMedicineText);

        showPrescriptionData();

        return view;
    }

    private void showPrescriptionData() {
        String medicalTestText = getContext().getResources().getString(R.string.medicalTestHeading, hashMap.get("MEDICAL_TESTS"));
        text_medicalTest.setText(Html.fromHtml(medicalTestText));

        String precautionText = getContext().getResources().getString(R.string.precautionHeading, hashMap.get("PRECAUTIONS"));
        text_precaution.setText(Html.fromHtml(precautionText));

        String end_DateText = getContext().getResources().getString(R.string.endDateHeading, hashMap.get("PRESCRIPTION_END_DATE"));
        text_endDate.setText(Html.fromHtml(end_DateText));

        String morningMedText = getContext().getResources().getString(R.string.morningMedicineHeading, hashMap.get("MEDICINE_FOR_MORNING"));
        text_morningMedicine.setText(Html.fromHtml(morningMedText));

        String noonMedText = getContext().getResources().getString(R.string.noonMedicineHeading, hashMap.get("MEDICINE_FOR_NOON"));
        text_noonMedicine.setText(Html.fromHtml(noonMedText));

        String eveMedText = getContext().getResources().getString(R.string.eveMedicineHeading, hashMap.get("MEDICINE_FOR_EVE"));
        text_eveMedicine.setText(Html.fromHtml(eveMedText));
    }
}
