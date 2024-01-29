package com.example.wsbapp3.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wsbapp3.R;
import com.example.wsbapp3.models.Ticket;
import com.example.wsbapp3.database.TicketProvider;
import com.example.wsbapp3.database.ChildProvider;
import com.example.wsbapp3.database.JourneyProvider;
import com.example.wsbapp3.models.Child;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * ScanAgainFragment- confirms jacket assignment.
 * Buttons to redirect either to ScanFragment or PassengersFragment.
 * TODO: update the UI.
 */
public class ScanAgainFragment extends Fragment {

    //Parameter arguments, passed from AssignJacketFragment
    private static final String JACKET_ID = "jacketId";
    private static final String TICKET_ID = "ticketId";
    private String jacketId;
    private String ticketId;

    TextView testText;

    //button to view passenger list
    Button passengersButton;

    //Button to scan a new ticket
    Button scanButton;

    public ScanAgainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param jacketId Parameter 1.
     * @param ticketId Parameter 2.
     * @return A new instance of fragment ScanAgainFragment.
     */
    public static ScanAgainFragment newInstance(String jacketId, String ticketId) {
        ScanAgainFragment fragment = new ScanAgainFragment();
        Bundle args = new Bundle();
        args.putString(JACKET_ID, jacketId);
        args.putString(TICKET_ID, ticketId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Find the views and set onClickListeners for the buttons
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scan_again, container, false);
        testText = v.findViewById(R.id.testText);
        passengersButton = v.findViewById(R.id.passengersButton);
        scanButton = v.findViewById(R.id.ticketButton);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        passengersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPassengersButtonClick();
            }
        });
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onScanButtonClick();
            }
        });

        //Fill in jacket assigned confirmation string
        if (getArguments() != null) {
            jacketId = getArguments().getString(JACKET_ID);
            ticketId = getArguments().getString(TICKET_ID);

            {
                ticketId = getArguments().getString(TICKET_ID);
                TicketProvider tProvider = new TicketProvider();
                ChildProvider cProvider = new ChildProvider();
                JourneyProvider jProvider = new JourneyProvider();
                tProvider.fetchTicketById(ticketId, new TicketProvider.FetchTicketCallback() {
                    @Override
                    public void onTicketFetched(Ticket ticket) {
                        String journeyId = ticket.getJourneyId();
                        String childId = ticket.getChildId();
                        if (ticket.isPickUp()) { //if pick up, assign jacket to passenger
                            jProvider.assignJacket(journeyId, childId, jacketId);
                        } else {
                            jProvider.deassignJacket(journeyId, childId, jacketId);
                        }
                        cProvider.fetchChildById(childId, new ChildProvider.FetchChildCallback() {
                            @Override
                            public void onChildFetched(Child child) {
                                String testString;
                                if (ticket.isPickUp()) {
                                    testString = "Jacket " + jacketId + " assigned to\n" + child.getFirstName() + " " + child.getLastName();
                                    testText.setText(testString);
                                } else {
                                    testString = "Jacket " + jacketId + " de-assigned from\n" + child.getFirstName() + " " + child.getLastName();
                                    testText.setText(testString);
                                }
                            }

                            @Override
                            public void onChildNotFound() {
                                // Handle case where child is not found
                                Log.d("TicketFragment", "Child not found");
                            }

                            @Override
                            public void onFetchFailed(String errorMessage) {
                                // Handle fetch failure
                                Log.e("TicketFragment", "Fetch failed: " + errorMessage);
                            }
                        });

                    }

                    @Override
                    public void onTicketNotFound() {
                        // Handle case where ticket is not found
                    }

                    @Override
                    public void onFetchFailed(String errorMessage) {
                        // Handle fetch failure
                    }
                });
            }
        }
        return v;
    }

    /**
     * Handles passenger button click.
     * Replaces fragment with passengers fragment.
     */
    private void onPassengersButtonClick() {
        PassengersFragment passengersFragment = PassengersFragment.newInstance();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, passengersFragment)
                //.addToBackStack(null)  // Don't add to backstack. This only accessible via ScanFragment
                .commit();
    }

    /**
     * Hanles scan button click.
     * Replaces fragment with ScanFragment
     */
    private void onScanButtonClick() {
        ScanFragment scanFragment = ScanFragment.newInstance();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, scanFragment)
                //.addToBackStack(null)  // Don't add to backstack. This only accessible via ScanFragment
                .commit();
    }
}