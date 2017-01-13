package com.bartekcios.ticketsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity implements ActivityWithRequestHandling {

    private static final String url = "http://ec2-54-93-114-125.eu-central-1.compute.amazonaws.com:8000/user/";
    private static final RESTRequestTask.RequestMethod requestMethod = RESTRequestTask.RequestMethod.POST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void positiveResponseCbk(JSONArray jsonArray) {
        goToLogin(findViewById(android.R.id.content));
    }

    @Override
    public void negativeResponseCbk(JSONArray jsonArray) {

        JSONObject jsonObject;
        String firstKey;
        String error = "";

        try {
            jsonObject = jsonArray.getJSONObject(0);
            firstKey = jsonObject.keys().next();
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
        return url;
    }

    @Override
    public RESTRequestTask.RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void sendRegisterRequest(View view) {
        EditText editTextUsername   = (EditText) findViewById(R.id.editTextUsername);
        EditText editTextPassword   = (EditText) findViewById(R.id.editTextPassword);
        EditText editTextFirstName  = (EditText) findViewById(R.id.editTextFirstName);
        EditText editTextLastName   = (EditText) findViewById(R.id.editTextLastName);
        EditText editTextEmail      = (EditText) findViewById(R.id.editTextEmail);

        String username  = editTextUsername.getText().toString();
        String password  = editTextPassword.getText().toString();
        String firstName = editTextFirstName.getText().toString();
        String lastName  = editTextLastName.getText().toString();
        String email     = editTextEmail.getText().toString();

        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("first_name", firstName));
        params.add(new BasicNameValuePair("last_name", lastName));
        params.add(new BasicNameValuePair("email", email));

        Server server = new Server(this);
        server.sendRequest(null, params);
    }

    public void goToLogin(View view) {
        Intent loginActivity = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(loginActivity);
    }
}
