package com.example.wsbapp3.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import com.example.wsbapp3.R;
import com.example.wsbapp3.database.TicketProvider;
import com.example.wsbapp3.database.ChildProvider;
import com.example.wsbapp3.database.JourneyProvider;
import com.example.wsbapp3.models.Child;
import com.example.wsbapp3.models.Journey;
import com.example.wsbapp3.models.Ticket;

/**
 * Shown after ScanFragment.
 * Calls pickUpPassenger or dropOffPassenger to write to database.
 * Confirms passenger picked up/dropped off onscreen.
 * Button directs to AssignJacketFragment.
 */
public class TicketFragment extends Fragment {

    //Input parameter:
    private static final String TICKET_ID = "ticketId";
    private String ticketId;

    //Fragment member objects
    private Ticket ticket;
    private Child child;
    private Journey journey;

    TextView testText;
    Button jacketButton;

    public TicketFragment() {
        // Required empty public constructor
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ticketId Parameter 1.
     * @return A new instance of fragment TicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketFragment newInstance(String ticketId) {
        TicketFragment fragment = new TicketFragment();
        Bundle args = new Bundle();
        args.putString(TICKET_ID, ticketId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Calls SetObjects to fetch child, jacket and ticket objects from database.
     * Calls pickUpPassenger or dropOffPassenger depending on ticket.
     * Sets assign jacket button onClickListener
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
        // Inflate the layout
        View v = inflater.inflate(R.layout.fragment_ticket, container, false);

        // Create a callback for when setObjects is complete
        SetObjectsCallback setObjectsCallback = new SetObjectsCallback() {
            @Override
            public void onSetObjectsComplete() {
                String testString = "";
                // Once setObjects complete, create the testString using the fragment member objects
                testString = "Child: " + child.getFirstName() + " " + child.getLastName()
                        + "\nJourney: outbound = " + journey.is0utwardJourney() + " " + journey.getJourneyDateTime();
                // Update the UI with the testString
                TextView testText = v.findViewById(R.id.testText);
                if (testText != null) {
                    testText.setText(testString);
                }

                //get journey Id from ticket
                JourneyProvider provider = new JourneyProvider();
                String journeyId = ticket.getJourneyId();

                if (ticket.isPickUp()) { //PICK UP CHILD
                    Log.d("Passengers", "ticket.isPickUp()");
                    provider.pickUpPassenger(journeyId, child);
                } else { //DROP OFFF CHILD
                    Log.d("Passengers", "ticket.is Drop Off");
                    provider.dropOffPassenger(journeyId, child.getId());
                }
            }

        };

        if (getArguments() != null) {
            // Pass the callback to setObjects
            setObjects(setObjectsCallback);
        }

        //set assign jacket button onclick listener
        jacketButton = v.findViewById(R.id.jacketButton);
        jacketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAssignJacketButtonClick();
            }
        });


        return v;
    }

    /**
     * Handles click of assign jacket button.
     * Creates new AssignJacketFragment and passes in ticketId
     */
    private void onAssignJacketButtonClick() {
        // Create a new AssignJacketFragment and pass in the ticketId
        AssignJacketFragment assignJacketFragment = AssignJacketFragment.newInstance(ticketId);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, assignJacketFragment)
                .commit();
    }

    /**
     * callback ensures that all asynchronous operations in setObjects are complete before calling pickUpPassenger etc
     */
    public interface SetObjectsCallback {
        void onSetObjectsComplete();
    }

    /**
     * fetches Child, Jacket and Ticket objects from the database, using their Ids.
     * Stores them as member variables of this fragment.
     *
     * @param callback
     */
    public void setObjects(SetObjectsCallback callback) {
        ticketId = getArguments().getString(TICKET_ID);
        TicketProvider tProvider = new TicketProvider();
        ChildProvider cProvider = new ChildProvider();
        JourneyProvider jProvider = new JourneyProvider();
        tProvider.fetchTicketById(ticketId, new TicketProvider.FetchTicketCallback() {

            //Fetch and set ticket object
            @Override
            public void onTicketFetched(Ticket ticket) {
                setTicket(ticket);
                if (ticket.isPickUp()) {
                    jacketButton.setText("Assign Jacket");
                } else {
                    jacketButton.setText("De-assign Jacket");
                }
                // now fetch and set child object
                String childId = ticket.getChildId();
                cProvider.fetchChildById(childId, new ChildProvider.FetchChildCallback() {
                    @Override
                    public void onChildFetched(Child child) {
                        setChild(child);
                        Log.d("TicketFragment", "Child fetched: " + child.toString());
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

                // now fetch and set journey object
                String journeyId = ticket.getJourneyId();
                jProvider.fetchJourneyById(journeyId, new JourneyProvider.FetchJourneyCallback() {
                    @Override
                    public void onJourneyFetched(Journey journey) {
                        setJourney(journey);

                        // Callback when all operations are complete
                        callback.onSetObjectsComplete();
                    }

                    @Override
                    public void onJourneyNotFound() {
                        // Handle case where journey not found
                        Log.d("TicketFragment", "Journey not found");
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
                // Handle case where ticket not found
            }

            @Override
            public void onFetchFailed(String errorMessage) {
                // Handle fetch failure
            }
        });
    }
}