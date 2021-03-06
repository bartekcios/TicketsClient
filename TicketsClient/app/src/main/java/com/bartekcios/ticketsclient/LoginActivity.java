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

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class LoginActivity extends AppCompatActivity implements ActivityWithRequestHandling {

    private static final String url = "http://ec2-54-93-114-125.eu-central-1.compute.amazonaws.com:8000/auth/";
    private static final RESTRequestTask.RequestMethod requestMethod = RESTRequestTask.RequestMethod.POST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void positiveResponseCbk(JSONArray jsonArray) {
        String token = "";
        try {
            token = jsonArray.getJSONObject(0).getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        EditText editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        // init user values
        User.username = editTextUsername.getText().toString();
        User.password = editTextPassword.getText().toString();
        User.token = token;
        User.loggedIn = true;

        Intent mainMenuActivity = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(mainMenuActivity);
    }

    @Override
    public void negativeResponseCbk(JSONArray jsonArray) {
        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
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

    public void authorizeCredentials(View view) {
        EditText editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));

        Server server = new Server(this);
        server.sendRequest(null, params);
    }

    public void goToRegister(View view) {
        Intent registerActivity = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(registerActivity);
    }


}
