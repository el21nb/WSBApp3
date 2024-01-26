package com.example.wsbapp3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BusStopsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//https://github.com/foxandroid/NestedRecyclerView/blob/master/app/src/main/res/layout/list_item.xml
public class BusStopsFragment extends Fragment {

    BusStopAdapter busStopAdapter;
    ArrayList<BusStopListItem> busStopListItemArrayList;
    ArrayList<ChildListItem> childListItemArrayList;
    RecyclerView rvParent;
    public BusStopsFragment() {
        // Required empty public constructor
    }

    public static BusStopsFragment newInstance() {
        BusStopsFragment fragment = new BusStopsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_passengers, container, false);
        rvParent = v.findViewById(R.id.recyclerview);
        //implement db name here
        String[] busStopName = {"ab","cd","ef","gh"};
        String[] busStopAddress = {"abc","def","ghk","lmn"};
        busStopListItemArrayList = new ArrayList<>();
        childListItemArrayList = new ArrayList<>();
        for(int i = 0; i<busStopName.length;i++){
            BusStopListItem busStopListItem = new BusStopListItem(busStopName[i], busStopAddress[i]);
            busStopListItemArrayList.add(busStopListItem);
            if(i<busStopAddress.length){
                ChildListItem childListItem = new ChildListItem(busStopName[i], busStopAddress[i]);
                childListItemArrayList.add(childListItem);
            }
        }
        busStopAdapter = new BusStopAdapter(requireActivity(),busStopListItemArrayList,childListItemArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        rvParent.setLayoutManager(linearLayoutManager);
        rvParent.setAdapter(busStopAdapter);

        return v;


    }

}