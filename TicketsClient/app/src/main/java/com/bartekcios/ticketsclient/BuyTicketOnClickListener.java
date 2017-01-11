package com.bartekcios.ticketsclient;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by bartekcios on 2017-01-11.
 */

public class BuyTicketOnClickListener implements View.OnClickListener, ActivityWithRequestHandling {

    private static final String mUrl = "http://ec2-54-93-114-125.eu-central-1.compute.amazonaws.com:8000/usertickets/";
    private static final RESTRequestTask.RequestMethod mRequestMethod = RESTRequestTask.RequestMethod.POST;
    private final ProgressBar mProgressBarLoading;
    private final Context mContext;
    private final TicketType mTicketType;

    public BuyTicketOnClickListener(Context context, TicketType ticketType, ProgressBar progressBarLoading) {
        mProgressBarLoading = progressBarLoading;
        mContext = context;
        mTicketType = ticketType;
    }

    @Override
    public void onClick(View v) {
        sendDataRequest();
    }

    @Override
    public void positiveResponseCbk(JSONArray jsonArray) {
        Toast.makeText(mContext, "Ticket bought!", Toast.LENGTH_SHORT).show();
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
        return mUrl;
    }

    @Override
    public RESTRequestTask.RequestMethod getRequestMethod() {
        return mRequestMethod;
    }

    private void sendDataRequest(){
        ArrayList<NameValuePair> headers = new ArrayList<>();
        ArrayList<NameValuePair> params = new ArrayList<>();

        headers.add(new BasicNameValuePair("authorization", "token "+User.mToken));
        params.add(new BasicNameValuePair("ticket_id", Integer.toString(mTicketType.getId())));

        Server server = new Server(this);
        server.sendRequest(headers, params);
    }
}
