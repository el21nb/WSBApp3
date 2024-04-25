package com.example.wsbapp3.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsbapp3.activities.MainActivity;
import com.example.wsbapp3.models.Jacket;
import com.example.wsbapp3.database.JacketProvider;
import com.example.wsbapp3.models.PopupTextInput;
import com.example.wsbapp3.R;
import com.example.wsbapp3.models.Ticket;
import com.example.wsbapp3.database.TicketProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Fragment to assign or deassign a jacket to a child, after ticket scanned and info shown in TicketFragment.
 * Jacket code entered either by QR code or popUpTextInput
 * <p>
 * Scan process fragment order:
 * ScanFragment -> TicketFragment -> Assign Jacket Fragment -> ScanAgainFragment
 */
public class AssignJacketFragment extends Fragment {
    private EditText editText;
    private FirebaseFirestore db;

    //Ticket Id parameter, passed in from ScanFragment
    private static final String TICKET_ID = "ticketId";

    private String ticketId;

    private TextView scanTitle;

    private JacketProvider jacketProvider;

    private TicketProvider ticketProvider;
    PopupTextInput popup;

    Button scanJacketButton;

    Button findJacketButton;


    public AssignJacketFragment() {
        // Required empty public constructor
    }

    //constructor for popup dependency
    public AssignJacketFragment(PopupTextInput popup) {
        this.popup = popup;
    }

    /**
     * Create new instance of AssignJacketFragment, using TicketId paremeter
     *
     * @param ticketId Parameter 1.
     * @return A new instance of fragment AssignJacketFragment.
     */
    public static AssignJacketFragment newInstance(String ticketId) {
        AssignJacketFragment fragment = new AssignJacketFragment();
        Bundle args = new Bundle();
        args.putString(TICKET_ID, ticketId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * OnCreate, get input parameters
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ticketId = getArguments().getString(TICKET_ID);
        }
    }

