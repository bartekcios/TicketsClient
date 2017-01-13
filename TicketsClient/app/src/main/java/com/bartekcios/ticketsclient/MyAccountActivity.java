package com.bartekcios.ticketsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class MyAccountActivity extends AppCompatActivity implements ActivityWithRequestHandling {

    private static final String url = "http://ec2-54-93-114-125.eu-central-1.compute.amazonaws.com:8000/account/";
    private static final RESTRequestTask.RequestMethod requestMethod = RESTRequestTask.RequestMethod.GET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        fillInFields();
        sendDataRequest();
    }

    private void fillInFields(){
        EditText editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        EditText editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        EditText editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        editTextUsername.setText(User.username);
        editTextPassword.setText(User.password);
        editTextFirstName.setText(User.firstName);
        editTextLastName.setText(User.lastName);
        editTextEmail.setText(User.email);
    }

    public void goToMainMenu(View view) {
        Intent mainMenuActivity = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(mainMenuActivity);
    }

    @Override
    public void positiveResponseCbk(JSONArray jsonArray) {
        // read and set balance
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            EditText editTextBalance = (EditText) findViewById(R.id.editTextBalance);
            EditText editTextRecharge= (EditText) findViewById(R.id.editTextRecharge);

            Account account = new Account(jsonObject);
            editTextBalance.setText(String.format(Locale.getDefault(), "%.0f", account.getBalance()));

            //set onclick listener
            Button rechargeButton = (Button) findViewById(R.id.buttonRecharge);
            rechargeButton.setOnClickListener(new RechargeOnClickListener(this, editTextBalance, account, (ProgressBar)findViewById(R.id.progressBarLoading), editTextRecharge));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void negativeResponseCbk(JSONArray jsonArray) {
        Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show();
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

    private void sendDataRequest(){
        ArrayList<NameValuePair> headers = new ArrayList<>();

        headers.add(new BasicNameValuePair("authorization", "token "+User.token));

        Server server = new Server(this);
        server.sendRequest(headers, null);
    }
}
