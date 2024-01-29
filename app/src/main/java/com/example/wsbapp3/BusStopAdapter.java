package com.example.wsbapp3;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//https://github.com/foxandroid/NestedRecyclerView/blob/master/app/src/main/res/layout/child_item.xml
public class BusStopAdapter extends RecyclerView.Adapter<BusStopAdapter.ViewHolder> {
    private Activity activity;
    ArrayList<BusStopListItem> busStopItemArrayList;
    ArrayList<ArrayList<ChildListItem>> childItemArrayList;

    // Constructor
    public BusStopAdapter(Activity activity, ArrayList<BusStopListItem> busStopItemArrayList, ArrayList<ArrayList<ChildListItem>> childItemArrayList) {
        this.activity = activity;
        this.busStopItemArrayList = busStopItemArrayList;
        this.childItemArrayList = childItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_stop_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BusStopListItem busStopItem = busStopItemArrayList.get(position);
        holder.busStopName.setText(busStopItem.busStopName);
        holder.busStopAddress.setText(busStopItem.busStopAddress);

        // Retrieve the child list for the current bus stop
        ArrayList<ChildListItem> childItemArrayList = this.childItemArrayList.get(position);

        // Pass the child list to the ChildAdapter
        ChildAdapter childAdapter = new ChildAdapter(childItemArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        holder.nested_rv.setLayoutManager(linearLayoutManager);
        holder.nested_rv.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
        return busStopItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView busStopName, busStopAddress, arrow;
        RecyclerView nested_rv;
        boolean isExpanded;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            busStopName = itemView.findViewById(R.id.busStopName);
            busStopAddress = itemView.findViewById(R.id.address);
            nested_rv = itemView.findViewById(R.id.nested_rv);
            arrow = itemView.findViewById(R.id.arrow);
            isExpanded=false; //initialise all as collapsed
            updateExpandCollapseViews();
            itemView.setOnClickListener(v -> {
                isExpanded = !isExpanded;
                updateExpandCollapseViews();
            });
        }


        private void updateExpandCollapseViews() {
            // Update visibility of nested RecyclerView based on the expanded state
            nested_rv.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            // Update expand indicator text or icon based on the expanded state
            arrow.setText(isExpanded ? ">" : "v");
        }
    }
}