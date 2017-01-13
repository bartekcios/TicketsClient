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

public class HistoryActivity extends AppCompatActivity implements ActivityWithRequestHandling{

    private static final String ticketsTypesListUrl = "http://ec2-54-93-114-125.eu-central-1.compute.amazonaws.com:8000/ticket/";
    private static final String historyUrl = "http://ec2-54-93-114-125.eu-central-1.compute.amazonaws.com:8000/usertickets/";
    private static final RESTRequestTask.RequestMethod commonRequestMethod = RESTRequestTask.RequestMethod.GET;
    private List<TicketType> ticketTypes = null;
    private List<BoughtTicket> boughtTickets = null;
    private boolean isTicketTypesListCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //send request for types of tickets
        sendDataRequest();
    }

    public void goToMainMenu(View view) {
        Intent mainMenuActivity = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(mainMenuActivity);
    }

    @Override
    public void positiveResponseCbk(JSONArray jsonArray) {

        if(!isTicketTypesListCreated)
        {
            // create list of ticket types
            ticketTypes = new ArrayList<>();
            try {
                for(int i=0;i<jsonArray.length();++i){
                    ticketTypes.add(new TicketType(jsonArray.getJSONObject(i)));
                }
                isTicketTypesListCreated = true;
                // send request for bought tickets
                sendDataRequest();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            // create list of users tickets
            boughtTickets = new ArrayList<>();
            try {
                for(int i=0;i<jsonArray.length();++i){
                    boughtTickets.add(new BoughtTicket(jsonArray.getJSONObject(i), ticketTypes));
                }

                initBoughtTicketsList();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        if(!isTicketTypesListCreated)
        {
            return ticketsTypesListUrl;
        }
        else
        {
            return historyUrl;
        }
    }

    @Override
    public RESTRequestTask.RequestMethod getRequestMethod() {
        return commonRequestMethod;
    }

    private void sendDataRequest(){
        ArrayList<NameValuePair> headers = new ArrayList<>();
        ArrayList<NameValuePair> params = new ArrayList<>();

        headers.add(new BasicNameValuePair("authorization", "token "+User.token));

        Server server = new Server(this);
        server.sendRequest(headers, params);
    }

    private void initBoughtTicketsList()
    {
        ListAdapter ticketsAdapter = new CustomBoughtTicketAdapter(this, boughtTickets, (ProgressBar)findViewById(R.id.progressBarLoading));
        ListView ticketsListView = (ListView)findViewById(R.id.listViewBoughtTickets);

        ticketsListView.setAdapter(ticketsAdapter);
    }
}
