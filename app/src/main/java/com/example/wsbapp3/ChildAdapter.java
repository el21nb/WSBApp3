package com.example.wsbapp3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    ArrayList<ChildListItem> childListItemArrayList;

    public ChildAdapter(ArrayList<ChildListItem> childListItemArrayList) {
        this.childListItemArrayList = childListItemArrayList;
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
    }

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
}
