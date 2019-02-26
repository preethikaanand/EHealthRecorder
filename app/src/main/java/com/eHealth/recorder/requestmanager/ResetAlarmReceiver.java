package com.eHealth.recorder.requestmanager;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.eHealth.recorder.util.AlarmSetter;

import java.util.Random;

public class ResetAlarmReceiver extends BroadcastReceiver {

	private PendingIntent morning_pendingIntent, noon_pendingIntent, eve_pendingIntent;
	private AlarmSetter alarmSetter;

	@Override
	public void onReceive(Context context, Intent intent) {
		alarmSetter = AlarmSetter.getInstance();

		Intent morning_intent = new Intent("MEDICINE_NOTIFY_RECEIVER");
		morning_intent.putExtra("medicine_reminder_time", "morning");

		Intent noon_intent = new Intent("MEDICINE_NOTIFY_RECEIVER");
		noon_intent.putExtra("medicine_reminder_time", "noon");

		Intent eve_intent = new Intent("MEDICINE_NOTIFY_RECEIVER");
		eve_intent.putExtra("medicine_reminder_time", "eve");

		morning_pendingIntent = PendingIntent.getBroadcast(context, new Random().nextInt(), morning_intent, 0);
		noon_pendingIntent = PendingIntent.getBroadcast(context, new Random().nextInt(), noon_intent, 0);
		eve_pendingIntent = PendingIntent.getBroadcast(context, new Random().nextInt(), eve_intent, 0);

		alarmSetter.settingMorningAlarm(morning_pendingIntent, context);
		alarmSetter.settingNoonAlarm(noon_pendingIntent, context);
		alarmSetter.settingEveAlarm(eve_pendingIntent, context);
	}

}
