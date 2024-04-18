package com.example.wsbapp3.fragments;

import static android.app.ProgressDialog.show;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.wsbapp3.R;
import com.example.wsbapp3.adapters.InfoAdapter;
import com.example.wsbapp3.adapters.UpdatesAdapter;
import com.example.wsbapp3.models.UpdatesListItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdatesFragment#} factory method to
 * create an instance of this fragment.
 */
public class UpdatesFragment extends Fragment {

    private EditText titleEditText;
    private EditText statusEditText;
    private RadioGroup visibilityRadioGroup;

    private RadioButton parentsRadioButton, publicRadioButton, staffRadioButton;
    private Button postButton;

    private RecyclerView RVparent;

    UpdatesAdapter updatesAdapter;

    // Firestore
    private FirebaseFirestore firestore;

    public UpdatesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_updates, container, false);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize Views
        titleEditText = view.findViewById(R.id.titleEditText);
        statusEditText = view.findViewById(R.id.statusEditText);
        visibilityRadioGroup = view.findViewById(R.id.visibilityRadioGroup);
        postButton = view.findViewById(R.id.postButton);
        parentsRadioButton = view.findViewById(R.id.parentsRadioButton);
        publicRadioButton = view.findViewById(R.id.publicRadioButton);
        staffRadioButton = view.findViewById(R.id.staffRadioButton);
        RVparent =view.findViewById(R.id.RVparent);
        int selectedColor = getResources().getColor(R.color.darkBlue);
        ColorStateList colorStateList = ColorStateList.valueOf(selectedColor);
        parentsRadioButton.setButtonTintList(colorStateList);
        publicRadioButton.setButtonTintList(colorStateList);
        staffRadioButton.setButtonTintList(colorStateList);

        // Set click listener for post button

        fetchUpdates();
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postStatus();
            }
        });

        return view;
    }

    private void postStatus() {

        String title = titleEditText.getText().toString();
        String status = statusEditText.getText().toString();
        int selectedVisibilityId = visibilityRadioGroup.getCheckedRadioButtonId();

        // check if all fields filled
        if (title.isEmpty() || status.isEmpty() || selectedVisibilityId == -1) {
            Toast.makeText(requireContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();

            return;
        }

        // get visibility
        RadioButton selectedRadioButton = getView().findViewById(selectedVisibilityId);
        String visibility = selectedRadioButton.getText().toString();

        // Create a Map to store status data
        Map<String, Object> statusData = new HashMap<>();
        statusData.put("title", title);
        statusData.put("status", status);
        statusData.put("visibility", visibility);
        statusData.put("datetime", FieldValue.serverTimestamp());

        // Add the status to Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Statuses")
                .add(statusData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(requireContext(), "Update sent!", Toast.LENGTH_SHORT).show();
                        fetchUpdates();
                        titleEditText.setText("");
                        statusEditText.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Update not sent, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void fetchUpdates(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Statuses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<UpdatesListItem> updatesList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title") + "\nPosted: "+ document.getTimestamp("datetime").toDate().toString();
                                String message = document.getString("status");
                                // You need to adjust this according to your data structure in Firestore
                                // Construct a new UpdatesListItem object and add it to the list
                                UpdatesListItem updatesListItem = new UpdatesListItem(title, message);
                                updatesList.add(updatesListItem);

                            }
                            // Pass the updatesList to the adapter
                            updatesAdapter = new UpdatesAdapter(requireActivity(), requireActivity().getSupportFragmentManager(), updatesList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
                            RVparent.setLayoutManager(linearLayoutManager);
                            RVparent.setAdapter(updatesAdapter);
                        } else {
                            Toast.makeText(requireContext(), "Error fetching statuses", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