    /**
     * OnCreateView:
     * set onclick listener for manual code entry button.
     * TODO: Set up QR Code input
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
        View view = inflater.inflate(R.layout.fragment_assign_jacket, container, false);
        findJacketButton = view.findViewById(R.id.findJacketButton);
        scanJacketButton = view.findViewById(R.id.scanJacketButton);
        db = FirebaseFirestore.getInstance();
        popup = new PopupTextInput();

        findJacketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFindJacketButtonClick();
            }
        });
        scanJacketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                IntentIntegrator intentIntegrator = new IntentIntegrator(requireActivity());
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scan Ticket QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.forSupportFragment(AssignJacketFragment.this).initiateScan();
                intentIntegrator.forSupportFragment(AssignJacketFragment.this).initiateScan();

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("scan", "in on ActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            Log.d("scan", "result non null");

            if (result.getContents() == null) {
                // If result is null, handle cancellation or error
                Toast.makeText(requireContext(), "Scan canceled", Toast.LENGTH_SHORT).show();
            } else {
                // Process the scanned data
                String jacketId = result.getContents();
                Log.d("scan", "got jacketId" + jacketId);

                if ( jacketId != null) {
                    // Input receive. If dropping off, check it is the right jacket:


                    JacketProvider jacketProvider = new JacketProvider();
                    jacketProvider.fetchJacketById(jacketId, new JacketProvider.FetchJacketCallback() {
                        @Override
                        public void onJacketFetched(Jacket jacket) {
                            String ticketId = getArguments().getString(TICKET_ID);
                            checkCorrectJacket(jacketId, ticketId);
                        }

                        @Override
                        public void onJacketNotFound() {
                            Toast.makeText(requireContext(), "Jacket not found", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFetchFailed(String errorMessage) {
                            Toast.makeText(requireContext(), "Jacket fetch failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Handle the case where the user didn't provide a valid jacketId
                    Toast.makeText(requireContext(), "Invalid Jacket Id", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // Call super method for other activity results
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * Handles click of manual jacket code entry button.
     * Calls popup dialog, and fetches text input.
     * Calls checkCorrectJacket to handle input.
     */
    private void handleFindJacketButtonClick() {
        popup = new PopupTextInput();
        String jacketId;
        // Show the popup and provide a callback for when the input is received
        popup.showPopupTextInput(requireContext(), "Enter Jacket Id", new PopupTextInput.InputCallback() {
            @Override
            public void onInput(String jacketId) { //Check the jacket exists
                if (jacketId != null) {
                    // Input receive. If dropping off, check it is the right jacket:


                    jacketProvider = new JacketProvider();
                    jacketProvider.fetchJacketById(jacketId, new JacketProvider.FetchJacketCallback() {
                        @Override
                        public void onJacketFetched(Jacket jacket) {
                            String ticketId = getArguments().getString(TICKET_ID);
                            checkCorrectJacket(jacketId, ticketId);
                        }

                        @Override
                        public void onJacketNotFound() {
                            Toast.makeText(requireContext(), "Jacket not found", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFetchFailed(String errorMessage) {
                            Toast.makeText(requireContext(), "Jacket fetch failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Handle the case where the user didn't provide a valid jacketId
                    Toast.makeText(requireContext(), "Invalid Jacket Id", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Checks the jacket code input from the manual input (and eventually QR scan)
     * Fetches ticket from fragment parameter.
     * Determines if morning/afternoon pickup/drop off, --> assignment or deassignment
     * If pickup --> calls next fragment, where jacket is assigned
     * If dropoff --> checks it is the right jacket for the ticket
     *
     * @param jacketId
     * @param ticketId
     */
    private void checkCorrectJacket(String jacketId, String ticketId) { //if pick up, calls next fragment, if drop off checks jacket is correct
        boolean correctJacket;
        ticketProvider = new TicketProvider();
        ticketProvider.fetchTicketById(ticketId, new TicketProvider.FetchTicketCallback() {
            @Override
            public void onTicketFetched(Ticket ticket) {
                if (ticket.isPickUp()) {
                    //if assigning jacket, go straight to next fragment
                    loadScanAgainFragment(jacketId, ticketId);
                } else {
                    //Else if deassigning, check it is the right jacket:
                    //Fetch passenger document using ticket Id
                    String childId = ticket.getChildId();
                    String journeyId = ticket.getJourneyId();
                    DocumentReference passengerDocumentRef = db.collection("Journeys").document(journeyId)
                            .collection("Passengers")
                            .document(childId);
                    passengerDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {

                                    //Fetch assigned jacked Id from passenger document
                                    String currentJacketId = document.getString("Jacket");

                                    // Check if the current Jacket field value matches the provided jacketId
                                    if (currentJacketId != null && currentJacketId.equals(jacketId)) {

                                        //if correct jacket, load next fragmentt
                                        loadScanAgainFragment(jacketId, ticketId);
                                    } else {

                                        //If incorrect jacket Toast error message
                                        Toast.makeText(requireContext(), "Wrong jacket!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.e("deassignJacket", "Document not found for passenger: " + childId);
                                    // Handle  case where the passenger document  not found
                                }
                            } else {
                                Log.e("deassignJacket", "Error getting passenger document: " + task.getException().getMessage());
                                // Handle exception as needed
                            }
                        }
                    });

                }
            }

            @Override
            public void onTicketNotFound() {
                // Handle case where ticket not found
            }

            @Override
            public void onFetchFailed(String errorMessage) {
            }
        });
    }

    /**
     * Once jacket validity confirmed, replace this with ScanAgainFragment to confirm assignment and offer onwards direction
     *
     * @param jacketId Id of assigned/deassigned jacket
     * @param ticketId Id of ticket
     */
    private void loadScanAgainFragment(String jacketId, String ticketId) {
        //Create new instance of scanAgainFragment,
        ScanAgainFragment scanAgainFragment = ScanAgainFragment.newInstance(jacketId, ticketId);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, scanAgainFragment);

        //  do not add to backstack, this fragment only accessible via route from ScanFragment
        fragmentTransaction.commit();
    }


}