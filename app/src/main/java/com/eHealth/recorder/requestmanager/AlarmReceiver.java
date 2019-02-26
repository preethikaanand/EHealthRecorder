package com.eHealth.recorder.requestmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String reminder_time = intent.getStringExtra("medicine_reminder_time");
		intent = new Intent(context, PrescriptionListService.class);
		intent.putExtra("reminder_time", reminder_time);
		context.startService(intent);
	}

}
