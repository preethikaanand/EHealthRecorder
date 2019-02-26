package com.eHealth.recorder.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceOperation {
	private static PreferenceOperation fragmentOpration;
	private static Context activity;

	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;

	private PreferenceOperation(final Context activity) {
		this.activity = activity;
	}

	public static PreferenceOperation getInstance(final Context activity) {
		if (fragmentOpration == null) {
			fragmentOpration = new PreferenceOperation(activity);
		}
		return fragmentOpration;
	}

	public SharedPreferences getSharedPreferences(String sharedPreferenceName) {
		sharedPreferences = activity.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE);
		return sharedPreferences;
	}

	public boolean getFirstFlagRun(String sharedPreferenceName) {
		return getSharedPreferences(sharedPreferenceName).getBoolean("First_Flag_Value", true);
	}

	public void setFlagRunned(String sharedPreferenceName) {
		editor = getSharedPreferences(sharedPreferenceName).edit();
		editor.putBoolean("First_Flag_Value", false);
		editor.commit();
	}

}
