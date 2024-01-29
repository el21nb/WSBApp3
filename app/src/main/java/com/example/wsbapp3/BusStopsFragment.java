package com.example.wsbapp3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
    FirebaseFirestore db;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_passengers, container, false);
        rvParent = v.findViewById(R.id.recyclerview);
        busStopListItemArrayList = new ArrayList<>();
        ArrayList<ArrayList<ChildListItem>> childListItemArrayList = new ArrayList<>();  // Initialize here

        FirebaseFirestore db = FirebaseFirestore.getInstance(); // Initialize Firestore

        db.collection("Journeys")
                .document(((MainActivity) requireActivity()).getCurrentJourneyId())
                .collection("JourneyBusStops")
                .orderBy("arrivalTime")
                .get()
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int totalBusStops = task.getResult().size();
                            AtomicInteger completedBusStops = new AtomicInteger(0);

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> busStopData = (Map<String, Object>) document.get("busStop");

                                if (busStopData != null) {
                                    String arrivalTime = document.getString("arrivalTime");
                                    String busStopName = (String) busStopData.get("name");
                                    String busStopAddress = (String) busStopData.get("address");
                                    String busStopId = (String) busStopData.get("id");
                                    Log.d("FetchChildren", "Fetched bus stop " + busStopName);

                                    BusStopListItem busStopListItem = new BusStopListItem(arrivalTime + "- " + busStopName, busStopAddress);
                                    busStopListItemArrayList.add(busStopListItem);

                                    // Initialize child list for each bus stop
                                    ArrayList<ChildListItem> childrenList = new ArrayList<>();

                                    int currentBusStopIndex = busStopListItemArrayList.size() - 1; // Get the index of the current bus stop

                                    db.collection("Journeys")
                                            .document(((MainActivity) requireActivity()).getCurrentJourneyId())
                                            .collection("JourneyBusStops")
                                            .document(busStopId)
                                            .collection("busStopChildren")
                                            .get()
                                            .addOnCompleteListener(requireActivity(), new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            String name = document.getString("firstName") + " " + document.getString("lastName");
                                                            String childId = document.getId();
                                                            ChildListItem childListItem = new ChildListItem(name, childId);
                                                            childrenList.add(childListItem);
                                                            Log.d("FetchChildren", "adding child to array " + childrenList.size());
                                                        }

                                                        // Assign the children list to the specific bus stop
                                                        if (currentBusStopIndex < childListItemArrayList.size()) {
                                                            childListItemArrayList.set(currentBusStopIndex, childrenList);
                                                        }

                                                        // Check if all bus stops are processed
                                                        if (completedBusStops.incrementAndGet() == totalBusStops) {
                                                            // All bus stops processed, set up the adapter and RecyclerView
                                                            busStopAdapter = new BusStopAdapter(requireActivity(), busStopListItemArrayList, childListItemArrayList);
                                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
                                                            rvParent.setLayoutManager(linearLayoutManager);
                                                            rvParent.setAdapter(busStopAdapter);
                                                        }
                                                    } else {
                                                        Log.d("FetchChildren", "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    }
                });

        return v;
    }
}
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_passengers, container, false);
//        rvParent = v.findViewById(R.id.recyclerview);
//        busStopListItemArrayList = new ArrayList<>();
//        ArrayList<ArrayList<ChildListItem>> childListItemArrayList = new ArrayList<>();  // Initialize here
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance(); // Initialize Firestore
//
//        db.collection("Journeys")
//                .document(((MainActivity) requireActivity()).getCurrentJourneyId())
//                .collection("JourneyBusStops")
//                .orderBy("arrivalTime")
//                .get()
//                .addOnCompleteListener(requireActivity(), new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            int totalBusStops = task.getResult().size();
//                            AtomicInteger completedBusStops = new AtomicInteger(0);
//
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Map<String, Object> busStopData = (Map<String, Object>) document.get("busStop");
//
//                                if (busStopData != null) {
//                                    String arrivalTime = document.getString("arrivalTime");
//                                    String busStopName = (String) busStopData.get("name");
//                                    String busStopAddress = (String) busStopData.get("address");
//                                    String busStopId = (String) busStopData.get("id");
//                                    Log.d("FetchChildren", "Fetched bus stop " + busStopName);
//
//                                    BusStopListItem busStopListItem = new BusStopListItem(arrivalTime + "- " + busStopName, busStopAddress);
//                                    busStopListItemArrayList.add(busStopListItem);
//
//                                    // Initialize child list for each bus stop
//                                    ArrayList<ChildListItem> childrenList = new ArrayList<>();
//
//                                    db.collection("Journeys")
//                                            .document(((MainActivity) requireActivity()).getCurrentJourneyId())
//                                            .collection("JourneyBusStops")
//                                            .document(busStopId)
//                                            .collection("busStopChildren")
//                                            .get()
//                                            .addOnCompleteListener(requireActivity(), new OnCompleteListener<QuerySnapshot>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                    if (task.isSuccessful()) {
//                                                        for (QueryDocumentSnapshot document : task.getResult()) {
//                                                            String name = document.getString("firstName") + " " + document.getString("lastName");
//                                                            String childId = document.getId();
//                                                            ChildListItem childListItem = new ChildListItem(name, childId);
//                                                            childrenList.add(childListItem);
//                                                            Log.d("FetchChildren", "adding child to array " + childrenList.size());
//                                                        }
//
//                                                        // Assign the children list to the specific bus stop
//                                                        int busStopIndex = busStopListItemArrayList.indexOf(busStopListItem);
//                                                        if (busStopIndex != -1) {
//                                                            childListItemArrayList.add(childrenList);
//                                                            // Notify adapter after adding all children
//                                                            if (busStopAdapter != null) {
//                                                                busStopAdapter.notifyDataSetChanged();
//                                                            }
//                                                        }
//
//                                                        // Check if all bus stops are processed
//                                                        if (completedBusStops.incrementAndGet() == totalBusStops) {
//                                                            // All bus stops processed, set up the adapter and RecyclerView
//                                                            busStopAdapter = new BusStopAdapter(requireActivity(), busStopListItemArrayList, childListItemArrayList);
//                                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
//                                                            rvParent.setLayoutManager(linearLayoutManager);
//                                                            rvParent.setAdapter(busStopAdapter);
//                                                        }
//                                                    } else {
//                                                        Log.d("FetchChildren", "Error getting documents: ", task.getException());
//                                                    }
//                                                }
//                                            });
//                                }
//                            }
//                        }
//                    }
//                });
//
//        return v;
//    }
//}