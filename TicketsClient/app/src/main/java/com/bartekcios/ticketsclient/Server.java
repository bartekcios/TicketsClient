package com.bartekcios.ticketsclient;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import cz.msebera.android.httpclient.NameValuePair;

/**
 * Created by bartekcios on 2017-01-03.
 */

public class Server {

    private ActivityWithRequestHandling mRequestHandling;

    public Server(ActivityWithRequestHandling requestHandling) {
        mRequestHandling = requestHandling;
    }

    public void sendRequest(ArrayList<NameValuePair> headers, ArrayList<NameValuePair> params) {

        RESTRequestTask restRequestTask = new RESTRequestTask(this,
                                                                headers,
                                                                params,
                                                                mRequestHandling.getUrl(),
                                                                mRequestHandling.getRequestMethod(),
                                                                mRequestHandling.getLoadingWidget());
        restRequestTask.execute();
    }

    public void receiveResponseCbk(JSONArray aResponse, int aResponseCode) {
        Log.d("Request response", aResponse.toString() + Integer.toString(aResponseCode));
        if(200<= aResponseCode && 209 >= aResponseCode) {
            mRequestHandling.positiveResponseCbk(aResponse);
        } else {
            mRequestHandling.negativeResponseCbk(aResponse);
        }
    }
}
