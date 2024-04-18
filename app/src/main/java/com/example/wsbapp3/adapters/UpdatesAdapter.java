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

import com.example.wsbapp3.models.UpdatesListItem;
import com.example.wsbapp3.models.ChildListItem;
import com.example.wsbapp3.R;

import java.util.ArrayList;


/**
 * Adapter for the parent recyclerview in UpdatessFragment.
 * Sets up the parent RV and calls ChildAdapter to set up the nested RV in each parent item.
 * Code for nested RV based on
 * https://github.com/foxandroid/NestedRecyclerView/blob/master/app/src/main/res/layout/child_item.xml
 */


public class UpdatesAdapter extends RecyclerView.Adapter<UpdatesAdapter.ViewHolder> {
    private Activity activity;
    //Array of UpdatesListItem to populate the parent RV
    ArrayList<UpdatesListItem> UpdatesItemArrayList;

    //Array of arrays of ChildListItem objects to populate the nested RVs.
    //Each arraylist corresponds to one element of the UpdatesItemArrayList
    //FragmentManager passed in from UpdatessFragment, to be passed to the ChildAdapter
    private FragmentManager fragmentManager;

    public UpdatesAdapter() {
        //required empty constructor
    }

    //Constructor
    public UpdatesAdapter(Activity activity, FragmentManager fragmentManager, ArrayList<UpdatesListItem> updatesItemArrayList) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.UpdatesItemArrayList = updatesItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.updates_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //fetch the bus stop at the input position
        UpdatesListItem UpdatesItem = UpdatesItemArrayList.get(position);
        //set text to bus stop name and address
        holder.title.setText(UpdatesItem.getTitle());
        holder.message.setText(UpdatesItem.getMessage());



        //assign the adapter to the RV
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
    }

    //Return number of bus stops
    @Override
    public int getItemCount() {
        return UpdatesItemArrayList.size();
    }

    /**
     * ViewHolder class fetches the views from the layout
     * Sets onclick listeners to expand and collapse the parent items
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        //initialise views
        TextView title, message, arrow;

        //Flag to indicate if parent item is expanded
        boolean isExpanded;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Find views from layout
            title = itemView.findViewById(R.id.title);
            message = itemView.findViewById(R.id.message);
            arrow = itemView.findViewById(R.id.arrow);

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
            message.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            // update arrow indicate direction
            arrow.setText(isExpanded ? "v" : ">");
        }
    }
}