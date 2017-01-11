package com.bartekcios.ticketsclient;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by bartekcios on 2017-01-03.
 */


public class RESTRequestTask extends AsyncTask<Void, Void, JSONArray> {

    public enum RequestMethod {
        GET,
        POST
    }

    private int mResponseCode = 0;
    private JSONArray mResponse;
    private RequestMethod mMethod;
    private String mUrl;
    private ArrayList<NameValuePair> mHeaders;
    private ArrayList<NameValuePair> mParams;
    private Server mServer;
    private ProgressBar mLoadingWidget;

    public RESTRequestTask(Server a_server, ArrayList<NameValuePair> a_headers, ArrayList<NameValuePair> a_params, String a_url, RequestMethod a_method, ProgressBar a_loadingWidget) {
        this.mParams = a_params;
        this.mUrl = a_url;
        this.mMethod = a_method;
        this.mHeaders = a_headers;
        this.mServer = a_server;
        this.mLoadingWidget = a_loadingWidget;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(null != mLoadingWidget) {
            mLoadingWidget.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected JSONArray doInBackground(Void... params) {
        JSONArray returnValue = new JSONArray();
        try {
            returnValue = Execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        super.onPostExecute(jsonArray);
        if(null != mLoadingWidget) {
            mLoadingWidget.setVisibility(View.INVISIBLE);
        }
        mServer.receiveResponseCbk(mResponse, mResponseCode);
    }

    private JSONArray Execute() throws Exception {
        switch (mMethod) {
            case GET: {
                ExecuteGet();
                break;
            }
            case POST: {
                ExecutePost();
                break;
            }
        }

        return mResponse;
    }

    private JSONArray ExecutePost()
    {
        JSONArray retVal = null;
        HttpPost request = new HttpPost(mUrl);

        request.setHeader("Content-type", "application/json");

        try {
                //add headers
                if (mHeaders != null) {
                    mHeaders = addJsonHeaderField(mHeaders);
                    for (NameValuePair h : mHeaders)
                        request.addHeader(h.getName(), h.getValue());
                }

            //add parameters
            if(null != mParams) {
                JSONObject json = new JSONObject();

                for(NameValuePair pair : mParams)
                {
                    json.put(pair.getName(), pair.getValue());
                }

                StringEntity se = new StringEntity( json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                request.setEntity(se);
            }

            retVal = executeRequest(request);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    private JSONArray ExecuteGet()
    {
        JSONArray retVal = null;

        // add parameters
        String combinedParams = "";
        if (mParams != null) {
            combinedParams += "?";
            for (NameValuePair p : mParams) {
                String paramString = null;
                try {
                    paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (combinedParams.length() > 1)
                    combinedParams += "&" + paramString;
                else
                    combinedParams += paramString;
            }
        }
        HttpGet request = new HttpGet(mUrl + combinedParams);
        //request.setHeader("Content-type", "application/json");

        // add headers
        if (mHeaders != null) {
            mHeaders = addJsonHeaderField(mHeaders);
            for (NameValuePair h : mHeaders)
                request.addHeader(h.getName(), h.getValue());
        }
        mResponse = executeRequest(request);

        return retVal;
    }

    private ArrayList<NameValuePair> addJsonHeaderField(ArrayList<NameValuePair> _header) {
        _header.add(new BasicNameValuePair("Content-Type", "application/json"));
        return _header;
    }

    private JSONArray executeRequest(HttpUriRequest request) {
        HttpClient client = new DefaultHttpClient();
        HttpResponse httpResponse;
        try {
            httpResponse = client.execute(request);
            mResponseCode = httpResponse.getStatusLine().getStatusCode();
            String message = httpResponse.getStatusLine().getReasonPhrase();        // TODO to check if needed
            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {
                InputStream inputStream = entity.getContent();

                mResponse = convertInputStreamToJSONArray(inputStream);
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mResponse;
    }


    private static JSONArray convertInputStreamToJSONArray(InputStream inputStream)
            throws JSONException {

        JSONArray retVal;

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String result = "";
        try {
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Object json = new JSONTokener(result).nextValue();
        if (json instanceof JSONObject)
        {
            JSONObject jsonObject = new JSONObject(result);
            retVal = new JSONArray();
            retVal.put(jsonObject);
        }
        else if (json instanceof JSONArray)
        {
            retVal = new JSONArray(result);
        }
        else
        {
            throw new JSONException("Returned value is not a JSONObject neither JSONArray");
        }

        return retVal;
    }
}
