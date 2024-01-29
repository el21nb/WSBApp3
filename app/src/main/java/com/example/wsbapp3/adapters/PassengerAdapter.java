package com.example.wsbapp3.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wsbapp3.R;

import java.util.List;

/**
 * Adapter for displaying the recyclerView list of onboard passengers in PassengerFragment
 */
public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.ChildViewHolder> {

    private List<String[]> passengerList;
    private OnItemClickListener listener;

    /**
     * Constructor
     *
     * @param passengerList List of String Arrays, each containing passenger name, assigned jacket code, childId
     * @param listener      onclick listener for the RV items, to open a ChildInfo fragment
     */
    public PassengerAdapter(List<String[]> passengerList, OnItemClickListener listener) {
        this.passengerList = passengerList;
        this.listener = listener;
    }

    /**
     * Called to when a view holder is created, in the process of adding an item to the recyclerView
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.passenger_item, parent, false);
        return new ChildViewHolder(view);
    }

    /**
     * Called to display the passenger data in the recyclerview item
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        String[] passengerData = passengerList.get(position);
        holder.bind(passengerData, listener);
    }

    /**
     * @return number of passengers in list
     */
    @Override
    public int getItemCount() {
        return passengerList.size();
    }

    /**
     * Viewholder class, used to represent the passenger items in the recyclerview
     */
    static class ChildViewHolder extends RecyclerView.ViewHolder {

        //TextView to show passenger name
        private TextView passengerTextView;
        //Textview to show assigned jacket code
        private TextView jacketTextView;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            passengerTextView = itemView.findViewById(R.id.passengerTextView);
            jacketTextView = itemView.findViewById(R.id.jacketTextView);
        }

        /**
         * @param passengerData array of
         * @param listener
         */
        public void bind(String[] passengerData, OnItemClickListener listener) {
            //Set the first element of passengerData, the passenger name, to the top textview
            passengerTextView.setText(passengerData[0]);
            //Set the second element, the assigned jacket code, to the second textview
            jacketTextView.setText(passengerData[1]);
            //pass the third array element, the childId, to the the onclick listener
            itemView.setOnClickListener(v -> listener.onItemClick(passengerData[2]));
        }
    }

    /**
     * Interface for item click listener.
     */
    public interface OnItemClickListener {
        //onItemClick declared in PassengersFragment
        //Called when a passenger item is clicked
        void onItemClick(String childId);
    }
}