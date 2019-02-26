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
import com.eHealth.recorder.parseoperation.UserLoginParseOperation;

/**
 * Created by electrorobo on 5/5/16.
 */
public class ResetPasswordFragment extends BaseFragment {

    private EditText editText_resetEmail;
    private Button button_reset_password;

    private InputMethodManager inputMethodManager;
    private UserLoginParseOperation userLoginParseOperation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        userLoginParseOperation = UserLoginParseOperation.getInstance((AppCompatActivity) getActivity(), getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        editText_resetEmail = (EditText) view.findViewById(R.id.edittext_Email);
        button_reset_password = (Button) view.findViewById(R.id.submitButton);

        button_reset_password.setOnClickListener(resetButtonListener);

        return view;
    }

    View.OnClickListener resetButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            String text_email_reset = editText_resetEmail.getText().toString();

            if(TextUtils.isEmpty(text_email_reset)){
                Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            userLoginParseOperation.userResetPasswordProcess(text_email_reset);
        }
    };
}
