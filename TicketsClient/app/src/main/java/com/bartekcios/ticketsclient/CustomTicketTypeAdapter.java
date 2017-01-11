package com.bartekcios.ticketsclient;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by bartekcios on 2017-01-10.
 */

public class CustomTicketTypeAdapter extends ArrayAdapter<TicketType>{

    private final ProgressBar mProgressBarLoading;
    private final Context mContext;

    public CustomTicketTypeAdapter(Context context, List<TicketType> tickets, ProgressBar progressBarLoading) {
        super(context, R.layout.custom_ticket_row, tickets);
        mProgressBarLoading = progressBarLoading;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater ticketsInflater = LayoutInflater.from(getContext());
        View customView = ticketsInflater.inflate(R.layout.custom_ticket_row, parent, false);

        TicketType ticket = getItem(position);
        TextView nameTextView = (TextView)customView.findViewById(R.id.textViewName);
        TextView priceTextView = (TextView)customView.findViewById(R.id.textViewPrice);
        TextView descriptionTextView = (TextView)customView.findViewById(R.id.textViewDescription);
        TextView validityTimeTextView = (TextView)customView.findViewById(R.id.textViewValidityTime);
        Button buyButton = (Button) customView.findViewById(R.id.buttonBuy);

        nameTextView.setText(ticket.getName());
        priceTextView.setText(ticket.getPrice());
        descriptionTextView.setText(ticket.getDescription());
        validityTimeTextView.setText(ticket.getValidityTime()+" "+ticket.getTimeUnit());

        buyButton.setOnClickListener(new BuyTicketOnClickListener(mContext, ticket, mProgressBarLoading));

        // TODO button click buy

        return customView;
    }
}
