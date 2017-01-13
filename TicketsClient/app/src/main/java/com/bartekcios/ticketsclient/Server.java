package com.bartekcios.ticketsclient;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import cz.msebera.android.httpclient.NameValuePair;

/**
 * Created by bartekcios on 2017-01-03.
 * Handles requests from client using prepared interface
 */

class Server {

    private final ActivityWithRequestHandling requestHandling;

    public Server(ActivityWithRequestHandling requestHandling) {
        this.requestHandling = requestHandling;
    }

    public void sendRequest(ArrayList<NameValuePair> headers, ArrayList<NameValuePair> params) {

        RESTRequestTask restRequestTask = new RESTRequestTask(this,
                                                                headers,
                                                                params,
                                                                requestHandling.getUrl(),
                                                                requestHandling.getRequestMethod(),
                                                                requestHandling.getLoadingWidget());
        restRequestTask.execute();
    }

    public void receiveResponseCbk(JSONArray aResponse, int aResponseCode) {
        Log.d("Request response", aResponse.toString() + Integer.toString(aResponseCode));
        if(200<= aResponseCode && 209 >= aResponseCode) {
            requestHandling.positiveResponseCbk(aResponse);
        } else {
            requestHandling.negativeResponseCbk(aResponse);
        }
    }
}
