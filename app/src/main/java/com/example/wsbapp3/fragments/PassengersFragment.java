package com.example.wsbapp3.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wsbapp3.R;
import com.example.wsbapp3.activities.MainActivity;
import com.example.wsbapp3.adapters.PassengerAdapter;
import com.example.wsbapp3.database.JourneyProvider;
import com.example.wsbapp3.models.Child;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a recyclerview list of onboard passengers and their assigned jackets.
 * Clicking an item opens a ChildInfoFragment and passes in the childId
 */
public class PassengersFragment extends Fragment {

    //List of string arrays of passenger data
    List<String[]> passengerList;
    private RecyclerView recyclerView;
    private PassengerAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Creates a new instance of the fragment
     *
     * @return the fragment
     */
    public static PassengersFragment newInstance() {
        PassengersFragment fragment = new PassengersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }
    /**injection getter/setter for testing*/
    public FirebaseFirestore getDb() {
        return db;
    }

    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }

    /**
     * Inflates the layout view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_passengers, container, false);
        return v;
    }

    /**
     * Finds the recyclerview.
     * Calls readPassengers() to populate the list.
     * Sets the adapter and subsequently the recyclerview.
     * Sets onclick listeners for the child items.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        // Initialize passengerList
        passengerList = new ArrayList<>();

        // Initialize adapter with passengerList
        adapter = new PassengerAdapter(passengerList, new PassengerAdapter.OnItemClickListener() {

            //Set onclick listeners for the child items.
            @Override
            public void onItemClick(String childId) {
                ChildInfoFragment childInfoFragment = ChildInfoFragment.newInstance(childId);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, childInfoFragment)
                        .addToBackStack(null)  // Add to back stack so the user can navigate back
                        .commit();
            }
        });
        //Set recyclerview to hold the adapter.
        recyclerView.setAdapter(adapter);

        // call readPassengers to update passengerList.
        readPassengers(((MainActivity) requireActivity()).getCurrentJourneyId());
    }

    /**
     * Populates passengerList with data from the passengers subcollection in the journey document
     *
     * @param journeyId id of the journey document to enter
     */
    public void readPassengers(String journeyId) {
        JourneyProvider provider = new JourneyProvider();

        db.collection("Journeys")
                .document(journeyId)
                .collection("Passengers")
                .whereEqualTo("dropOffTime", null) // only passengers who have not been dropped off
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            passengerList.clear();
                            for (QueryDocumentSnapshot passengerDocument : task.getResult()) {
                                String jacket = passengerDocument.getString("Jacket");
                                // Use toObject to convert the HashMap to a Child object
                                Child child = passengerDocument.get("Child", Child.class);
                                if (child != null) {
                                    String firstName = child.getFirstName();
                                    String lastName = child.getLastName();
                                    String id = child.getId();

                                    String[] itemString = new String[3];
                                    itemString[0] = firstName + " " + lastName + " (" + id + " )";
                                    itemString[1] = "Jacket: " + jacket;
                                    itemString[2] = id;
                                    passengerList.add(itemString);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("RBSD", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

}



