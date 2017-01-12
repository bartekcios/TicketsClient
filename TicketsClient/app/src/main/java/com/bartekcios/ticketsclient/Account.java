package com.bartekcios.ticketsclient;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bartekcios on 2017-01-12.
 */

public class Account {

    private int mId;
    private double mBalance;

    public Account(JSONObject jsonObject) {

        try {
            mId = obtainId(Uri.parse(jsonObject.getString("url")));
            mBalance = jsonObject.getDouble("account_balance");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return mId;
    }

    public double getBalance() {
        return mBalance;
    }

    private int obtainId(Uri uri)
    {
        int id;
        String[] segments = uri.getPath().split("/");
        String idStr = segments[segments.length-1];
        id = Integer.parseInt(idStr);

        return id;
    }
}
