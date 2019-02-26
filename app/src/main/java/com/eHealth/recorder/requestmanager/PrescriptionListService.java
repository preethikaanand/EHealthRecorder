package com.eHealth.recorder.requestmanager;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.eHealth.recorder.parseoperation.PrescriptionServiceParseOperation;

public class PrescriptionListService extends IntentService {

	private Context context;

	private String medicine_time;

	public PrescriptionListService() {
		super("PrescriptionListService");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		medicine_time = intent.getStringExtra("reminder_time");

		context = this.getApplicationContext();
		new PrescriptionListCalculationOperation(context).execute();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
	}

	private class PrescriptionListCalculationOperation extends AsyncTask<String, String, String> {

		private boolean isInternetPresent;
		private Context mContext;
		private PrescriptionServiceParseOperation prescriptionServiceParseOperation;

		public PrescriptionListCalculationOperation(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			isInternetPresent = isConnectingToInternet(mContext);
			prescriptionServiceParseOperation = PrescriptionServiceParseOperation.getInstance();
		}

		@Override
		protected String doInBackground(String... params) {

			if (isInternetPresent) {
				prescriptionServiceParseOperation.getLastMedicalPrescriptionData(medicine_time);
			} else {
				//switchToIntentService(context);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

	}

	private boolean isConnectingToInternet(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	/*private void switchToIntentService(Context context) {
		Intent service_intent = new Intent(context, CalculatedDataService.class);
		service_intent.putExtra("internet_status", "false");
		context.startService(service_intent);
	}*/

}
