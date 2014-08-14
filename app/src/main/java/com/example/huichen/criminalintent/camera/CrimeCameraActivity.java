package com.example.huichen.criminalintent.camera;

import android.support.v4.app.Fragment;

import com.example.huichen.criminalintent.SingleFragmentActivity;

/**
 * Created by huichen on 8/14/14.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }
}
