package com.eHealth.recorder.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.view.KeyEvent;

/**
 * A helper class to manage the progress bar throughout the app.
 *
 */
public class ProgressBarHelper {
	private static ProgressBarHelper mProgressBarHelper = null;
	private static ProgressDialog mProgressDialog = null;
	private static Handler mLastHandler;

	/** Method to get the singleton instance of the progress bar helper class. */
	public static ProgressBarHelper getSingletonInstance() {
		if (mProgressBarHelper == null)
			mProgressBarHelper = new ProgressBarHelper();
		return mProgressBarHelper;
	}

	/**
	 * Method to show normal progressbar that can be either cancellable or
	 * non-cancellable, this behaviour is depends upon boolean parameter named
	 * <code>isCancellable</code>.
	 */
	public void showProgressBarSmall(final int messageResID,
			final boolean isCancellable, Handler handler, final Context context) {
		String message = context.getResources().getString(messageResID);
		showProgressBarSmall(message, isCancellable, handler, context);
	}

	/**
	 * Method to show small progressbar that can be either cancellable or
	 * non-cancellable, this behaviour is depends upon boolean parameter named
	 * <code>isCancellable</code>.
	 */
	public void showProgressBarSmall(final String message,
			final boolean isCancellable, Handler handler, final Context context) {
		mLastHandler = handler;
		handler.post(new Runnable() {
			@Override
			public void run() {
				closeProgressbar();
				mProgressDialog = new ProgressDialog(context);
				mProgressDialog.setOnKeyListener(mProgressBarKeyListener);
				mProgressDialog.setCancelable(isCancellable);
				mProgressDialog.setMessage(message);
				mProgressDialog.show();
			}
		});
	}

	/** Call this method to dismiss progress bar inside the Ui thread. */
	public void dismissProgressBar(Handler handler) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				closeProgressbar();
			}
		});
	}

	/** Method to dismiss the running progress bar. */
	private void closeProgressbar() {
		try {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ProgressBar onKeyListener to prevent progress bar dismissal on Hard
	 * search key press.
	 */
	private final OnKeyListener mProgressBarKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_SEARCH
					&& event.getRepeatCount() == 0) {
				return true;
			}
			return false;
		}
	};

}