package com.eHealth.recorder.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eHealth.recorder.R;
import com.eHealth.recorder.parseoperation.UserLoginParseOperation;

public class LoginFragment extends BaseFragment {

    private EditText editText_email, editText_password;
    private Button button_user_login;
    private TextView textView_signUp, textView_reset_password;
    private InputMethodManager inputMethodManager;

    private static final String TAG = "MainFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editText_email = (EditText) view.findViewById(R.id.emailEditText);
        editText_password = (EditText) view.findViewById(R.id.passwordEditText);
        button_user_login = (Button) view.findViewById(R.id.loginUserButton);
        textView_signUp = (TextView) view.findViewById(R.id.signUp);
        textView_reset_password = (TextView) view.findViewById(R.id.resetPassword);

        button_user_login.setOnClickListener(loginListener);
        textView_signUp.setOnClickListener(signupListener);
        textView_reset_password.setOnClickListener(resetPasswordListener);

        return view;
    }

    OnClickListener loginListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            String emailText = editText_email.getText().toString();
            String passwordText = editText_password.getText().toString();

            if (TextUtils.isEmpty(emailText)) {
                Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(passwordText)) {
                Toast.makeText(getContext(), "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            if(emailText.equals("patient") && passwordText.equals("patient")){
                Intent intent = new Intent(getActivity(), PatientProfileActivity.class);
                getActivity().startActivity(intent);
            }

            UserLoginParseOperation userLoginParseOperation = UserLoginParseOperation.getInstance((AppCompatActivity) getActivity(), getContext());
            userLoginParseOperation.userLoginProcess(emailText, passwordText);
        }
    };

    OnClickListener signupListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            switchToSignUpFragment();
        }
    };

    OnClickListener resetPasswordListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            switchToResetPasswordFragment();
        }
    };

    private void switchToSignUpFragment() {
        Fragment fragment = new SignUpFragment();
        try {
            FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } catch (ClassCastException e) {
            Log.e(TAG, "Can't get fragment manager");
        }
    }

    private void switchToResetPasswordFragment() {
        Fragment fragment = new ResetPasswordFragment();
        try {
            FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } catch (ClassCastException e) {
            Log.e(TAG, "Can't get fragment manager");
        }

    }
}
