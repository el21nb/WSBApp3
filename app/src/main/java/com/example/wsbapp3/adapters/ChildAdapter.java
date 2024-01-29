package com.example.wsbapp3.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wsbapp3.fragments.ChildInfoFragment;
import com.example.wsbapp3.models.ChildListItem;
import com.example.wsbapp3.R;

import java.util.ArrayList;

/**
 * Adapter for the nested recycler views, containing a list of children at each bus stop,
 * in BusStopsFragment.
 * Handles the display of child items and sets up onClickListeners to open ChildInfoFragments
 * Code for onClickListeners  based on
 * https://stackoverflow.com/questions/71022273/nested-recyclerview-onclicklistener
 */
public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {
    //List of children at an individual bus stop
    ArrayList<ChildListItem> childListItemArrayList;

    //FragmentManager passed from the fragment, via the BusStopAdapter, allows ChildAdapter to load new fragment
    private FragmentManager fragmentManager;

    //On click listener for a nested child item
    private OnChildItemClickListener onChildItemClickListener;


    /**
     * Constructor for ChildAdapter.
     *
     * @param childListItemArrayList list of children at an individual bus stop
     * @param fragmentManager        FragmentManager to open ChildInfoFragment
     */
    public ChildAdapter(ArrayList<ChildListItem> childListItemArrayList, FragmentManager fragmentManager) {
        this.childListItemArrayList = childListItemArrayList;
        this.fragmentManager = fragmentManager;
    }

    /**
     * interface to define the onClick callback method
     */
    public interface OnChildItemClickListener {
        void onChildItemClick(String childId);
    }

    /**
     * sets the onClickListener for the nested child items
     *
     * @param onChildItemClickListener listener to be set
     */
    public void setOnChildItemClickListener(OnChildItemClickListener onChildItemClickListener) {
        this.onChildItemClickListener = onChildItemClickListener;
    }

    /**
     * Creates a viewholder for a child item in the nested RV
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Displays the item data at the specified position.
     * Sets the onClickListener
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChildListItem childListItem = childListItemArrayList.get(position);
        holder.childName.setText(childListItem.getChildName());
        holder.childId.setText(childListItem.getChildId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChildInfoFragment(childListItem.getChildId());
            }
        });
    }

    /**
     * @return number of children in nested list
     */
    @Override
    public int getItemCount() {
        return childListItemArrayList.size();
    }

    /**
     * View holder class, to display the ChildListItems
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView childName, childId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            childName = itemView.findViewById(R.id.childName);
            childId = itemView.findViewById(R.id.childId);
        }
    }

    /**
     * Opens ChildInfoFragment, passing in the ID of the clicked child.
     *
     * @param childId The ID of the child to be displayed.
     */
    private void openChildInfoFragment(String childId) {
        // Use fragmentManager to open ChildInfoFragment
        ChildInfoFragment childInfoFragment = ChildInfoFragment.newInstance(childId);

        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, childInfoFragment)
                .addToBackStack(null)
                .commit();
    }
}
