package com.bartekcios.ticketsclient;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bartekcios on 2017-01-10.
 * Class contains parser and fields for ticket types
 */

class TicketType {
    private String name;
    private String price;
    private String description;
    private String validityTime;
    private String timeUnit;
    private int mId;

    public TicketType(JSONObject jsonObject) {

        try {
            name = jsonObject.getString("name");
            price = jsonObject.getString("price");
            description = jsonObject.getString("description");
            validityTime = jsonObject.getString("time_of_validity");
            timeUnit = jsonObject.getString("time_unit");
            mId = obtainId(Uri.parse(jsonObject.getString("url")));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int obtainId(Uri uri)
    {
        int id;
        String[] segments = uri.getPath().split("/");
        String idStr = segments[segments.length-1];
        id = Integer.parseInt(idStr);

        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public String getValidityTime() {
        return validityTime;
    }

    public int getId() {
        return mId;
    }


}
