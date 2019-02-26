package com.eHealth.recorder.util;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by electrorobo on 5/4/16.
 */
public class NotificationHelper {

    private AppCompatActivity activity;
    private static NotificationHelper notificationHelper;

    private NotificationManager notificationManager;

    private NotificationHelper(AppCompatActivity activity) {
        this.activity = activity;
        notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static NotificationHelper getInstance(AppCompatActivity activity) {
        if (notificationHelper == null) {
            notificationHelper = new NotificationHelper(activity);
        }
        return notificationHelper;
    }

    public void hideNotification(int notificationId) {
        notificationManager.cancel(notificationId);
    }

    public void hideAllNotification() {
        notificationManager.cancelAll();
    }
}
