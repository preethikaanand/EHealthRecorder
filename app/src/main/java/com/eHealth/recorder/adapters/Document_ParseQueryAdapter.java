package com.eHealth.recorder.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.eHealth.recorder.R;
import com.eHealth.recorder.util.GettingDateTime;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * Created by electrorobo on 4/23/16.
 */
public class Document_ParseQueryAdapter extends ParseQueryAdapter<ParseObject> implements AdapterView.OnItemClickListener {

    private Fragment fragment;

    private GettingDateTime gettingDateTime;

    private DocumentClickListener documentClickListener;

    public Document_ParseQueryAdapter(Context context, Fragment fragment, final HashMap<String, String> hashMap) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("medical_documents");
                query.orderByDescending("createdAt");
                query.whereEqualTo("IMAGE_USER", ParseUser.createWithoutData("_User", hashMap.get("objectId")));
                return query;
            }
        });
        this.fragment = fragment;
        gettingDateTime = GettingDateTime.getInstance();
        try {
            documentClickListener = ((DocumentClickListener) fragment);
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement onDocumentClick.");
        }
    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.document_card_view_item, null);
        }

        super.getItemView(object, v, parent);

            // Add and download the image
            ParseImageView document_Image = (ParseImageView) v.findViewById(R.id.document_img_thumbnail);
            ParseFile imageFile = object.getParseFile("IMAGE_FILE");
            if (imageFile != null) {
                document_Image.setParseFile(imageFile);
                document_Image.loadInBackground();
            }

            TextView titleTextView = (TextView) v.findViewById(R.id.image_name);
            titleTextView.setText(gettingDateTime.dateFormatter(object.getCreatedAt()));

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ParseObject parseObject = (ParseObject) parent.getItemAtPosition(position);
        ParseFile parseFile = parseObject.getParseFile("IMAGE_FILE");
        try {
            final byte[] byteImageData = parseFile.getData();
            documentClickListener.onDocumentClick(byteImageData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public interface DocumentClickListener {
        public void onDocumentClick(byte[] imageData_byte);
    }
}
