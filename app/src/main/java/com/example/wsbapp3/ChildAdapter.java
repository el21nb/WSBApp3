package com.example.wsbapp3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    ArrayList<ChildListItem> childListItemArrayList;

        private FragmentManager fragmentManager;
    private OnChildItemClickListener onChildItemClickListener;

        public ChildAdapter(ArrayList<ChildListItem> childListItemArrayList, FragmentManager fragmentManager) {
            this.childListItemArrayList = childListItemArrayList;
            this.fragmentManager = fragmentManager;
        }
    //https://stackoverflow.com/questions/71022273/nested-recyclerview-onclicklistener
    public interface OnChildItemClickListener {
        void onChildItemClick(String childId);
    }


    public void setOnChildItemClickListener(OnChildItemClickListener onChildItemClickListener) {
        this.onChildItemClickListener = onChildItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChildListItem childListItem = childListItemArrayList.get(position);
        holder.childName.setText(childListItem.childName);
        holder.childId.setText(childListItem.childId);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChildInfoFragment(childListItem.childId);
            }
        });    }

    @Override
    public int getItemCount() {
        return childListItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView childName, childId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            childName = itemView.findViewById(R.id.childName);
            childId = itemView.findViewById(R.id.childId);
        }
    }


    private void openChildInfoFragment(String childId) {
        // Use fragmentManager to open ChildInfoFragment
        ChildInfoFragment childInfoFragment = ChildInfoFragment.newInstance(childId);

        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, childInfoFragment)
                .addToBackStack(null)
                .commit();
    }
}
