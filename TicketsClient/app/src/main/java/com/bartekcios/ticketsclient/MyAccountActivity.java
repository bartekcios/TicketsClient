package com.bartekcios.ticketsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class MyAccountActivity extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        fillInFields();
    }

    private void fillInFields(){
        EditText editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        EditText editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        EditText editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        editTextUsername.setText(User.mUsername);
        editTextPassword.setText(User.mPassword);
        editTextFirstName.setText(User.mFirstName);
        editTextLastName.setText(User.mLastName);
        editTextEmail.setText(User.mEmail);
    }

    public void goToMainMenu(View view) {
        Intent mainMenuActivity = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(mainMenuActivity);
    }
}
