package com.example.huichen.criminalintent;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.huichen.criminalintent.utils.PictureUtils;

/**
 * Created by huichen on 8/15/14.
 */
public class ImageFragment extends DialogFragment {
    public static final String EXTRA_IMAGE_PATH = "com.example.huichen.criminalintent.image_path";

    private ImageView mImageView;

    public static ImageFragment newInstance(String imagePath) {
        Bundle args = new Bundle();
        args.putString(EXTRA_IMAGE_PATH, imagePath);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mImageView = new ImageView(getActivity());
        String path = getArguments().getString(EXTRA_IMAGE_PATH);
        BitmapDrawable bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(), path);
        mImageView.setImageDrawable(bitmapDrawable);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return mImageView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PictureUtils.cleanImageView(mImageView);
    }
}
