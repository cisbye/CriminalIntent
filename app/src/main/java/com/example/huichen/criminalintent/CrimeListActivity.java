package com.example.huichen.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by huichen on 8/12/14.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
