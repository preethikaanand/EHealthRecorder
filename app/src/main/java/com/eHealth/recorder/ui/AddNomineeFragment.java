package com.eHealth.recorder.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eHealth.recorder.R;
import com.eHealth.recorder.dto.Nomination;
import com.eHealth.recorder.parseoperation.NominationDataParseOperation;

/**
 * Created by electrorobo on 6/5/16.
 */
public class AddNomineeFragment extends BaseFragment {

    private EditText editText_nomineeEmail;
    private Button button_nominate;

    private NominationDataParseOperation nominationDataParseOperation;
    private InputMethodManager inputMethodManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nominationDataParseOperation = NominationDataParseOperation.getInstance((AppCompatActivity) getActivity(), getContext());
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_nominee, container, false);

        editText_nomineeEmail = (EditText) view.findViewById(R.id.editText_nomineeEmail);
        button_nominate = (Button) view.findViewById(R.id.nominate_button);

        button_nominate.setOnClickListener(nominateListener);

        return view;
    }

    View.OnClickListener nominateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            String text_nomineeEmail = editText_nomineeEmail.getText().toString();

            if(text_nomineeEmail.isEmpty()){
                Toast.makeText(getContext(), "Enter nominee email", Toast.LENGTH_SHORT).show();
                return;
            }

            Nomination nomination = new Nomination();
            nomination.setNominee(text_nomineeEmail);

            nominationDataParseOperation.putNominationData(nomination, (BaseFragment) getParentFragment());
        }
    };
}
