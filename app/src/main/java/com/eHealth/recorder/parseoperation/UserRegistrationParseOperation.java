package com.eHealth.recorder.parseoperation;

import com.eHealth.recorder.dto.User;
import com.eHealth.recorder.ui.LoginActivity;
import com.eHealth.recorder.util.ProgressBarHelper;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class UserRegistrationParseOperation {

    private static AppCompatActivity activityContext;
    private static UserRegistrationParseOperation userRegistrationParseOperation;

    final ProgressBarHelper progressBarHelper = ProgressBarHelper.getSingletonInstance();
    private Handler handler = new Handler();

    public static UserRegistrationParseOperation getInstance(AppCompatActivity activity) {
        activityContext = activity;
        if (userRegistrationParseOperation == null) {
            userRegistrationParseOperation = new UserRegistrationParseOperation();
        }
        return userRegistrationParseOperation;
    }

    public void userRegistrationProcess(final User userDto) {
        progressBarHelper.showProgressBarSmall("Please Wait...", false, handler, activityContext);

        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(userDto.getUserEmail());
        parseUser.setEmail(userDto.getUserEmail());
        parseUser.put("Name", userDto.getUserName());
        parseUser.put("BirthDate", userDto.getUserDob());
        parseUser.put("ContactNo", userDto.getUserContactNo());
        parseUser.put("Gender", userDto.getUserGender());
        parseUser.put("Address", userDto.getUserAddress());
        parseUser.setPassword(userDto.getUserPassword());
        parseUser.put("UserType", userDto.getUserType());

        parseUser.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(ParseException arg0) {
                if (arg0 == null) {
                    ParseUser.logOut();
                    progressBarHelper.dismissProgressBar(handler);
                    switchToFragment();
                    Toast.makeText(activityContext, "Email has been sent to your e-mail address, confirm your e-mail address", Toast.LENGTH_LONG).show();
                } else {
                    progressBarHelper.dismissProgressBar(handler);
                    Toast.makeText(activityContext, "" + arg0.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void switchToFragment() {
        FragmentManager fragmentManager = ((FragmentActivity) activityContext).getSupportFragmentManager();
        fragmentManager.popBackStack();
        switchToSameFragment();
    }

    private void switchToSameFragment() {
        activityContext.finish();
        Intent intent = new Intent(activityContext, LoginActivity.class);
        activityContext.startActivity(intent);
    }


}
