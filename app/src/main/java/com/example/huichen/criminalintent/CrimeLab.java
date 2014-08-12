package com.example.huichen.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by huichen on 8/12/14.
 */
public class CrimeLab {
    private static CrimeLab     sCrimeLab;

    private Context             mAppContext;
    private ArrayList<Crime>    mCrimes;

    private CrimeLab(Context appContext) {
        mAppContext = appContext;
        mCrimes = new ArrayList<Crime>();

        // test data
        for (int i = 0; i < 102; i++) {
            Crime c = new Crime();
            c.setTitle("Crime #" + i);
            c.setSolved(i % 2 == 0);
            mCrimes.add(c);
        }
    }

    public static CrimeLab getCrimeLab(Context appContext) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(appContext.getApplicationContext());
        }

        return sCrimeLab;
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime c : mCrimes) {
            if (c.getId().equals(id)) {
                return c;
            }
        }

        return null;
    }
}
