package com.bartekcios.ticketsclient;

import android.widget.ProgressBar;

import org.json.JSONArray;

/**
 * Created by bartekcios on 2017-01-11.
 * interface contains methods needed to support REST request
 */

interface ActivityWithRequestHandling {
    void positiveResponseCbk(JSONArray jsonArray);
    void negativeResponseCbk(JSONArray jsonArray);
    ProgressBar getLoadingWidget();
    String getUrl();
    RESTRequestTask.RequestMethod getRequestMethod();
}
