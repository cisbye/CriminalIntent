package com.example.huichen.criminalintent.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by huichen on 8/11/14.
 */
public class Crime {
    private static final String JSON_ID     = "id";
    private static final String JSON_TITLE  = "title";
    private static final String JSON_DATE   = "date";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_PHOTO  = "photo";
    private static final String JSON_SUSPECT = "suspect";
    private static final String JSON_PHONE_NUMBER = "phone_number";

    private UUID    mId;
    private String  mTitle;
    private Date    mDate = new Date();
    private boolean mSolved;
    private Photo   mPhoto;
    private String  mSuspect;
    private String mPhoneNumber;

    public Crime() {
        // 生成唯一标识符
        mId = UUID.randomUUID();
    }

    public Crime(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_TITLE)) {
            mTitle = json.getString(JSON_TITLE);
        }
        mDate = new Date(json.getLong(JSON_DATE));
        mSolved = json.getBoolean(JSON_SOLVED);

        if (json.has(JSON_PHOTO)) {
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
        }

        if (json.has(JSON_SUSPECT)) {
            mSuspect = json.getString(JSON_SUSPECT);
        }

        if (json.has(JSON_PHONE_NUMBER)) {
            mPhoneNumber = json.getString(JSON_PHONE_NUMBER);
        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId);
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_DATE, mDate.getTime());
        json.put(JSON_SOLVED, mSolved);

        if (mPhoto != null) {
            json.put(JSON_PHOTO, mPhoto.toJSON());
        }

        json.put(JSON_SUSPECT, mSuspect);
        json.put(JSON_PHONE_NUMBER, mPhoneNumber);

        return json;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return mTitle;
    }
}
