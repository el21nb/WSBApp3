package com.example.wsbapp3.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;
//import com.example.wsbapp3.Manifest;
import com.example.wsbapp3.R;
import com.example.wsbapp3.models.Ticket;
import com.example.wsbapp3.database.TicketProvider;
import com.example.wsbapp3.activities.MainActivity;
import com.example.wsbapp3.models.PopupTextInput;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

/**
 * ScanFragment- User can scan a QR Code ticket or manually enter code.
 * Checks ticket validity against database, then passes the ticketId to a new TicketFragment.
 * TODO: set up QR code scanner
 */
public class ScanFragment extends Fragment {
    private EditText editText;
    private FirebaseFirestore db;

    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    String intentData = "";

    Button findTicketButton;
    Button scanTicketButton;

    public ScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment ScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanFragment newInstance() {
        ScanFragment fragment = new ScanFragment();
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

    /**
     * Inflate layout, find views, set button onclicklistener.
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
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        findTicketButton = view.findViewById(R.id.findTicketButton);

        scanTicketButton = view.findViewById(R.id.scanTicketButton);

        db = FirebaseFirestore.getInstance();
        findTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFindTicketButtonClick();
            }
        });

        scanTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                IntentIntegrator intentIntegrator = new IntentIntegrator(requireActivity());
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scan Ticket QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.forSupportFragment(ScanFragment.this).initiateScan();

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("scan", "in onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                // handle null result
                Toast.makeText(requireContext(), "Scan canceled", Toast.LENGTH_SHORT).show();
            } else {
                // Process the scanned data
                String ticketId = result.getContents();
                Log.d("scan", "got ticketId" + ticketId);

                //fetch the ticket from Firestore by ticketId
                TicketProvider provider = new TicketProvider();
                provider.fetchTicketById(ticketId, new TicketProvider.FetchTicketCallback() {
                    @Override
                    public void onTicketFetched(Ticket ticket) {
                        MainActivity mainActivity = (MainActivity) requireActivity();
                        String currentJourneyId = mainActivity.getCurrentJourneyId();
                        Log.d("ScanAc", "currentJourneyId = " + currentJourneyId);
                        Log.d("ScanAc", "Inputted ticket Id = " + ticketId + " with journId " + ticket.getJourneyId());

                        // Check that the journey ID on the ticket is this journey
                        if (ticket.getJourneyId().equals(currentJourneyId)) {
                            loadTicketFragment(ticketId);
                        } else {
                            // If not valid, display toast message
                            Toast.makeText(requireContext(), "Ticket invalid for this journey", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onTicketNotFound() {
                        Toast.makeText(requireContext(), "Ticket not found", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFetchFailed(String errorMessage) {
                        Toast.makeText(requireContext(), "Ticket fetch failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            // Call super method for other activity results
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * Handles manual ticket code entry button click.
     * Opens pop up text input dialog, retrieves input, checks validity.
     * If invalid, toast error message.
     * If valid, pass ticketId to loadTicketFragment
     */
    private void handleFindTicketButtonClick() {
        PopupTextInput popup = new PopupTextInput();
        String ticketId;

        // Show the popup and provide a callback for when the input is received
        popup.showPopupTextInput(requireContext(), "Enter Ticket Id", new PopupTextInput.InputCallback() {
            @Override
            public void onInput(String ticketId) {
                if (ticketId != null) {
                    // Input received, now fetch the ticket from db Tickets collection by ID
                    TicketProvider provider = new TicketProvider();
                    provider.fetchTicketById(ticketId, new TicketProvider.FetchTicketCallback() {
                        @Override
                        public void onTicketFetched(Ticket ticket) {
                            MainActivity mainActivity = (MainActivity) getActivity();
                            String currentJourneyId = mainActivity.getCurrentJourneyId();
                            Log.d("ScanAc", "currentJourneyId = " + currentJourneyId);
                            Log.d("ScanAc", "Inputted ticket Id = " + ticketId + " with journId " + ticket.getJourneyId());

                            //Check that the journey ID on the ticket is this journey
                            if (ticket.getJourneyId().equals(currentJourneyId)) {
                                loadTicketFragment(ticketId);
                            } else {
                                //if not valid, toast message
                                Toast.makeText(requireContext(), "Ticket invalid for this journey", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onTicketNotFound() {
                            Toast.makeText(requireContext(), "Ticket not found", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFetchFailed(String errorMessage) {
                            Toast.makeText(requireContext(), "Ticket fetch failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Handle the case where the user didn't provide a valid ticketId
                    Toast.makeText(requireContext(), "Invalid Ticket Id", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }



    /**
     * Opens new TicketFragment
     *
     * @param ticketId Id of the ticket just scanned
     */
    private void loadTicketFragment(String ticketId) { //opens ticket display fragment, passing in a valid ticket id
        // Create instance of TicketFragment and pass in ticket Id
        TicketFragment ticketFragment = TicketFragment.newInstance(ticketId);
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, ticketFragment);
        fragmentTransaction.commit();
    }
}