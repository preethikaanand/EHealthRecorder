package com.eHealth.recorder.requestmanager;

import java.util.Iterator;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.eHealth.recorder.R;
import com.eHealth.recorder.ui.PatientProfileActivity;

public class NotificationParseReceiver extends BroadcastReceiver {

    private final String TAG = "Parse Notification";
    private String msg = "", expanded_msg = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "PUSH RECEIVED!!!");
        try {
            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.parse.Channel");
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
            Iterator itr = json.keys();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                Log.d(TAG, "..." + key + " => " + json.getString(key));
                if (key.equals("expanded_custom_data")) {
                    expanded_msg = json.getString(key);
                }
                if (key.equals("custom_data")) {
                    msg = json.getString(key);
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ehealthrecorder_logo);

        Intent launchActivity = new Intent(context.getApplicationContext(), PatientProfileActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context.getApplicationContext(), 0, launchActivity, 0);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText(msg);
        builder.setStyle(bigTextStyle.bigText(expanded_msg).setBigContentTitle(context.getString(R.string.app_name)).setSummaryText("Patient Medicine Information"));
        builder.setSmallIcon(R.mipmap.ehealthrecorder_logo);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setColor(Color.parseColor("#87cefa"));
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setLargeIcon(icon);
        builder.setContentIntent(pi);
        builder.setLights(0x0000FF, 4000, 100);
        builder.setCategory(Notification.CATEGORY_REMINDER);
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        notificationManager.notify(new Random().nextInt(), notification);
    }

}
