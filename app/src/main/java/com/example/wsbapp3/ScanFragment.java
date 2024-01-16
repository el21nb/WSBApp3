package com.example.wsbapp3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment {
    private EditText editText;
    private FirebaseFirestore db;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanFragment newInstance(String param1, String param2) {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        Button findTicketButton = view.findViewById(R.id.findTicketButton);
        db = FirebaseFirestore.getInstance();
        findTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFindTicketButtonClick();
            }
        });

        return view;
    }


    private void handleFindTicketButtonClick() {
        PopupTextInput popup = new PopupTextInput();
        String ticketId;
        // Show the popup and provide a callback for when the input is received
        popup.showPopupTextInput(requireContext(), "Enter Ticket Id", new PopupTextInput.InputCallback() {
            @Override
            public void onInput(String ticketId) {
                if (ticketId != null) {
                    // Input received, now fetch the ticket
                    TicketProvider provider = new TicketProvider();
                    provider.fetchTicketById(ticketId, new TicketProvider.FetchTicketCallback() {
                        @Override
                        public void onTicketFetched(Ticket ticket) {
                            MainActivity mainActivity = (MainActivity) getActivity();
                            String currentJourneyId = mainActivity.getCurrentJourneyId();
                            Log.d("ScanAc", "currentJourneyId = "+ currentJourneyId);
                            Log.d("ScanAc", "Inputted ticket Id = " + ticketId + "with journId "+ ticket.getJourneyId());

                            if(ticket.getJourneyId().equals(currentJourneyId)) { //check ticket valid for current journey
                                loadTicketFragment(ticketId);
                            } else {
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

    private void loadTicketFragment(String ticketId) { //opens ticket display fragment, passing in a valid ticket id
        // Create an instance of TicketFragment and pass the ticket object as an argument
        TicketFragment ticketFragment = TicketFragment.newInstance(ticketId);

        // Get the FragmentManager and start a fragment transaction
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the TicketFragment
        fragmentTransaction.replace(R.id.frameLayout, ticketFragment);

        // Add the transaction to the back stack (optional)
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }
}