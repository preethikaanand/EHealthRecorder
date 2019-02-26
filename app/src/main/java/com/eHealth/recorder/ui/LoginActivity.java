package com.eHealth.recorder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.eHealth.recorder.R;
import com.parse.ParseUser;

public class LoginActivity extends BaseActivity {

    private ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            switchToActivity();
        }
        setContentView(R.layout.activity_login);

        addLoginFragment();
    }

    private void addLoginFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LoginFragment fragment = new LoginFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment, "LOGIN");
        fragmentTransaction.commit();
    }

    private void switchToActivity() {

        if (currentUser.getString("UserType").equals("Doctor")) {
            Intent intent = new Intent(LoginActivity.this, DoctorProfileActivity.class);
            startActivity(intent);
        } else if (currentUser.getString("UserType").equals("Patient")) {
            Intent intent = new Intent(LoginActivity.this, PatientProfileActivity.class);
            startActivity(intent);
        }

    }
}
