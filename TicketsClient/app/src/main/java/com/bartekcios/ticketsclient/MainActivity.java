package com.bartekcios.ticketsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class MainActivity extends AppCompatActivity implements ActivityWithRequestHandling {

    private static final String mUrl = "http://ec2-54-93-114-125.eu-central-1.compute.amazonaws.com:8000/user/";
    private static final RESTRequestTask.RequestMethod mRequestMethod = RESTRequestTask.RequestMethod.GET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView username = (TextView)findViewById(R.id.textViewFirstName);
        username.setText("Hi "+User.mUsername+"!");

        if(!User.mInitialized) {
            sendDataRequest();
        }
    }

    @Override
    public void positiveResponseCbk(JSONArray jsonArray) {

        try {
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            User.mFirstName = jsonObject.getString("first_name");
            User.mLastName = jsonObject.getString("last_name");
            User.mEmail = jsonObject.getString("email");

            User.mInitialized = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void negativeResponseCbk(JSONArray jsonArray) {

        String error = "";

        try {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String firstKey = jsonObject.keys().next();
            error = jsonObject.getString(firstKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public ProgressBar getLoadingWidget() {
        return (ProgressBar)findViewById(R.id.progressBarLoading);
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
        ArrayList<NameValuePair> params = null;

        headers.add(new BasicNameValuePair("authorization", "token "+User.mToken));

        Server server = new Server(this);
        server.sendRequest(headers, params);
    }

    public void goToBuyTicket(View view) {
        Intent buyTicketActivity = new Intent(getApplicationContext(),BuyTicketActivity.class);
        startActivity(buyTicketActivity);
    }

    public void goToHistory(View view) {
        Intent historyActivity = new Intent(getApplicationContext(),HistoryActivity.class);
        startActivity(historyActivity);
    }

    public void goToMyAccount(View view) {
        Intent myAccountActivity = new Intent(getApplicationContext(),MyAccountActivity.class);
        startActivity(myAccountActivity);
    }

    public void logout(View view) {
        User.deinit();
        Intent loginActivity = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(loginActivity);
    }
}
