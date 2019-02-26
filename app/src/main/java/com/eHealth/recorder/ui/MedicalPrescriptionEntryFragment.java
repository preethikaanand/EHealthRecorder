package com.eHealth.recorder.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eHealth.recorder.R;
import com.eHealth.recorder.parseoperation.MedicalPrescriptionParseOperation;

/**
 * Created by electrorobo on 3/31/16.
 */
public class MedicalPrescriptionEntryFragment extends BaseFragment {

    private EditText editText_pat_email;
    private Button button_proceed;
    private InputMethodManager inputMethodManager;

    private static final String TAG = "PrescriptionFragment";
    private MedicalPrescriptionParseOperation medicalPrescriptionParseOperation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        medicalPrescriptionParseOperation = MedicalPrescriptionParseOperation.getInstance((AppCompatActivity) getActivity(), getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medical_prescription_entry, container, false);

        editText_pat_email = (EditText) view.findViewById(R.id.edittext_PatEmail);
        button_proceed = (Button) view.findViewById(R.id.proceedButton);

        button_proceed.setOnClickListener(proceedClickListener);

        return view;
    }

    View.OnClickListener proceedClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            String text_pat_email = editText_pat_email.getText().toString();

            if (TextUtils.isEmpty(text_pat_email)) {
                Toast.makeText(getActivity(), "Enter patient's email", Toast.LENGTH_SHORT).show();
                return;
            }
            medicalPrescriptionParseOperation.patientAuthenticationProcess(text_pat_email);
        }
    };
}
