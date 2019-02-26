package com.eHealth.recorder.util;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;

public class AlarmSetter {

	private static AlarmSetter alarmSetter;
	private GettingDateTime gettingDateTime;
	private Calendar calendar;
	
	private AlarmSetter(){
	}
	
	public static AlarmSetter getInstance(){
		if(alarmSetter == null){
			alarmSetter = new AlarmSetter();
		}
		return alarmSetter;
	}
	
	public void settingMorningAlarm(PendingIntent pendingIntent, android.content.Context context) {
		gettingDateTime = GettingDateTime.getInstance();
		calendar = Calendar.getInstance();
		long start_time_alarm = gettingDateTime.getMorningStartOfAlarm(calendar.getTime());
		long next_repeat_time = AlarmManager.INTERVAL_DAY;
		
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(android.content.Context.ALARM_SERVICE);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, start_time_alarm, next_repeat_time, pendingIntent);
    }

	public void settingNoonAlarm(PendingIntent pendingIntent, android.content.Context context) {
		calendar = Calendar.getInstance();
		long start_time_alarm = gettingDateTime.getNoonStartOfAlarm(calendar.getTime());
		long next_repeat_time = AlarmManager.INTERVAL_DAY;

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(android.content.Context.ALARM_SERVICE);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, start_time_alarm, next_repeat_time, pendingIntent);
	}

	public void settingEveAlarm(PendingIntent pendingIntent, android.content.Context context) {
		calendar = Calendar.getInstance();
		long start_time_alarm = gettingDateTime.getEveStartOfAlarm(calendar.getTime());
		long next_repeat_time = AlarmManager.INTERVAL_DAY;

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(android.content.Context.ALARM_SERVICE);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, start_time_alarm, next_repeat_time, pendingIntent);
	}
}
