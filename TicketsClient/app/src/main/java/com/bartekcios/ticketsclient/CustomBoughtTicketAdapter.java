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

import java.util.List;

/**
 * Created by bartekcios on 2017-01-11.
 */

public class CustomBoughtTicketAdapter extends ArrayAdapter<BoughtTicket> {
    private final ProgressBar mProgressBarLoading;
    private final Context mContext;

    public CustomBoughtTicketAdapter(Context context, List<BoughtTicket> tickets, ProgressBar progressBarLoading) {
        super(context, R.layout.custom_history_ticket_row, tickets);
        mProgressBarLoading = progressBarLoading;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater ticketsInflater = LayoutInflater.from(getContext());
        View customView = ticketsInflater.inflate(R.layout.custom_history_ticket_row, parent, false);

        BoughtTicket ticket             = getItem(position);
        TextView priceTextView          = (TextView)customView.findViewById(R.id.textViewPrice);
        TextView validityTimeTextView   = (TextView)customView.findViewById(R.id.textViewValidityTime);
        TextView statusTextView         = (TextView)customView.findViewById(R.id.textViewStatus);
        TextView validToDataTextView    = (TextView)customView.findViewById(R.id.textViewValidToDate);
        Button validateButton           = (Button)  customView.findViewById(R.id.buttonValidateTheTicket);

        if(!ticket.getStatus().equals("new"))
        {
            validateButton.setVisibility(View.INVISIBLE);
        }

        priceTextView.setText(ticket.getPrice());
        validityTimeTextView.setText(ticket.getValidityTime());
        statusTextView.setText(ticket.getStatus());
        validToDataTextView.setText(ticket.getValidToDate());

        validateButton.setOnClickListener(new ValidateTicketOnClickListener(mContext, ticket, mProgressBarLoading, statusTextView, validToDataTextView, validateButton));

        return customView;
    }
}
