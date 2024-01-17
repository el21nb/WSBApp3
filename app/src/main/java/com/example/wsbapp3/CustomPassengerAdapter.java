package com.example.wsbapp3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomPassengerAdapter extends ArrayAdapter<String[]> {

    private Context context;
    private List<String[]> passengerList;

    public CustomPassengerAdapter(@NonNull Context context, int resource, @NonNull List<String[]> objects) {
        super(context, resource, objects);
        this.context = context;
        this.passengerList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_passenger_row, parent, false);
        }

        // Get the data for this position
        String[] passengerData = getItem(position);

        // Set the data to the TextViews in your custom layout
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView jacketTextView = convertView.findViewById(R.id.jacketTextView);

        if (passengerData != null) {
            nameTextView.setText(passengerData[0]);
            jacketTextView.setText(passengerData[1]);
        }

        return convertView;
    }
}