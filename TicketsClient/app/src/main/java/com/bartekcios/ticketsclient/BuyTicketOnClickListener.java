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
 * Class contains on click listener for buy ticket request
 */

public class BuyTicketOnClickListener implements View.OnClickListener, ActivityWithRequestHandling {

    private static final String url = "http://ec2-54-93-114-125.eu-central-1.compute.amazonaws.com:8000/usertickets/";
    private static final RESTRequestTask.RequestMethod requestMethod = RESTRequestTask.RequestMethod.POST;
    private final ProgressBar progressBarLoading;
    private final Context context;
    private final TicketType ticketType;

    public BuyTicketOnClickListener(Context context, TicketType ticketType, ProgressBar progressBarLoading) {
        this.progressBarLoading = progressBarLoading;
        this.context = context;
        this.ticketType = ticketType;
    }

    @Override
    public void onClick(View v) {
        sendDataRequest();
    }

    @Override
    public void positiveResponseCbk(JSONArray jsonArray) {
        Toast.makeText(context, "Ticket bought!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void negativeResponseCbk(JSONArray jsonArray) {
        Toast.makeText(context, "Error occurred!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public ProgressBar getLoadingWidget() {
        return progressBarLoading;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public RESTRequestTask.RequestMethod getRequestMethod() {
        return requestMethod;
    }

    private void sendDataRequest(){
        ArrayList<NameValuePair> headers = new ArrayList<>();
        ArrayList<NameValuePair> params = new ArrayList<>();

        headers.add(new BasicNameValuePair("authorization", "token "+User.token));
        params.add(new BasicNameValuePair("ticket_id", Integer.toString(ticketType.getId())));

        Server server = new Server(this);
        server.sendRequest(headers, params);
    }
}
