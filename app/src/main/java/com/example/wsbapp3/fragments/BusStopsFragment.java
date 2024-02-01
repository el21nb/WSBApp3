package com.example.wsbapp3.fragments;

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

import com.example.wsbapp3.models.ChildListItem;
import com.example.wsbapp3.activities.MainActivity;
import com.example.wsbapp3.R;
import com.example.wsbapp3.adapters.BusStopAdapter;
import com.example.wsbapp3.models.BusStopListItem;
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
 * Fragment to display a recyclerview of bus stops on the journey and their arrival times.
 * Accessed via a button in RouteFragment
 * Each bus stop item has a nested recyclerview containing the children
 * booked for that stop.
 * Clicking a child item opens a ChildInfoFragment.
 * Nested RV code adapted from:
 * https://github.com/foxandroid/NestedRecyclerView/blob/master/app/src/main/res/layout/list_item.xml
 */
public class BusStopsFragment extends Fragment {

    //Adapter for the parent RV
    BusStopAdapter busStopAdapter;

    //List of Bus Stops
    ArrayList<BusStopListItem> busStopListItemArrayList;


    //Parent recyclerview
    RecyclerView rvParent;

    public BusStopsFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of this fragment.
     *
     * @return new fragment
     */
    public static BusStopsFragment newInstance() {
        BusStopsFragment fragment = new BusStopsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Called when fragment layout is created.
     * Sets the textview to display the current date.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return
     */
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

        //Initialise bus stop list for parent rv
        busStopListItemArrayList = new ArrayList<>();

        //initialise list of lists of children for nested rvs
        ArrayList<ArrayList<ChildListItem>> childListItemArrayList = new ArrayList<>();  // Initialize here

        //call method to populate the recyclerviews
        populateBusStopList();

        return v;
    }

    /**
     * Populates the parent and nested recyckerviews from the database
     */
    public void populateBusStopList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // Initialize Firestore
        busStopListItemArrayList = new ArrayList<>();
        ArrayList<ArrayList<ChildListItem>> childListItemArrayList = new ArrayList<>();

        //Fetch journey bus stops and order by arrival time
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

                            //Counter to ensure all children fetched before fetching next bus stop
                            AtomicInteger completedBusStops = new AtomicInteger(0);

                            //Iterate through journey bus stops
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> busStopData = (Map<String, Object>) document.get("busStop");

                                //Fetch bus stop data
                                if (busStopData != null) {
                                    String arrivalTime = document.getString("arrivalTime");
                                    String busStopName = (String) busStopData.get("name");
                                    String busStopAddress = (String) busStopData.get("address");
                                    String busStopId = (String) busStopData.get("id");
                                    Log.d("FetchChildren", "Fetched bus stop " + busStopName);

                                    //Create busStopListItem
                                    BusStopListItem busStopListItem = new BusStopListItem(arrivalTime + "- " + busStopName, busStopAddress);
                                    busStopListItemArrayList.add(busStopListItem);

                                    // Initialize child list for this bus stop
                                    ArrayList<ChildListItem> childrenList = new ArrayList<>();
                                    // Get the index of the current bus stop
                                    int currentBusStopIndex = busStopListItemArrayList.size() - 1;

                                    // Add an empty list for each bus stop
                                    childListItemArrayList.add(new ArrayList<>());

                                    //Fetch busStopChildren
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

                                                        //Iterate through busStopChildren
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            String name = document.getString("firstName") + " " + document.getString("lastName");
                                                            String childId = document.getId();
                                                            ChildListItem childListItem = new ChildListItem(name, childId);

                                                            //Add child to list
                                                            childrenList.add(childListItem);
                                                            Log.d("FetchChildren", "adding child to array " + childrenList.size());
                                                        }

                                                        //Add list of children to ArrayList of children lists, at the corresponding position to the bus stop
                                                        if (currentBusStopIndex < childListItemArrayList.size()) {
                                                            childListItemArrayList.set(currentBusStopIndex, childrenList);
                                                        }

                                                        // Check if all bus stops are processed
                                                        if (completedBusStops.incrementAndGet() == totalBusStops) {
                                                            // When all bus stops processed, set up the adapter and RecyclerView
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