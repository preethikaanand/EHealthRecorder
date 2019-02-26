package com.eHealth.recorder.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eHealth.recorder.R;
import com.imagezoom.ImageAttacher;
import com.parse.ParseImageView;

/**
 * Created by electrorobo on 4/25/16.
 */
public class SingleImageFragment extends BaseFragment {

    private byte[] byte_image;
    private ParseImageView document_single_ImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        byte_image = getArguments().getByteArray("imageData");

        View view = inflater.inflate(R.layout.fragment_single_image, container, false);

        document_single_ImageView = (ParseImageView) view.findViewById(R.id.document_single_image);

        final Bitmap mBitmap = BitmapFactory.decodeByteArray(byte_image, 0, byte_image.length);
        //if (mBitmap != null) {
            document_single_ImageView.setImageBitmap(mBitmap);
            document_single_ImageView.loadInBackground();
        //}

        usingSimpleImage(document_single_ImageView);
        return view;
    }

    public void usingSimpleImage(ImageView imageView) {
        ImageAttacher mAttacher = new ImageAttacher(imageView);
        ImageAttacher.MAX_ZOOM = 2.0f; // Double the current Size
        ImageAttacher.MIN_ZOOM = 1.0f; // current Size
        MatrixChangeListener mMaListener = new MatrixChangeListener();
        mAttacher.setOnMatrixChangeListener(mMaListener);
        PhotoTapListener mPhotoTap = new PhotoTapListener();
        mAttacher.setOnPhotoTapListener(mPhotoTap);
    }

    private class PhotoTapListener implements ImageAttacher.OnPhotoTapListener {

        @Override
        public void onPhotoTap(View view, float x, float y) {
        }
    }

    private class MatrixChangeListener implements ImageAttacher.OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF rect) {

        }
    }

}
