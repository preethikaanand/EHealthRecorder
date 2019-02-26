package com.eHealth.recorder.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eHealth.recorder.R;
import com.eHealth.recorder.adapters.Document_ParseQueryAdapter;
import com.eHealth.recorder.parseoperation.DocumentParseOperation;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by electrorobo on 3/30/16.
 */
public class UploadDocumentFragment extends BaseListFragment implements DocumentParseOperation.DocumentLoadListener, Document_ParseQueryAdapter.DocumentClickListener{

    private Document_ParseQueryAdapter mAdapter;
    private Button button_gallery, button_camera;
    private LinearLayout linearLayout_buttons;
    private TextView textView_noSavedDocument;

    private HashMap<String, String> hashMap;

    private Bitmap bitmap;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 2;
    private Uri imageUri;

    private DocumentParseOperation documentParseOperation;

    public UploadDocumentFragment(){
        hashMap = new HashMap<String, String>();
    }

    public UploadDocumentFragment(HashMap<String, String> hashMap){
        this.hashMap = hashMap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        documentParseOperation = DocumentParseOperation.getInstace((AppCompatActivity) getActivity(), getContext());
        if (hashMap.isEmpty())
            hashMap.put("objectId", ParseUser.getCurrentUser().getObjectId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_document, container, false);

        textView_noSavedDocument = (TextView) view.findViewById(R.id.noSavedDocuments);
        button_gallery = (Button) view.findViewById(R.id.galleryButton);
        button_camera = (Button) view.findViewById(R.id.cameraButton);
        linearLayout_buttons = (LinearLayout) view.findViewById(R.id.linearLayout_imageButtons);

        mAdapter = new Document_ParseQueryAdapter(getContext(), this, hashMap);
        //mAdapter.loadObjects();
        setListAdapter(mAdapter);

        if (hashMap.get("objectId").equals(ParseUser.getCurrentUser().getObjectId()))
            linearLayout_buttons.setVisibility(View.VISIBLE);
        else
            linearLayout_buttons.setVisibility(View.GONE);

        button_gallery.setOnClickListener(galleryPickerListener);
        button_camera.setOnClickListener(captureImageListener);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemClickListener(mAdapter);
    }

    View.OnClickListener galleryPickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Intent gallery_intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //gallery_intent.setType("image/*");
                //gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery_intent, "Select Picture"), PICK_IMAGE);
            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    };

    View.OnClickListener captureImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //camera stuff
            Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "EHR_" + timeStamp + ".jpeg";

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            values.put(MediaStore.Images.Media.DESCRIPTION,"Image captured by camera");
            //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
            imageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(imageIntent, PICK_Camera_IMAGE);
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = null;
        String filePath = null;
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                    selectedImageUri = data.getData();
                }
                break;

            case PICK_Camera_IMAGE:
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    //use imageUri here to access the image
                    selectedImageUri = imageUri;
                } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                    Toast.makeText(getContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        if (selectedImageUri != null) {
            try {
                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();
                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                } else {
                    Toast.makeText(getContext(), "Unknown path", Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (filePath != null) {
                    decodeFile(filePath);
                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Internal error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(column_index);
            cursor.close();
            return picturePath;
        } else
            return null;
    }

    public void decodeFile(String filePath) {
        //Getting image name without extension
        String filename = (new File(filePath)).getName();
        String imageName_without_extension = filename.substring(0, filename.lastIndexOf("."));

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        // Convert it to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();

        documentParseOperation.imageUploadingProcess(image, imageName_without_extension, this);
        //imageView_selectedImage.setImageBitmap(bitmap);
    }

    @Override
    public void onDocumentLoad() {
        mAdapter.loadObjects();
    }

    @Override
    public void onDocumentClick(byte[] imageData_byte) {
        showNextFragment(imageData_byte);
    }

    private void showNextFragment(byte[] imageData_byte) {
        Bundle bundle = new Bundle();
        bundle.putByteArray("imageData", imageData_byte);

        SingleImageFragment singleImageFragment = new SingleImageFragment();
        singleImageFragment.setArguments(bundle);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        // Store the Fragment in stack
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_UploadDocumentFrameLayout, singleImageFragment).commit();
    }
}