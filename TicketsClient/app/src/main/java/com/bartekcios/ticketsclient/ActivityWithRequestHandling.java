package com.bartekcios.ticketsclient;

import android.widget.ProgressBar;

import org.json.JSONArray;

/**
 * Created by bartekcios on 2017-01-11.
 */

public interface ActivityWithRequestHandling<T> {
    void positiveResponseCbk(JSONArray jsonArray);
    void negativeResponseCbk(JSONArray jsonArray);
    ProgressBar getLoadingWidget();
    String getUrl();
    RESTRequestTask.RequestMethod getRequestMethod();
}
