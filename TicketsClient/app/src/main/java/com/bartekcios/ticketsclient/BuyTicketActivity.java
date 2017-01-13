package com.bartekcios.ticketsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class BuyTicketActivity extends AppCompatActivity implements ActivityWithRequestHandling {

    private static final String url = "http://ec2-54-93-114-125.eu-central-1.compute.amazonaws.com:8000/ticket/";
    private static final RESTRequestTask.RequestMethod requestMethod = RESTRequestTask.RequestMethod.GET;
    private List<TicketType> ticketTypes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        sendDataRequest();
    }

    public void goToMainMenu(View view) {
        Intent mainMenuActivity = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(mainMenuActivity);
    }

    @Override
    public void positiveResponseCbk(JSONArray jsonArray) {

        ticketTypes = new ArrayList<>();
        try {
        for(int i=0;i<jsonArray.length();++i){
            ticketTypes.add(new TicketType(jsonArray.getJSONObject(i)));
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initTicketTypesList();
    }

    @Override
    public void negativeResponseCbk(JSONArray jsonArray) {

    }

    @Override
    public ProgressBar getLoadingWidget() {
        return (ProgressBar)findViewById(R.id.progressBarLoading);
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

        Server server = new Server(this);
        server.sendRequest(headers, params);
    }

    private void initTicketTypesList()
    {
        ListAdapter ticketsAdapter = new CustomTicketTypeAdapter(this, ticketTypes, (ProgressBar)findViewById(R.id.progressBarLoading));
        ListView ticketsListView = (ListView)findViewById(R.id.listViewTickets);

        ticketsListView.setAdapter(ticketsAdapter);
    }
}
