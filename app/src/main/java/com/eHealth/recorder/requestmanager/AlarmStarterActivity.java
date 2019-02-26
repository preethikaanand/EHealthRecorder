package com.eHealth.recorder.requestmanager;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import com.eHealth.recorder.ui.BaseActivity;
import com.eHealth.recorder.util.AlarmSetter;

import java.util.Random;

public class AlarmStarterActivity extends BaseActivity {

	private PendingIntent morning_pendingIntent, noon_pendingIntent, eve_pendingIntent;
	private AlarmSetter alarmSetter;

	public AlarmStarterActivity() {
		alarmSetter = AlarmSetter.getInstance();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent morning_intent = new Intent("MEDICINE_NOTIFY_RECEIVER");
		morning_intent.putExtra("medicine_reminder_time", "morning");

		Intent noon_intent = new Intent("MEDICINE_NOTIFY_RECEIVER");
		noon_intent.putExtra("medicine_reminder_time", "noon");

		Intent eve_intent = new Intent("MEDICINE_NOTIFY_RECEIVER");
		eve_intent.putExtra("medicine_reminder_time", "eve");

		morning_pendingIntent = PendingIntent.getBroadcast(AlarmStarterActivity.this, new Random().nextInt(), morning_intent, 0);
		noon_pendingIntent = PendingIntent.getBroadcast(AlarmStarterActivity.this, new Random().nextInt(), noon_intent, 0);
		eve_pendingIntent = PendingIntent.getBroadcast(AlarmStarterActivity.this, new Random().nextInt(), eve_intent, 0);

		alarmSetter.settingMorningAlarm(morning_pendingIntent, this);
		alarmSetter.settingNoonAlarm(noon_pendingIntent, this);
		alarmSetter.settingEveAlarm(eve_pendingIntent, this);
		finish();
	}

}
