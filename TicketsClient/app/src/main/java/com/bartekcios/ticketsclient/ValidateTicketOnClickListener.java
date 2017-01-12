package com.bartekcios.ticketsclient;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by bartekcios on 2017-01-11.
 */

public class ValidateTicketOnClickListener  implements View.OnClickListener, ActivityWithRequestHandling {

    private static final String mUrl = "http://ec2-54-93-114-125.eu-central-1.compute.amazonaws.com:8000/usertickets/";
    private static final RESTRequestTask.RequestMethod mRequestMethod = RESTRequestTask.RequestMethod.PUT;
    private final ProgressBar mProgressBarLoading;
    private final Context mContext;
    private final BoughtTicket mBoughtTicket;
    private TextView statusTextView;
    private TextView validToDataTextView;
    private Button validateButton;

    public ValidateTicketOnClickListener(Context context, BoughtTicket ticket, ProgressBar progressBarLoading, TextView statusTextView, TextView validToDataTextView, Button validateButton) {
        this.mBoughtTicket = ticket;
        this.mProgressBarLoading = progressBarLoading;
        this.mContext = context;
        this.statusTextView = statusTextView;
        this.validToDataTextView = validToDataTextView;
        this.validateButton = validateButton;
    }

    @Override
    public void onClick(View v) {
        sendDataRequest();
    }

    @Override
    public void positiveResponseCbk(JSONArray jsonArray) {
        Toast.makeText(mContext, "Ticket validated!", Toast.LENGTH_SHORT).show();

        try {
            // get changed ticket
            List<TicketType> ticketTypes = new ArrayList<>();   // create empty list, we don't need the type of ticket
            BoughtTicket tempTicket = new BoughtTicket(jsonArray.getJSONObject(0), ticketTypes);

            // set values in view
            statusTextView.setText(tempTicket.getStatus());
            validToDataTextView.setText(tempTicket.getValidToDate());
            validateButton.setVisibility(View.INVISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void negativeResponseCbk(JSONArray jsonArray) {
        Toast.makeText(mContext, "Error occurred!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public ProgressBar getLoadingWidget() {
        return mProgressBarLoading;
    }

    @Override
    public String getUrl() {
        return mUrl+mBoughtTicket.getId()+"/";
    }

    @Override
    public RESTRequestTask.RequestMethod getRequestMethod() {
        return mRequestMethod;
    }

    private void sendDataRequest(){
        ArrayList<NameValuePair> headers = new ArrayList<>();
        ArrayList<NameValuePair> params = new ArrayList<>();

        headers.add(new BasicNameValuePair("authorization", "token "+User.mToken));
        params.add(new BasicNameValuePair("status", "active"));

        Server server = new Server(this);
        server.sendRequest(headers, params);
    }
}
