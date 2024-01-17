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
 * Use the {@link AssignJacketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignJacketFragment extends Fragment {
    private EditText editText;
    private FirebaseFirestore db;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TICKET_ID = "ticketId";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public AssignJacketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param jacketId Parameter 1.
     * @return A new instance of fragment AssignJacketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssignJacketFragment newInstance(String jacketId) {
        AssignJacketFragment fragment = new AssignJacketFragment();
        Bundle args = new Bundle();
        args.putString(TICKET_ID, jacketId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(TICKET_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assign_jacket, container, false);
        Button findJacketButton = view.findViewById(R.id.findJacketButton);
        db = FirebaseFirestore.getInstance();
        findJacketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFindJacketButtonClick();
            }
        });

        return view;
    }


 private void handleFindJacketButtonClick() {
        PopupTextInput popup = new PopupTextInput();
        String jacketId;
        // Show the popup and provide a callback for when the input is received
        popup.showPopupTextInput(requireContext(), "Enter Jacket Id", new PopupTextInput.InputCallback() {
            @Override
            public void onInput(String jacketId) {
                if (jacketId != null) {
                    // Input received, now fetch the jacket
                    JacketProvider provider = new JacketProvider();
                    provider.fetchJacketById(jacketId, new JacketProvider.FetchJacketCallback() {
                        @Override
                        public void onJacketFetched(Jacket jacket) {
                            String ticketId = getArguments().getString(TICKET_ID);
                            loadScanAgainFragment(jacketId,  ticketId);
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

    private void loadScanAgainFragment(String jacketId, String ticketId) {
        // Create an instance of JacketFragment and pass the jacket object as an argument
        ScanAgainFragment scanAgainFragment = ScanAgainFragment.newInstance(jacketId,ticketId);

        // Get the FragmentManager and start a fragment transaction
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the JacketFragment
        fragmentTransaction.replace(R.id.frameLayout, scanAgainFragment);

        // Add the transaction to the back stack (optional)
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }
}