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
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by bartekcios on 2017-01-03.
 * Class contains implementation for REST request using async task
 */


public class RESTRequestTask extends AsyncTask<Void, Void, JSONArray> {

    public enum RequestMethod {
        GET,
        POST,
        PUT
    }

    private int responseCode = 0;
    private JSONArray response;
    private final RequestMethod method;
    private final String url;
    private ArrayList<NameValuePair> headers;
    private final ArrayList<NameValuePair> params;
    private final Server server;
    private final ProgressBar loadingWidget;

    public RESTRequestTask(Server a_server, ArrayList<NameValuePair> a_headers, ArrayList<NameValuePair> a_params, String a_url, RequestMethod a_method, ProgressBar a_loadingWidget) {
        this.params = a_params;
        this.url = a_url;
        this.method = a_method;
        this.headers = a_headers;
        this.server = a_server;
        this.loadingWidget = a_loadingWidget;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(null != loadingWidget) {
            loadingWidget.setVisibility(View.VISIBLE);
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
        if(null != loadingWidget) {
            loadingWidget.setVisibility(View.INVISIBLE);
        }
        server.receiveResponseCbk(response, responseCode);
    }

    private JSONArray Execute() {
        switch (method) {
            case GET: {
                ExecuteGet();
                break;
            }
            case POST: {
                ExecutePost();
                break;
            }
            case PUT: {
                ExecutePut();
                break;
            }
            default:
            {
                break;
            }
        }
        return response;
    }

    private void ExecuteGet()
    {
        // add parameters
        String combinedParams = "";
        if (params != null) {
            combinedParams += "?";
            for (NameValuePair p : params) {
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
        HttpGet request = new HttpGet(url + combinedParams);
        //request.setHeader("Content-type", "application/json");

        // add headers
        if (headers != null) {
            headers = addJsonHeaderField(headers);
            for (NameValuePair h : headers)
                request.addHeader(h.getName(), h.getValue());
        }
        response = executeRequest(request);
    }

    private void ExecutePost()
    {
        HttpPost request = new HttpPost(url);

        request.setHeader("Content-type", "application/json");

        try {
                //add headers
                if (headers != null) {
                    headers = addJsonHeaderField(headers);
                    for (NameValuePair h : headers)
                        request.addHeader(h.getName(), h.getValue());
                }

            //add parameters
            if(null != params) {
                JSONObject json = new JSONObject();

                for(NameValuePair pair : params)
                {
                    json.put(pair.getName(), pair.getValue());
                }

                StringEntity se = new StringEntity( json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                request.setEntity(se);
            }

            executeRequest(request);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void ExecutePut()
    {
        HttpPut request = new HttpPut(url);

        request.setHeader("Content-type", "application/json");

        try {
            //add headers
            if (headers != null) {
                headers = addJsonHeaderField(headers);
                for (NameValuePair h : headers)
                    request.addHeader(h.getName(), h.getValue());
            }

            //add parameters
            if(null != params) {
                JSONObject json = new JSONObject();

                for(NameValuePair pair : params)
                {
                    json.put(pair.getName(), pair.getValue());
                }

                StringEntity se = new StringEntity( json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                request.setEntity(se);
            }

            executeRequest(request);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<NameValuePair> addJsonHeaderField(ArrayList<NameValuePair> _header) {
        _header.add(new BasicNameValuePair("Content-Type", "application/json"));
        return _header;
    }

    private JSONArray executeRequest(HttpUriRequest request) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse httpResponse;
        try {
            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {
                InputStream inputStream = entity.getContent();

                response = convertInputStreamToJSONArray(inputStream);
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
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
