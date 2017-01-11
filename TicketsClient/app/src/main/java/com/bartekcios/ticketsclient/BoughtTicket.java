package com.bartekcios.ticketsclient;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by bartekcios on 2017-01-11.
 */

public class BoughtTicket {

    private int mId;
    private int mTypeId;
    private String mStatus;
    private String mValidToDate;
    private TicketType mTicketType = null;
    private boolean mIsTicketTypeDownloaded = false;

    public BoughtTicket(JSONObject jsonObject, List<TicketType> ticketTypes) {

        try {
            mId = obtainId(Uri.parse(jsonObject.getString("url")));
            mTypeId = jsonObject.getInt("ticket_id");
            mStatus = jsonObject.getString("status");
            mValidToDate = jsonObject.getString("valid_to_date");

            mIsTicketTypeDownloaded = getTypeData(ticketTypes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return mId;
    }

    public int getTypeId() {
        return mTypeId;
    }

    public String getStatus() {
        return mStatus;
    }

    public String getValidToDate() {
        return mValidToDate;
    }

    public String getPrice() {
        if(mIsTicketTypeDownloaded) {
            return mTicketType.getPrice();
        }else
        {
            throw new NullPointerException();
        }
    }

    public String getValidityTime() {
        if(mIsTicketTypeDownloaded) {
            return mTicketType.getValidityTime()+" "+mTicketType.getTimeUnit();
        }else
        {
            throw new NullPointerException();
        }
    }

    public boolean isTicketTypeDownloaded() {
        return mIsTicketTypeDownloaded;
    }

    private int obtainId(Uri uri)
    {
        int id;
        String[] segments = uri.getPath().split("/");
        String idStr = segments[segments.length-1];
        id = Integer.parseInt(idStr);

        return id;
    }

    public boolean getTypeData(List<TicketType> ticketTypes)
    {
        boolean found = false;
        for (TicketType type:
             ticketTypes) {
            if(mTypeId == type.getId())
            {
                mTicketType = type;
                found = true;
                break;
            }
        }

        return found;
    }
}
