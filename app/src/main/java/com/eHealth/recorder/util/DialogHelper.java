package com.eHealth.recorder.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.eHealth.recorder.R;
import com.eHealth.recorder.ui.LoginActivity;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;

public class DialogHelper {

    private static DialogHelper fragmentOpration;
    private static AppCompatActivity activity;

    private NotificationHelper notificationHelper;

    private DialogHelper(final AppCompatActivity context) {
        notificationHelper = NotificationHelper.getInstance(context);
    }

    public static DialogHelper getInstance(final AppCompatActivity context) {
        activity = context;
        if (fragmentOpration == null) {
            fragmentOpration = new DialogHelper(context);
        }
        return fragmentOpration;
    }

    /**
     * Method to show dialog on the basis of different dialog ids.
     */
    public void showDialog(final String message, final String positiveMessage, final String negativeMessage) {
        AlertDialog mAlertDialog = null;
        AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
        dlg.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH && event.getRepeatCount() == 0) {
                    return true;
                }
                return false;
            }
        });

        dlg/*
             * .setIcon(R.drawable.ic_launcher) .setTitle(R.string.app_name)
			 */
                .setMessage(message).setCancelable(false)
                .setPositiveButton(positiveMessage, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                }).setNegativeButton(negativeMessage, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
        mAlertDialog = dlg.create();
        mAlertDialog.show();
    }

    /**
     * Method to show dialog on the basis of different dialog ids.
     */
    public void showLogoutDialog(final String message, final String positiveMessage, final String negativeMessage) {
        AlertDialog mAlertDialog = null;
        AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
        dlg.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH && event.getRepeatCount() == 0) {
                    return true;
                }
                return false;
            }
        });

        dlg.setMessage(message).setCancelable(false)
                .setPositiveButton(positiveMessage, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        ParseUser.logOutInBackground(new LogOutCallback() {

                            @Override
                            public void done(ParseException arg0) {
                                if (arg0 == null) {
                                    dialog.dismiss();

                                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                    installation.remove("user");
                                    ParsePush.unsubscribeInBackground("validSession");
                                    installation.saveInBackground();

                                    notificationHelper.hideAllNotification();
                                    PreferenceManager.getDefaultSharedPreferences(activity).edit().clear().commit();

                                    Toast.makeText(activity, "You have logged out", Toast.LENGTH_SHORT).show();
                                    backToHome();
                                } else {
                                    Toast.makeText(activity, arg0.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }).setNegativeButton(negativeMessage, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }

        mAlertDialog = dlg.create();
        mAlertDialog.show();
    }

    private void backToHome() {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
