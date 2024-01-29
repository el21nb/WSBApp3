package com.example.wsbapp3.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wsbapp3.models.BusStopListItem;
import com.example.wsbapp3.models.ChildListItem;
import com.example.wsbapp3.R;

import java.util.ArrayList;


/**
 * Adapter for the parent recyclerview in BusStopsFragment.
 * Sets up the parent RV and calls ChildAdapter to set up the nested RV in each parent item.
 * Code for nested RV based on
 * https://github.com/foxandroid/NestedRecyclerView/blob/master/app/src/main/res/layout/child_item.xml
 */


public class BusStopAdapter extends RecyclerView.Adapter<BusStopAdapter.ViewHolder> {
    private Activity activity;
    //Array of BusStopListItem to populate the parent RV
    ArrayList<BusStopListItem> busStopItemArrayList;

    //Array of arrays of ChildListItem objects to populate the nested RVs.
    //Each arraylist corresponds to one element of the busStopItemArrayList
    ArrayList<ArrayList<ChildListItem>> childItemArrayList;
    //FragmentManager passed in from BusStopsFragment, to be passed to the ChildAdapter
    private FragmentManager fragmentManager;

    public BusStopAdapter() {
        //required empty constructor
    }

    //Constructor
    public BusStopAdapter(Activity activity, ArrayList<BusStopListItem> busStopItemArrayList, ArrayList<ArrayList<ChildListItem>> childItemArrayList, FragmentManager fragmentManager) {
        this.activity = activity;
        this.busStopItemArrayList = busStopItemArrayList;
        this.childItemArrayList = childItemArrayList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_stop_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //fetch the bus stop at the input position
        BusStopListItem busStopItem = busStopItemArrayList.get(position);
        //set text to bus stop name and address
        holder.busStopName.setText(busStopItem.getBusStopName());
        holder.busStopAddress.setText(busStopItem.getBusStopAddress());

        // extract the list of children for this bus stop item from the array of child lists
        ArrayList<ChildListItem> childItemArrayList = this.childItemArrayList.get(position);

        // pass the child list and the fragmentManager from BusStopsFragment to the child adapter
        ChildAdapter childAdapter = new ChildAdapter(childItemArrayList, fragmentManager);

        //assign the adapter to the RV
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        holder.nested_rv.setLayoutManager(linearLayoutManager);
        holder.nested_rv.setAdapter(childAdapter);
    }

    //Return number of bus stops
    @Override
    public int getItemCount() {
        return busStopItemArrayList.size();
    }

    /**
     * ViewHolder class fetches the views from the layout
     * Sets onclick listeners to expand and collapse the parent items
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        //initialise views
        TextView busStopName, busStopAddress, arrow;
        RecyclerView nested_rv;

        //Flag to indicate if parent item is expanded
        boolean isExpanded;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Find views from layout
            busStopName = itemView.findViewById(R.id.busStopName);
            busStopAddress = itemView.findViewById(R.id.address);
            nested_rv = itemView.findViewById(R.id.nested_rv);
            arrow = itemView.findViewById(R.id.arrow);

            //initialise all as collapsed
            isExpanded = false;
            updateExpandCollapseViews();

            //onclick listener toggles expanded flag
            itemView.setOnClickListener(v -> {
                isExpanded = !isExpanded;
                updateExpandCollapseViews();
            });
        }

        //updates the visibility of each nested RV, depended on expanded flag of parent item.
        private void updateExpandCollapseViews() {
            // Update visibility
            nested_rv.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            // update arrow indicate direction
            arrow.setText(isExpanded ? "v" : ">");
        }
    }
}