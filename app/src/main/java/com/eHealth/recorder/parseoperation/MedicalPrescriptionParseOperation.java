package com.eHealth.recorder.parseoperation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.eHealth.recorder.R;
import com.eHealth.recorder.dto.MedicalPrescription;
import com.eHealth.recorder.dto.User;
import com.eHealth.recorder.ui.DoctorProfileActivity;
import com.eHealth.recorder.ui.MedicalPrescriptionDataFragment;
import com.eHealth.recorder.util.ProgressBarHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by electrorobo on 4/19/16.
 */
public class MedicalPrescriptionParseOperation {

    private static AppCompatActivity mactivityContext;
    private static Context mcontext;
    private static MedicalPrescriptionParseOperation medicalPrescriptionParseOperation;

    private static MedicalPrescriptionListener medicalPrescriptionListener;

    private HashMap<String, String> temp;
    private ArrayList<HashMap<String, String>> prescription_data_list_map;
    final ProgressBarHelper progressBarHelper = ProgressBarHelper.getSingletonInstance();
    private Handler handler = new Handler();

    public static MedicalPrescriptionParseOperation getInstance(AppCompatActivity activityContext, Context context) {
        mcontext = context;
        mactivityContext = activityContext;
        if (medicalPrescriptionParseOperation == null) {
            medicalPrescriptionParseOperation = new MedicalPrescriptionParseOperation();
        }
        return medicalPrescriptionParseOperation;
    }

    public void patientAuthenticationProcess(final String email) {
        progressBarHelper.showProgressBarSmall("Please wait while verifying patient...", false, handler, mcontext);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", email);
        query.whereEqualTo("UserType", "Patient");
        query.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> arg0, ParseException arg1) {
                if (arg1 == null) if (arg0.isEmpty()) {
                    progressBarHelper.dismissProgressBar(handler);
                    switchToSameFragment();
                    Toast.makeText(mactivityContext, "Sorry, This patient is not registered!!!", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Boolean emailVerificationStatus;
                    for (ParseUser parseUser : arg0) {
                        emailVerificationStatus = parseUser.getBoolean("emailVerified");
                        if (emailVerificationStatus) {
                            progressBarHelper.dismissProgressBar(handler);

                            User user = new User();
                            user.setUserId(parseUser.getObjectId());
                            user.setUserEmail(parseUser.getEmail());
                            user.setUserName(parseUser.getString("Name"));
                            user.setUserDob(parseUser.getString("BirthDate"));

                            switchToFragment(user);
                        } else {
                            progressBarHelper.dismissProgressBar(handler);
                            Toast.makeText(mactivityContext, "Sorry, This patient is not verified!!!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    progressBarHelper.dismissProgressBar(handler);
                    Toast.makeText(mactivityContext, "" + arg1.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void saveMedicalPrescriptionData(MedicalPrescription medicalPrescription) {
        progressBarHelper.showProgressBarSmall("Please wait while saving prescription...", false, handler,
                mcontext);

        ParseObject parseObject = new ParseObject("medical_prescription");
        parseObject.put("DISEASE_NAME", medicalPrescription.getDiseaseName());
        parseObject.put("MEDICAL_TESTS", medicalPrescription.getMedicalTests());
        parseObject.put("PRECAUTIONS", medicalPrescription.getPrecautions());
        parseObject.put("PRESCRIPTION_END_DATE", medicalPrescription.getPrescriptionEndDate());
        parseObject.put("MEDICINE_FOR_MORNING", medicalPrescription.getMorningMedicine());
        parseObject.put("MEDICINE_FOR_NOON", medicalPrescription.getNoonMedicine());
        parseObject.put("MEDICINE_FOR_EVE", medicalPrescription.getEveMedicine());
        parseObject.put("PRESCRIBED_BY", ParseObject.createWithoutData("_User", medicalPrescription.getPrescribedBy()));
        parseObject.put("PRESCRIBED_TO", ParseObject.createWithoutData("_User", medicalPrescription.getPrescribedTo()));
        parseObject.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException arg0) {
                if (arg0 == null) {
                    progressBarHelper.dismissProgressBar(handler);
                    Toast.makeText(mcontext, "This prescription has been saved...", Toast.LENGTH_SHORT).show();
                    switchToSameFragment();
                } else {
                    progressBarHelper.dismissProgressBar(handler);
                    Toast.makeText(mcontext, arg0.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getMedicalPrescriptionData(Fragment fragment, HashMap<String, String> hashMap) {
        try {
            medicalPrescriptionListener = ((MedicalPrescriptionListener) fragment);
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement OnCardAddListener.");
        }

        temp = new HashMap<String, String>();
        prescription_data_list_map = new ArrayList<HashMap<String, String>>();

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("medical_prescription");
        parseQuery.whereEqualTo("PRESCRIBED_TO", ParseUser.createWithoutData("_User", hashMap.get("objectId")));
        parseQuery.include("PRESCRIBED_TO");
        parseQuery.include("PRESCRIBED_BY");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject parseObject : list) {
                        temp = new HashMap<String, String>();
                        temp.put("PRESCRIBED_BY", parseObject.getParseObject("PRESCRIBED_BY").getString("Name"));
                        temp.put("DISEASE_NAME", parseObject.getString("DISEASE_NAME"));
                        temp.put("MEDICAL_TESTS", parseObject.getString("MEDICAL_TESTS"));
                        temp.put("PRECAUTIONS", parseObject.getString("PRECAUTIONS"));
                        temp.put("PRESCRIPTION_END_DATE", parseObject.getString("PRESCRIPTION_END_DATE"));
                        temp.put("MEDICINE_FOR_MORNING", parseObject.getString("MEDICINE_FOR_MORNING"));
                        temp.put("MEDICINE_FOR_NOON", parseObject.getString("MEDICINE_FOR_NOON"));
                        temp.put("MEDICINE_FOR_EVE", parseObject.getString("MEDICINE_FOR_EVE"));
                        prescription_data_list_map.add(temp);
                    }
                    medicalPrescriptionListener.savedPrescriptionList(prescription_data_list_map);
                } else {
                    Toast.makeText(mactivityContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public interface MedicalPrescriptionListener {
        public void savedPrescriptionList(ArrayList<HashMap<String, String>> medicalPrescriptionList);
    }

    private void switchToFragment(User user) {
        Fragment detail = new MedicalPrescriptionDataFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userData", user);
        detail.setArguments(bundle);
        FragmentManager fragmentManager = mactivityContext.getSupportFragmentManager();

        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.docProfile_contentframe, detail).commit();
    }

    private void switchToSameFragment() {
        mactivityContext.finish();
        Intent intent = new Intent(mactivityContext, DoctorProfileActivity.class);
        mactivityContext.startActivity(intent);
    }

}
