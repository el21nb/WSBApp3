package com.example.wsbapp3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TicketFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TICKET_ID = "ticketId";

    // TODO: Rename and change types of parameters
    private String ticketId;
    private Ticket ticket;
    private Child child;
    private Journey journey;

    TextView testText;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v  = inflater.inflate(R.layout.fragment_ticket, container, false);

        // Create a callback to be called when setObjects is complete
        SetObjectsCallback setObjectsCallback = new SetObjectsCallback() {
            @Override
            public void onSetObjectsComplete() {
                String testString = "";
                // Now that setObjects is complete, create the testString
                testString = "Child: " + child.getFirstName() + " " + child.getLastName()
                        + "\nJourney: outbound = " +  journey.is0utwardJourney() + " " + journey.getJourneyDateTime();
                // Update the UI with the testString
                TextView testText = v.findViewById(R.id.testText);
                if (testText != null) {
                    testText.setText(testString);
                }

                //add child to onboard collection in journey
                JourneyProvider provider = new JourneyProvider();
                String journeyId = ticket.getJourneyId();
                provider.addOnboardChild(journeyId,child);
            }

        };

        if (getArguments() != null) {
            // Pass the callback to setObjects
            setObjects(setObjectsCallback);
        }
        return v;
    }

    public interface SetObjectsCallback { //callback ensures that all asynchronous operations in setObjects are complete
        void onSetObjectsComplete();
    }

    public void setObjects(SetObjectsCallback callback) {
        ticketId = getArguments().getString(TICKET_ID);
        TicketProvider tProvider = new TicketProvider();
        ChildProvider cProvider = new ChildProvider();
        JourneyProvider jProvider = new JourneyProvider();
        tProvider.fetchTicketById(ticketId, new TicketProvider.FetchTicketCallback() {
            @Override
            public void onTicketFetched(Ticket ticket) {
                setTicket(ticket);

                // now fetch and set child object
                String childId = ticket.getChildId();
                cProvider.fetchChildById(childId, new ChildProvider.FetchChildCallback() {
                    @Override
                    public void onChildFetched(Child child) {
                        // Do something with the fetched child
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

                        // Call the callback when all operations are complete
                        callback.onSetObjectsComplete();
                    }

                    @Override
                    public void onJourneyNotFound() {
                        // Handle case where journey is not found
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
                // Handle case where ticket is not found
            }

            @Override
            public void onFetchFailed(String errorMessage) {
                // Handle fetch failure
            }
        });
    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View v  = inflater.inflate(R.layout.fragment_ticket, container, false);
//        String testString = "";
//
//        if (getArguments() != null) {
//            setObjects();
//            testString = "Child: " + child.getFirstName() + " " + child.getLastName()
//                    + "\nJourney: " + journey.getId() + " " + journey.getJourneyDateTime();
//        }
//
//        TextView testText = v.findViewById(R.id.testText);  // Correct method name
//        testText.setText(testString);
//        return v;
//    }
//
//
//    public void setObjects(){ //first fetch ticket object, and set member Ticket, then use ticket info to fetch child and journey objects
//        ticketId = getArguments().getString(TICKET_ID);
//        TicketProvider tProvider = new TicketProvider();
//        ChildProvider cProvider = new ChildProvider();
//        JourneyProvider jProvider = new JourneyProvider();
//        tProvider.fetchTicketById(ticketId, new TicketProvider.FetchTicketCallback() {
//            @Override
//            public void onTicketFetched(Ticket ticket) {
//                setTicket(ticket); //set fragment member variable ticket to the ticket object from firestore
//
//                //now fetch and set child object
//                String childId = ticket.getChildId();
//                cProvider.fetchChildById(childId, new ChildProvider.FetchChildCallback() {
//                    @Override
//                    public void onChildFetched(Child child) {
//                        // Do something with the fetched child
//                        setChild(child);
//                        Log.d("TicketFragment", "Child fetched: " + child.toString());
//                    }
//
//                    @Override
//                    public void onChildNotFound() {
//                        // Handle case where child is not found
//                        Log.d("TicketFragment", "Child not found");
//                    }
//
//                    @Override
//                    public void onFetchFailed(String errorMessage) {
//                        // Handle fetch failure
//                        Log.e("TicketFragment", "Fetch failed: " + errorMessage);
//                    }
//                });
//
//                //now fetch and set journey object
//                String journeyId = ticket.getJourneyId();
//                jProvider.fetchJourneyById(journeyId, new JourneyProvider.FetchJourneyCallback() {
//                    @Override
//                    public void onJourneyFetched(Journey journey) {
//                        setJourney(journey); //set fragment member variable ticket to the ticket object from firestore
//                    }
//                    @Override
//                    public void onJourneyNotFound() {
//                        // Handle case where child is not found
//                        Log.d("TicketFragment", "Child not found");
//                    }
//                    @Override
//                    public void onFetchFailed(String errorMessage) {
//                        // Handle fetch failure
//                        Log.e("TicketFragment", "Fetch failed: " + errorMessage);
//                    }
//                });
//
//            }
//            @Override
//            public void onTicketNotFound() {
//            }
//            @Override
//            public void onFetchFailed(String errorMessage) {
//            }
//        });
//        }
}
