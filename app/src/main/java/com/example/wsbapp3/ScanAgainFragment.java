package com.example.wsbapp3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanAgainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanAgainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String JACKET_ID = "jacketId";
    private static final String TICKET_ID = "ticketId";

    // TODO: Rename and change types of parameters
    private String jacketId;
    private String ticketId;

    TextView testText;
    Button passengersButton;
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
    // TODO: Rename and change types and number of parameters
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
                // Handle button click here
                onPassengersButtonClick();
            }
        });
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click here
                onScanButtonClick();
            }
        });

        //Fill in confimration string
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
                        if(ticket.isPickUp()) { //if pick up, assign jacket to passenger
                            jProvider.assignJacket(journeyId, childId, jacketId);
                        }
                        else{
                            jProvider.deassignJacket(journeyId, childId, jacketId);
                        }
                        cProvider.fetchChildById(childId, new ChildProvider.FetchChildCallback() {
                            @Override
                            public void onChildFetched(Child child) {
                                String testString;
                                if( ticket.isPickUp() ){
                                    testString = "Jacket " + jacketId + " assigned to\n" + child.getFirstName() + " " + child.getLastName();
                                    testText.setText(testString);
                                }
                                else{
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

    private void onPassengersButtonClick() {
        // Create a new AssignJacketFragment and pass in the ticketId
        PassengersFragment passengersFragment = PassengersFragment.newInstance("","");
        // Use FragmentManager to replace the current fragment with AssignJacketFragment
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, passengersFragment)
                //.addToBackStack(null)  // Add to back stack so the user can navigate back
                .commit();
    }

    private void onScanButtonClick() {
        // Create a new ScanFragment and pass in the jacketId and ticketId
        ScanFragment scanFragment = ScanFragment.newInstance("jacketId", "ticketId");

        // Use FragmentManager to replace the current fragment with ScanFragment
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, scanFragment)
                //.addToBackStack(null)  // Add to back stack so the user can navigate back
                .commit();
    }
}