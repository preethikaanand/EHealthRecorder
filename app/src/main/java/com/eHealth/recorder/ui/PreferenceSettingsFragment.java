package com.eHealth.recorder.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.eHealth.recorder.R;
import com.eHealth.recorder.util.NotificationHelper;

/**
 * Created by electrorobo on 5/3/16.
 */
public class PreferenceSettingsFragment extends PreferenceFragment {

    private SwitchPreference emergencyNotification_switchPreference;
    private EditTextPreference diseaseName_editTextPreference, emergencyContact_editTextPreference;

    private NotificationHelper notificationHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_preference_settings);
        emergencyNotification_switchPreference = (SwitchPreference) findPreference("emergency_notification");
        diseaseName_editTextPreference = (EditTextPreference) findPreference("disease_name");
        emergencyContact_editTextPreference = (EditTextPreference) findPreference("emergency_contactNo");

        emergencyNotification_switchPreference.setSummaryOn("Turn off toggle to deactivate emergency notification");
        emergencyNotification_switchPreference.setSummaryOff("Turn on toggle to activate emergency notification");

        if (emergencyNotification_switchPreference != null) {
            emergencyNotification_switchPreference.setOnPreferenceClickListener(notificationStatusClickListener);
            emergencyNotification_switchPreference.setOnPreferenceChangeListener(notificationStatusChangeListener);
        }
        notificationHelper = NotificationHelper.getInstance((AppCompatActivity) getActivity());
    }

    Preference.OnPreferenceClickListener notificationStatusClickListener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (diseaseName_editTextPreference.getText() == null || diseaseName_editTextPreference.getText().isEmpty()) {
                emergencyNotification_switchPreference.setChecked(false);
                Toast.makeText(getActivity(), "Enter disease name ", Toast.LENGTH_SHORT).show();
            }
            if (emergencyContact_editTextPreference.getText() == null || emergencyContact_editTextPreference.getText().isEmpty()) {
                emergencyNotification_switchPreference.setChecked(false);
                Toast.makeText(getActivity(), "Enter emergency contact no.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    Preference.OnPreferenceChangeListener notificationStatusChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            boolean isNotificationOn = (Boolean) newValue;
            if (isNotificationOn) {
                if (diseaseName_editTextPreference.getText() == null || diseaseName_editTextPreference.getText().isEmpty()) {
                } else if (emergencyContact_editTextPreference.getText() == null || emergencyContact_editTextPreference.getText().isEmpty()) {
                } else {
                    emergencyNotification_switchPreference.setSummaryOn("Turn off toggle to deactivate emergency notification");
                    showNotification();
                }
            } else {
                emergencyNotification_switchPreference.setSummaryOff("Turn on toggle to activate emergency notification");
                notificationHelper.hideNotification(0);
            }
            return true;
        }
    };

    private void showNotification() {
        String diseaseNameText = getResources().getString(R.string.diseaseName_notification, diseaseName_editTextPreference.getText());
        String emergencyContactNoText = getResources().getString(R.string.emergencyContactNo_notification, emergencyContact_editTextPreference.getText());

        String notification_small_conetnt = "Disease: " + diseaseName_editTextPreference.getText();
        String notification_big_conetnt = "Disease: " + diseaseName_editTextPreference.getText() + System.getProperty("line.separator") + "Emergency Contact: " + emergencyContact_editTextPreference.getText();


        Intent launchActivity = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, launchActivity, 0);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
        builder.setStyle(bigTextStyle.bigText(notification_big_conetnt).setBigContentTitle(getString(R.string.app_name)).setSummaryText("Patient Disease Information"));
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(notification_small_conetnt);
        builder.setSmallIcon(R.mipmap.ehealthrecorder_logo);
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        builder.setColor(Color.parseColor("#87cefa"));
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setContentIntent(pi);
        builder.setLights(0x0000FF, 4000, 100);
        builder.setCategory(Notification.CATEGORY_MESSAGE);

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        notificationManager.notify(0, notification);
    }
}
