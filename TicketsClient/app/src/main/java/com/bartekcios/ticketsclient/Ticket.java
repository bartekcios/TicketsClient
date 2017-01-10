package com.bartekcios.ticketsclient;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by bartekcios on 2017-01-10.
 */

public class Ticket {
    private String mName;
    private String mPrice;
    private String mDescription;
    private String mValidityTime;
    private String mTimeUnit;
    private int mId;

    public String getPrice() {
        return mPrice;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getName() {
        return mName;
    }

    public String getTimeUnit() {
        return mTimeUnit;
    }

    public String getValidityTime() {
        return mValidityTime;
    }

    public int getId() {
        return mId;
    }

    public Ticket(JSONObject jsonObject) {

        try {
            mName = jsonObject.getString("name");
            mPrice = jsonObject.getString("price");
            mDescription = jsonObject.getString("description");
            mValidityTime = jsonObject.getString("time_of_validity");
            mTimeUnit = jsonObject.getString("time_unit");
            mId = obtainId(Uri.parse(jsonObject.getString("url")));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int obtainId(Uri uri)
    {
        int id = 0;
        String[] segments = uri.getPath().split("/");
        String idStr = segments[segments.length-1];
        id = Integer.parseInt(idStr);

        return id;
    }

    //public final String
}
