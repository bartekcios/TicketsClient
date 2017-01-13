package com.bartekcios.ticketsclient;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by bartekcios on 2017-01-11.
 * Class contains parser and fields for bought ticket
 */

class BoughtTicket {

    private int id;
    private int typeId;
    private String status;
    private String validToDate;
    private TicketType ticketType = null;
    private boolean isTicketTypeDownloaded = false;

    public BoughtTicket(JSONObject jsonObject, List<TicketType> ticketTypes) {

        try {
            id = obtainId(Uri.parse(jsonObject.getString("url")));
            typeId = jsonObject.getInt("ticket_id");
            status = jsonObject.getString("status");
            validToDate = jsonObject.getString("valid_to_date");

            isTicketTypeDownloaded = getTypeData(ticketTypes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getValidToDate() {
        return validToDate;
    }

    public String getPrice() {
        if(isTicketTypeDownloaded) {
            return ticketType.getPrice();
        }else
        {
            throw new NullPointerException();
        }
    }

    public String getValidityTime() {
        if(isTicketTypeDownloaded) {
            return ticketType.getValidityTime()+" "+ ticketType.getTimeUnit();
        }else
        {
            throw new NullPointerException();
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

    private boolean getTypeData(List<TicketType> ticketTypes)
    {
        boolean found = false;
        for (TicketType type:
             ticketTypes) {
            if(typeId == type.getId())
            {
                ticketType = type;
                found = true;
                break;
            }
        }

        return found;
    }
}
