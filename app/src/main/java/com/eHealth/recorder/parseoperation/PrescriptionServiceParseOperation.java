package com.eHealth.recorder.parseoperation;

import android.util.Log;

import com.eHealth.recorder.util.GettingDateTime;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by electrorobo on 4/27/16.
 */
public class PrescriptionServiceParseOperation {

    private static PrescriptionServiceParseOperation prescriptionServiceParseOperation;

    private String NotificationMessage, NotificationExpandedMessage;
    private JsonDataParseOperation jsonDataParseOperation;
    private GettingDateTime gettingDateTime;

    public static PrescriptionServiceParseOperation getInstance() {
        if (prescriptionServiceParseOperation == null) {
            prescriptionServiceParseOperation = new PrescriptionServiceParseOperation();
        }
        return prescriptionServiceParseOperation;
    }

    public void getLastMedicalPrescriptionData(final String medicine_time) {
        gettingDateTime = GettingDateTime.getInstance();

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("medical_prescription");
        parseQuery.whereEqualTo("PRESCRIBED_TO", ParseObject.createWithoutData("_User", ParseUser.getCurrentUser().getObjectId()));
        parseQuery.include("PRESCRIBED_TO");
        parseQuery.include("PRESCRIBED_BY");
        parseQuery.orderByDescending("createdAt");
        parseQuery.setLimit(1);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null && !list.isEmpty()) {
                    for (ParseObject parseObject : list) {

                        try {
                            if (gettingDateTime.dateComparator(parseObject.getString("PRESCRIPTION_END_DATE")).equals("0") || gettingDateTime.dateComparator(parseObject.getString("PRESCRIPTION_END_DATE")).equals("1")) {
                                NotificationMessage = "Medicine Reminder";
                                Log.i("MEDICINE_TAG", "REACHED PARSE METHOD " + medicine_time);
                                if (medicine_time.equals("morning") && !parseObject.getString("MEDICINE_FOR_MORNING").equals("")) {
                                    NotificationExpandedMessage = "Take your morning time medicine for disease " + parseObject.getString("DISEASE_NAME") + " prescribed by Dr. " + parseObject.getParseObject("PRESCRIBED_BY").getString("Name") + System.getProperty("line.separator") + "Medicine: " + parseObject.getString("MEDICINE_FOR_MORNING");
                                } else if (medicine_time.equals("noon") && !parseObject.getString("MEDICINE_FOR_NOON").equals("")) {
                                    NotificationExpandedMessage = "Take your noon time medicine for disease " + parseObject.getString("DISEASE_NAME") + " prescribed by Dr. " + parseObject.getParseObject("PRESCRIBED_BY").getString("Name") + System.getProperty("line.separator") + "Medicine: " + parseObject.getString("MEDICINE_FOR_NOON");
                                } else if (medicine_time.equals("eve") && !parseObject.getString("MEDICINE_FOR_EVE").equals("")) {
                                    NotificationExpandedMessage = "Take your evening time medicine for disease " + parseObject.getString("DISEASE_NAME") + " prescribed by Dr. " + parseObject.getParseObject("PRESCRIBED_BY").getString("Name") + System.getProperty("line.separator") + "Medicine: " + parseObject.getString("MEDICINE_FOR_EVE");
                                }
                                jsonDataParseOperationListener();
                            }
                        } catch (java.text.ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else {
                }
            }
        });
    }

    private void jsonDataParseOperationListener() {
        jsonDataParseOperation = JsonDataParseOperation.getInstance();
        Log.i("MEDICINE_TAG", "REACHED jsondataparse listener");
        jsonDataParseOperation.sendMessageAsIntent(NotificationMessage, NotificationExpandedMessage);
    }
}
