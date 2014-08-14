package com.example.huichen.criminalintent.model;

import android.content.Context;
import android.util.Log;

import com.example.huichen.criminalintent.utils.CriminalIntentJSONSerializer;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by huichen on 8/12/14.
 */
public class CrimeLab {
    private static final String TAG         = "CrimeLab";
    private static final String FILENAME    = "crimes.json";

    private static CrimeLab     sCrimeLab;

    private Context             mAppContext;
    private ArrayList<Crime>    mCrimes;
    private CriminalIntentJSONSerializer mSerializer;

    private CrimeLab(Context appContext) {
        mAppContext = appContext;
        mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);

        try {
            mCrimes = mSerializer.loadCrimes();
        } catch (Exception e) {
            mCrimes = new ArrayList<Crime>();
            Log.e(TAG, "Error loading crimes: ", e);
        }

//        // test data
//        for (int i = 0; i < 102; i++) {
//            Crime c = new Crime();
//            c.setTitle("Crime #" + i);
//            c.setSolved(i % 2 == 0);
//            mCrimes.add(c);
//        }
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

    public void addCrime(Crime crime) {
        mCrimes.add(crime);
    }

    public void deleteCrime(Crime crime) {
        mCrimes.remove(crime);
    }

    public boolean saveCrimes() {
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "crimes saved to file");

            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving crimes: ", e);
            return false;
        }
    }
}
