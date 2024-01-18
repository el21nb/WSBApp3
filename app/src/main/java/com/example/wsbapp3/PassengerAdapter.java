package com.example.wsbapp3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.ChildViewHolder> {

    private List<String[]> passengerList;
    private OnItemClickListener listener;

    public PassengerAdapter(List<String[]> passengerList, OnItemClickListener listener) {
        this.passengerList = passengerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.passenger_item, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        String[] passengerData = passengerList.get(position);
        holder.bind(passengerData, listener);
    }

    @Override
    public int getItemCount() {
        return passengerList.size();
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder {
        private TextView passengerTextView;
        private TextView jacketTextView;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            passengerTextView = itemView.findViewById(R.id.passengerTextView);
            jacketTextView = itemView.findViewById(R.id.jacketTextView);
        }

        public void bind(String[] passengerData, OnItemClickListener listener) {
            passengerTextView.setText(passengerData[0]);  // Assuming the first element is the passenger's name
            jacketTextView.setText(passengerData[1]);  // Assuming the second element is the jacket ID
            itemView.setOnClickListener(v -> listener.onItemClick(passengerData[2]));  // Assuming the third element is the child ID
        }
    }

    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(String childId);
    }
}