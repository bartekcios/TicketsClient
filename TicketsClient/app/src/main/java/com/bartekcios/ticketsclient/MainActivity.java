package com.bartekcios.ticketsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToBuyTicket(View view) {
        Intent buyTicketActivity = new Intent(getApplicationContext(),BuyTicketActivity.class);
        startActivity(buyTicketActivity);
    }

    public void goToHistory(View view) {
        Intent historyActivity = new Intent(getApplicationContext(),HistoryActivity.class);
        startActivity(historyActivity);
    }

    public void goToMyAccount(View view) {
        Intent myAccountActivity = new Intent(getApplicationContext(),MyAccountActivity.class);
        startActivity(myAccountActivity);
    }

    public void logout(View view) {
        Intent loginActivity = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(loginActivity);
    }
}
