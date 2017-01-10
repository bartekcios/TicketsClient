package com.bartekcios.ticketsclient;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by bartekcios on 2017-01-10.
 */

public class CustomTicketAdapter extends ArrayAdapter<JSONObject>{
    public CustomTicketAdapter(Context context, List<JSONObject> jsonObjects) {
        super(context, R.layout.custom_ticket_row, jsonObjects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater jsonsInflater = LayoutInflater.from(getContext());
        View customView = jsonsInflater.inflate(R.layout.custom_ticket_row, parent, false);

        JSONObject singleJsonObj = getItem(position);
        TextView nameTextView = (TextView)customView.findViewById(R.id.textViewName);
        TextView priceTextView = (TextView)customView.findViewById(R.id.textViewPrice);
        TextView descriptionTextView = (TextView)customView.findViewById(R.id.textViewDescription);
        TextView validityTimeTextView = (TextView)customView.findViewById(R.id.textViewValidityTime);


        String name = "";
        String price = "";
        String description = "";
        String validityTime = "";
        String timeUnit = "";
        try {
            name = singleJsonObj.getString("name");
            price = singleJsonObj.getString("price");
            description = singleJsonObj.getString("description");
            validityTime = singleJsonObj.getString("time_of_validity");
            timeUnit = singleJsonObj.getString("time_unit");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        nameTextView.setText(name);
        priceTextView.setText(price);
        descriptionTextView.setText(description);
        validityTimeTextView.setText(validityTime+" "+timeUnit);

        return customView;
    }
}
