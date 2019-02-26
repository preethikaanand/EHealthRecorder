package com.eHealth.recorder.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.eHealth.recorder.R;
import com.eHealth.recorder.dto.User;
import com.eHealth.recorder.parseoperation.UserRegistrationParseOperation;
import com.eHealth.recorder.util.DatePicker_Dialog_Fragment;

import java.util.Calendar;

public class SignUpFragment extends BaseFragment {

    private EditText editText_email, editText_name, editText_dob, editText_contactNo, editText_address, editText_password, editText_reenter_password;
    private String text_gender, text_userType;
    private RadioGroup radioGroup_gender, radioGroup_userType;
    private RadioButton radioButton_gender, radioButton_userType;
    private Button button_signUp;
    private InputMethodManager inputMethodManager;

    private UserRegistrationParseOperation userRegistrationParseOperation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        userRegistrationParseOperation = UserRegistrationParseOperation.getInstance((AppCompatActivity) getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        editText_email = (EditText) view.findViewById(R.id.edittext_Email);
        editText_name = (EditText) view.findViewById(R.id.edittext_Name);
        editText_dob = (EditText) view.findViewById(R.id.editText_Dob);
        editText_contactNo = (EditText) view.findViewById(R.id.edittext_ContactNo);
        radioGroup_gender = (RadioGroup) view.findViewById(R.id.radioGroup_Gender);
        editText_address = (EditText) view.findViewById(R.id.edittext_Address);
        editText_password = (EditText) view.findViewById(R.id.edittext_Password);
        editText_reenter_password = (EditText) view.findViewById(R.id.edittext_ConfirmPassword);
        radioGroup_userType = (RadioGroup) view.findViewById(R.id.radioGroup_Usertype);
        button_signUp = (Button) view.findViewById(R.id.signUpButton);

        editText_dob.setOnClickListener(dobListener);
        radioGroup_gender.setOnCheckedChangeListener(genderRadioListener);
        radioGroup_userType.setOnCheckedChangeListener(userTypeRadioListener);
        button_signUp.setOnClickListener(signUpButtonListener);

        return view;
    }

    RadioGroup.OnCheckedChangeListener genderRadioListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            radioButton_gender = (RadioButton) group.findViewById(checkedId);
            if (null != radioButton_gender && checkedId > -1) {
                text_gender = radioButton_gender.getText().toString();
            }
        }
    };

    RadioGroup.OnCheckedChangeListener userTypeRadioListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            radioButton_userType = (RadioButton) group.findViewById(checkedId);
            if (null != radioButton_userType && checkedId > -1) {
                text_userType = radioButton_userType.getText().toString();
            }
        }
    };

    android.view.View.OnClickListener dobListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            DialogFragment datePickerFragment = new DatePicker_Dialog_Fragment() {
                int yy, mm, dd;

                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    Calendar c = Calendar.getInstance();
                    c.set(year, month, day);
                    yy = year;
                    mm = month + 1;
                    dd = day;

                    if (mm < 10 && dd < 10) {
                        editText_dob.setText("0" + dd + "/0" + mm + "/" + yy);
                    } else if (mm < 10 || dd < 10) {
                        if (mm < 10)
                            editText_dob.setText(dd + "/0" + mm + "/" + yy);
                        else if (dd < 10)
                            editText_dob.setText("0" + dd + "/" + mm + "/" + yy);
                    } else {
                        editText_dob.setText(dd + "/" + mm + "/" + yy);
                    }
                }
            };
            datePickerFragment.show(getActivity().getFragmentManager(), "datePicker");
        }
    };

    OnClickListener signUpButtonListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            String text_email = editText_email.getText().toString();
            String text_name = editText_name.getText().toString();
            String text_dob = editText_dob.getText().toString();
            String text_contactNo = editText_contactNo.getText().toString();
            String text_address = editText_address.getText().toString();
            String text_password = editText_password.getText().toString();
            String text_reenterPassword = editText_reenter_password.getText().toString();

            if (TextUtils.isEmpty(text_email)) {
                Toast.makeText(getActivity(), "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(text_name)) {
                Toast.makeText(getActivity(), "Enter name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(text_dob)) {
                Toast.makeText(getActivity(), "Enter birth date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(text_contactNo)) {
                Toast.makeText(getActivity(), "Enter contact number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(text_gender)) {
                Toast.makeText(getActivity(), "Choose gender", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(text_address)) {
                Toast.makeText(getActivity(), "Enter address", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(text_password)) {
                Toast.makeText(getActivity(), "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(text_reenterPassword)) {
                Toast.makeText(getActivity(), "Reenter password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!text_password.equals(text_reenterPassword)) {
                Toast.makeText(getActivity(), "Passwords doesn't match", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(text_userType)) {
                Toast.makeText(getActivity(), "Choose user type", Toast.LENGTH_SHORT).show();
                return;
            }

            User userDTO = new User();
            userDTO.setUserEmail(text_email);
            userDTO.setUserName(text_name);
            userDTO.setUserDob(text_dob);
            userDTO.setUserContactNo(text_contactNo);
            userDTO.setUserGender(text_gender);
            userDTO.setUserAddress(text_address);
            userDTO.setUserPassword(text_password);
            userDTO.setUserType(text_userType);

            userRegistrationParseOperation.userRegistrationProcess(userDTO);
        }
    };
}
