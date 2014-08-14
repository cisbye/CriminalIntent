package com.example.huichen.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.text.format.DateFormat;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.huichen.criminalintent.camera.CrimeCameraActivity;
import com.example.huichen.criminalintent.camera.CrimeCameraFragment;
import com.example.huichen.criminalintent.model.Crime;
import com.example.huichen.criminalintent.model.CrimeLab;
import com.example.huichen.criminalintent.model.Photo;
import com.example.huichen.criminalintent.utils.PictureUtils;

import java.util.Date;
import java.util.UUID;

/**
 * Created by huichen on 8/11/14.
 */
public class CrimeFragment extends Fragment {
    private static final String TAG = "CrimeFragment";
    private static final String DIALOG_IMAGE = "image";
    public static final String  EXTRA_CRIME_ID  = "com.example.huichen.criminalintent.crime_id";

    private static final String DIALOG_DATE     = "dialog_date_tag";
    private static final int    REQUEST_DATE    = 0;
    private static final int    REQUEST_PHOTO   = 1;

    private Crime mCrime;

    private ImageButton mPhotoButton;
    private ImageView   mPhotoView;
    private EditText    mTitleField;
    private Button      mDateButton;
    private CheckBox    mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void updateDate() {
        mDateButton.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss", mCrime.getDate()));
    }

    private void showPhoto() {
        // (Re)set the image button's image based on our photo
        Photo photo = mCrime.getPhoto();
        BitmapDrawable bitmapDrawable = null;

        if (photo != null) {
            String path = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
            bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(), path);
        }

        mPhotoView.setImageDrawable(bitmapDrawable);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.getCrimeLab(getActivity()).getCrime(crimeId);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // 检查Manifest文件中是否配置了父Activity，如果有配置，才显示左上角的回退剪头
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mPhotoButton = (ImageButton)view.findViewById(R.id.crime_takePhotoButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(intent, REQUEST_PHOTO);
            }
        });

        // 对于没有摄像头的设备，直接禁用拍照按钮
        PackageManager pm = getActivity().getPackageManager();
        boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
        mPhotoButton.setEnabled(hasCamera);

        mPhotoView = (ImageView)view.findViewById(R.id.crime_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photo photo = mCrime.getPhoto();
                if (photo == null) {
                    return;
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path).show(fragmentManager, DIALOG_DATE);
            }
        });

        mTitleField = (EditText)view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });

        mDateButton = (Button)view.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox)view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // 修改问题是否已经解决属性
                mCrime.setSolved(b);
            }
        });



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getCrimeLab(getActivity()).saveCrimes();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                // 检查Manifest中是否配置的父Activity，如果有，就回退
//                if (NavUtils.getParentActivityName(getActivity()) != null) {
//                    NavUtils.navigateUpFromSameTask(getActivity());
//                }

                getActivity().finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_PHOTO) {
            String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null) {
                Photo photo = new Photo(filename);
                mCrime.setPhoto(photo);

                showPhoto();

                Log.i(TAG, "Crime: " + mCrime.getTitle() + "has a photo: " + filename);
            }
        }
    }

    public void returnResult() {
        getActivity().setResult(Activity.RESULT_OK, null);
    }
}
