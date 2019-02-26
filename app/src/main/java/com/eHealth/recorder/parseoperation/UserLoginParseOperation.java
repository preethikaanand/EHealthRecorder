package com.eHealth.recorder.parseoperation;

import java.util.List;

import com.eHealth.recorder.ui.DoctorProfileActivity;
import com.eHealth.recorder.ui.LoginActivity;
import com.eHealth.recorder.ui.PatientProfileActivity;
import com.eHealth.recorder.util.ProgressBarHelper;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class UserLoginParseOperation {

    private static AppCompatActivity mactivityContext;
    private static Context mcontext;
    private static UserLoginParseOperation userLoginParseOperation;

    final ProgressBarHelper progressBarHelper = ProgressBarHelper.getSingletonInstance();
    private Handler handler = new Handler();

    private UserLoginParseOperation() {
    }

    public static UserLoginParseOperation getInstance(AppCompatActivity activityContext, Context context) {
        mcontext = context;
        mactivityContext = activityContext;
        if (userLoginParseOperation == null) {
            userLoginParseOperation = new UserLoginParseOperation();
        }
        return userLoginParseOperation;
    }

    public void userLoginProcess(final String email, final String password) {
        progressBarHelper.showProgressBarSmall("Please wait while verifying you...", false, handler, mcontext);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", email);
        query.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> arg0, ParseException arg1) {
                if (arg1 == null) if (arg0.isEmpty()) {
                    progressBarHelper.dismissProgressBar(handler);
                    switchToSameFragment();
                    Toast.makeText(mactivityContext, "Sorry, You'r not registered user!!!", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Boolean emailVerificationStatus;
                    for (ParseUser parseUser : arg0) {
                        emailVerificationStatus = parseUser.getBoolean("emailVerified");
                        if (emailVerificationStatus) {
                            progressBarHelper.dismissProgressBar(handler);
                            executeLoginProcess(email, password, parseUser.getString("UserType"));
                        } else {
                            progressBarHelper.dismissProgressBar(handler);
                            Toast.makeText(mactivityContext, "Sorry, You'r not verified user!!!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    progressBarHelper.dismissProgressBar(handler);
                    Toast.makeText(mactivityContext, "" + arg1.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void executeLoginProcess(String email, String password, final String userType) {
        progressBarHelper.showProgressBarSmall("Please wait while logging in you...", false, handler, mcontext);
        ParseUser.logInInBackground(email, password, new LogInCallback() {

            @Override
            public void done(ParseUser arg0, ParseException arg1) {
                if (arg0 != null) {
                    progressBarHelper.dismissProgressBar(handler);

                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    installation.put("user", ParseUser.getCurrentUser());
                    ParsePush.subscribeInBackground("validSession");
                    installation.saveInBackground();

                    switchToActivity(userType);
                    Toast.makeText(mactivityContext, "Welcome " + arg0.getString("Name"), Toast.LENGTH_SHORT).show();
                } else {
                    progressBarHelper.dismissProgressBar(handler);
                    Toast.makeText(mactivityContext, "" + arg1.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void userResetPasswordProcess(String email) {
        progressBarHelper.showProgressBarSmall("Please wait while processing your reset password request...", false,
                handler, mcontext);
        ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {

            @Override
            public void done(ParseException arg0) {
                if (arg0 == null) {
                    progressBarHelper.dismissProgressBar(handler);
                    switchToFragment();
                    Toast.makeText(mactivityContext, "An email has been successfully sent with reset instructions.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    progressBarHelper.dismissProgressBar(handler);
                    switchToFragment();
                    Toast.makeText(mactivityContext, arg0.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void switchToActivity(String userType) {
        if (userType.equals("Patient")) {
            Intent intent = new Intent(mactivityContext, PatientProfileActivity.class);
            mactivityContext.startActivity(intent);
        } else if (userType.equals("Doctor")) {
            Intent intent = new Intent(mactivityContext, DoctorProfileActivity.class);
            mactivityContext.startActivity(intent);
        }
    }

    private void switchToFragment() {
        FragmentManager fragmentManager = ((FragmentActivity) mactivityContext).getSupportFragmentManager();
        fragmentManager.popBackStack();
        switchToSameFragment();
    }

    private void switchToSameFragment() {
        mactivityContext.finish();
        Intent intent = new Intent(mactivityContext, LoginActivity.class);
        mactivityContext.startActivity(intent);
    }
}
