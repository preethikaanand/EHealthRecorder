package com.eHealth.recorder.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by electrorobo on 3/31/16.
 */
public class EHealthRecorderApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "McG7YlUnD5y7t9upD6CugB14rew36oM48CjzEdaz", "yReezDIjzyrKDPJRDUS2IDcBhXbjhVjXO5eGKxdV");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
