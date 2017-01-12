package com.bartekcios.ticketsclient;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by bartekcios on 2017-01-13.
 */

public class RechargeOnClickListener implements View.OnClickListener, ActivityWithRequestHandling  {

    private static final String mUrl = "http://ec2-54-93-114-125.eu-central-1.compute.amazonaws.com:8000/account/";
    private static final RESTRequestTask.RequestMethod mRequestMethod = RESTRequestTask.RequestMethod.PUT;
    private final EditText editTextBalance;
    private Account account;
    private final ProgressBar progressBarLoading;
    private final Context myAccountActivity;
    private EditText editTextRecharge;

    public RechargeOnClickListener(MyAccountActivity myAccountActivity, EditText editTextBalance, Account account, ProgressBar progressBarLoading, EditText editTextRecharge) {
        this.editTextBalance = editTextBalance;
        this.account = account;
        this.progressBarLoading = progressBarLoading;
        this.myAccountActivity = myAccountActivity;
        this.editTextRecharge = editTextRecharge;
    }

    @Override
    public void onClick(View v) {
        sendDataRequest();
    }

    @Override
    public void positiveResponseCbk(JSONArray jsonArray) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            account = new Account(jsonObject);
            editTextBalance.setText(Double.toString(account.getBalance()));

            Toast.makeText(myAccountActivity, "Recharged correctly!", Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void negativeResponseCbk(JSONArray jsonArray) {
        Toast.makeText(myAccountActivity, "Error occurred!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public ProgressBar getLoadingWidget() {
        return progressBarLoading;
    }

    @Override
    public String getUrl() {
        return mUrl+account.getId()+"/";
    }

    @Override
    public RESTRequestTask.RequestMethod getRequestMethod() {
        return mRequestMethod;
    }

    private void sendDataRequest(){
        ArrayList<NameValuePair> headers = new ArrayList<>();
        ArrayList<NameValuePair> params = new ArrayList<>();
        int increaseValue = Integer.parseInt(editTextRecharge.getText().toString());

        headers.add(new BasicNameValuePair("authorization", "token "+User.mToken));
        params.add(new BasicNameValuePair("account_balance", Integer.toString(increaseValue)));

        Server server = new Server(this);
        server.sendRequest(headers, params);
    }
}
