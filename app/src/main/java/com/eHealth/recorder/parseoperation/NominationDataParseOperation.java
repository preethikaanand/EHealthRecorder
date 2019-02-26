package com.eHealth.recorder.parseoperation;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.eHealth.recorder.dto.Nomination;
import com.eHealth.recorder.ui.BaseFragment;
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
 * Created by electrorobo on 6/5/16.
 */
public class NominationDataParseOperation {

    private static AppCompatActivity mactivityContext;
    private static Context mcontext;
    private static NominationDataParseOperation nominationDataParseOperation;

    private static NominatorListener nominatorListener;
    private static NomineeListener nomineeListener;

    private HashMap<String, String> temp;
    private ArrayList<HashMap<String, String>> nominator_data_list_map, nominee_data_list_map;

    private NominationDataParseOperation() {
    }

    public static NominationDataParseOperation getInstance(AppCompatActivity activityContext, Context context) {
        mcontext = context;
        mactivityContext = activityContext;
        if (nominationDataParseOperation == null) {
            nominationDataParseOperation = new NominationDataParseOperation();
        }
        return nominationDataParseOperation;
    }

    public void putNominationData(Nomination nomination, final BaseFragment fragment){
        final ParseObject parseObject = new ParseObject("nomination_data");
        parseObject.put("NOMINATOR", ParseUser.getCurrentUser());

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("_User");
        parseQuery.whereEqualTo("username", nomination.getNominee());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){

                    parseObject.put("NOMINEE", ParseObject.createWithoutData("_User", objects.get(0).getObjectId()));
                    parseObject.saveInBackground(new SaveCallback() {

                        @Override
                        public void done(ParseException arg0) {
                            if (arg0 == null) {
                                fragment.onBackPressed();
                                Toast.makeText(mcontext, "Person has been nominated...", Toast.LENGTH_SHORT).show();
                            } else {
                                fragment.onBackPressed();
                                Toast.makeText(mcontext, arg0.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    fragment.onBackPressed();
                    Toast.makeText(mcontext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getNominatorData(Fragment fragment){

        try {
            nominatorListener = ((NominatorListener) fragment);
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement savedNominatorList.");
        }

        temp = new HashMap<String, String>();
        nominator_data_list_map = new ArrayList<HashMap<String, String>>();

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("nomination_data");
        parseQuery.whereEqualTo("NOMINEE", ParseUser.getCurrentUser());
        parseQuery.whereNotEqualTo("NOMINATOR", ParseUser.getCurrentUser());
        parseQuery.include("NOMINATOR");
        parseQuery.include("NOMINEE");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject parseObject : list) {
                        temp = new HashMap<String, String>();

                        temp.put("objectId", parseObject.getParseObject("NOMINATOR").getObjectId());
                        temp.put("Name", parseObject.getParseObject("NOMINATOR").getString("Name"));
                        temp.put("email", parseObject.getParseObject("NOMINATOR").getString("email"));
                        nominator_data_list_map.add(temp);
                    }
                    nominatorListener.savedNominatorList(nominator_data_list_map);
                } else {
                    Toast.makeText(mactivityContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public interface NominatorListener{
        public void savedNominatorList(ArrayList<HashMap<String, String>> nominatorList);
    }

    public void getNomineeData(Fragment fragment){

        try {
            nomineeListener = ((NomineeListener) fragment);
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement savedNomineeList.");
        }

        temp = new HashMap<String, String>();
        nominee_data_list_map = new ArrayList<HashMap<String, String>>();

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("nomination_data");
        parseQuery.whereEqualTo("NOMINATOR", ParseUser.getCurrentUser());
        parseQuery.whereNotEqualTo("NOMINEE", ParseUser.getCurrentUser());
        parseQuery.include("NOMINATOR");
        parseQuery.include("NOMINEE");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject parseObject : list) {
                        temp = new HashMap<String, String>();

                        temp.put("objectId", parseObject.getParseObject("NOMINEE").getObjectId());
                        temp.put("Name", parseObject.getParseObject("NOMINEE").getString("Name"));
                        temp.put("email", parseObject.getParseObject("NOMINEE").getString("email"));
                        nominee_data_list_map.add(temp);
                    }
                    nomineeListener.savedNomineeList(nominee_data_list_map);
                } else {
                    Toast.makeText(mactivityContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public interface NomineeListener{
        public void savedNomineeList(ArrayList<HashMap<String, String>> nomineeList);
    }
}
