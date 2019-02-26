package com.eHealth.recorder.parseoperation;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.eHealth.recorder.util.ProgressBarHelper;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by electrorobo on 4/22/16.
 */
public class DocumentParseOperation {

    private static AppCompatActivity mactivityContext;
    private static Context mcontext;

    private static DocumentParseOperation documentParseOperation;

    private DocumentLoadListener documentLoadListener;
    final ProgressBarHelper progressBarHelper = ProgressBarHelper.getSingletonInstance();
    private Handler handler = new Handler();

    private DocumentParseOperation(){

    }

    public static DocumentParseOperation getInstace(AppCompatActivity activityContext, Context context){
        mcontext = context;
        mactivityContext = activityContext;
        if(documentParseOperation == null){
            documentParseOperation = new DocumentParseOperation();
        }
        return documentParseOperation;
    }

    public void imageUploadingProcess(byte[] image, String imageName_without_extension, Fragment fragment){
        try {
            documentLoadListener = ((DocumentLoadListener) fragment);
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement onDocumentLoad.");
        }

        ParseUser user = ParseUser.getCurrentUser();
        progressBarHelper.showProgressBarSmall("Please wait while uploading document...", false, handler, mcontext);
        // Create the ParseFile
        ParseFile file = new ParseFile(imageName_without_extension+".png", image);
        // Upload the image into Parse Cloud
        file.saveInBackground();

        // Create a New Class called "ImageUpload" in Parse
        ParseObject image_upload = new ParseObject("medical_documents");
        image_upload.put("IMAGE_NAME", imageName_without_extension+".png");
        image_upload.put("IMAGE_FILE", file);
        image_upload.put("IMAGE_USER", user);
        image_upload.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    progressBarHelper.dismissProgressBar(handler);
                    documentLoadListener.onDocumentLoad();
                    Toast.makeText(mactivityContext, "Document has been uploaded", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public interface DocumentLoadListener {
        public void onDocumentLoad();
    }
}
