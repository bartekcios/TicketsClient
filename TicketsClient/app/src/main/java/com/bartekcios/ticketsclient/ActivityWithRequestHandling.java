package com.bartekcios.ticketsclient;

import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by bartekcios on 2017-01-09.
 */

public interface ActivityWithRequestHandling {
    void positiveResponseCbk(JSONArray jsonArray);
    void negativeResponseCbk(JSONArray jsonArray);
    ProgressBar getLoadingWidget();
    String getUrl();
    RESTRequestTask.RequestMethod getRequestMethod();
}
