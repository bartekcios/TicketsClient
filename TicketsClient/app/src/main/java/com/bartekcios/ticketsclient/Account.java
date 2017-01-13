package com.bartekcios.ticketsclient;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bartekcios on 2017-01-12.
 * Class contains parser and fields for account
 */

class Account {

    private int id;
    private double balance;

    public Account(JSONObject jsonObject) {

        try {
            id = obtainId(Uri.parse(jsonObject.getString("url")));
            balance = jsonObject.getDouble("account_balance");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public double getBalance() {
        return balance;
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
