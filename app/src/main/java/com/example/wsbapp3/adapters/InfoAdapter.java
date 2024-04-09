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

import com.example.wsbapp3.models.InfoListItem;
import com.example.wsbapp3.models.ChildListItem;
import com.example.wsbapp3.R;

import java.util.ArrayList;


/**
 * Adapter for the parent recyclerview in InfosFragment.
 * Sets up the parent RV and calls ChildAdapter to set up the nested RV in each parent item.
 * Code for nested RV based on
 * https://github.com/foxandroid/NestedRecyclerView/blob/master/app/src/main/res/layout/child_item.xml
 */


public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    private Activity activity;
    //Array of InfoListItem to populate the parent RV
    ArrayList<InfoListItem> InfoItemArrayList;

    //Array of arrays of ChildListItem objects to populate the nested RVs.
    //Each arraylist corresponds to one element of the InfoItemArrayList
    //FragmentManager passed in from InfosFragment, to be passed to the ChildAdapter
    private FragmentManager fragmentManager;

    public InfoAdapter() {
        //required empty constructor
    }

    //Constructor
    public InfoAdapter(Activity activity, FragmentManager fragmentManager, ArrayList<InfoListItem> infoItemArrayList) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.InfoItemArrayList = infoItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //fetch the bus stop at the input position
        InfoListItem InfoItem = InfoItemArrayList.get(position);
        //set text to bus stop name and address
        holder.heading.setText(InfoItem.getHeading());
        holder.body.setText(InfoItem.getBody());



        //assign the adapter to the RV
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
    }

    //Return number of bus stops
    @Override
    public int getItemCount() {
        return InfoItemArrayList.size();
    }

    /**
     * ViewHolder class fetches the views from the layout
     * Sets onclick listeners to expand and collapse the parent items
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        //initialise views
        TextView heading, body, arrow;

        //Flag to indicate if parent item is expanded
        boolean isExpanded;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Find views from layout
            heading = itemView.findViewById(R.id.heading);
            body = itemView.findViewById(R.id.body);
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
            body.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            // update arrow indicate direction
            arrow.setText(isExpanded ? "v" : ">");
        }
    }
}