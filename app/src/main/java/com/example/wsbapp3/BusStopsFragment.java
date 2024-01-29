package com.example.wsbapp3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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

    private View.OnClickListener onItemClickListener;

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
    View v = inflater.inflate(R.layout.fragment_bus_stops, container, false);
    rvParent = v.findViewById(R.id.RVparent);

    TextView currentDateTextView = v.findViewById(R.id.datetext);

    // Get the current date
    Date currentDate = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("EEE a, d MMMM yyyy", Locale.getDefault());
    String formattedDate = sdf.format(currentDate);

    // Set the formatted date to the TextView
    currentDateTextView.setText(formattedDate);

    busStopListItemArrayList = new ArrayList<>();
    ArrayList<ArrayList<ChildListItem>> childListItemArrayList = new ArrayList<>();  // Initialize here

    populateBusStopList();

    return v;
}

    public void populateBusStopList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // Initialize Firestore
        busStopListItemArrayList = new ArrayList<>();
        ArrayList<ArrayList<ChildListItem>> childListItemArrayList = new ArrayList<>();
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

                                    // Add an empty list for each bus stop
                                    childListItemArrayList.add(new ArrayList<>());

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
                                                            busStopAdapter = new BusStopAdapter(requireActivity(), busStopListItemArrayList, childListItemArrayList, requireActivity().getSupportFragmentManager());
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

    }


}