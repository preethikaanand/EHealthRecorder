package com.eHealth.recorder.parseoperation;

import org.json.JSONException;
import org.json.JSONObject;


import com.eHealth.recorder.config.PushData_Info;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class JsonDataParseOperation {

	private static JsonDataParseOperation jsonDataParseOperation;

	private JsonDataParseOperation() {
	}

	public static JsonDataParseOperation getInstance() {
		if (jsonDataParseOperation == null) {
			jsonDataParseOperation = new JsonDataParseOperation();
		}
		return jsonDataParseOperation;
	}

	// Use JSON data to send a message to a broadcast receiver
	public void sendMessageAsIntent(String Messagedata, String ExpandedMessageData) {
		ParseQuery parseQuery = ParseInstallation.getQuery();
		parseQuery.whereEqualTo("channels", "validSession");
		parseQuery.whereEqualTo("user", ParseUser.getCurrentUser());

		JSONObject data = getJSONDataMessageForIntent(Messagedata, ExpandedMessageData);
		ParsePush push = new ParsePush();
		push.setQuery(parseQuery);
		push.setData(data);
		push.sendInBackground();
	}

	private JSONObject getJSONDataMessageForIntent(String Messagedata, String ExpandedMessageData) {
		try {
			JSONObject data = new JSONObject();
			// Notice alert is not required
			// data.put("alert", "Message from Intent");
			// instead action is used
			data.put("action", PushData_Info.KEY_PUSH_DATA_ACTION);
			data.put("custom_data", Messagedata);
			data.put("expanded_custom_data", ExpandedMessageData);
			return data;
		} catch (JSONException x) {
			throw new RuntimeException("Something wrong with JSON ", x);
		}
	}
}
