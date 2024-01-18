package com.example.wsbapp3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChildInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChildInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CHILD_ID = "childId";

    // TODO: Rename and change types of parameters
    private String childId;
    TextView testText;

    public ChildInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChildInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChildInfoFragment newInstance(String childId) {
        ChildInfoFragment fragment = new ChildInfoFragment();
        Bundle args = new Bundle();
        args.putString(CHILD_ID, childId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            childId = getArguments().getString(CHILD_ID);
            Log.d("CIA", "ChildId =" + childId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_child_info, container, false);
        testText = v.findViewById(R.id.textview);
        ChildProvider provider = new ChildProvider();
        provider.fetchChildById(childId, new ChildProvider.FetchChildCallback() {
            @Override
            public void onChildFetched(Child child) {
                String testString;
                String contactsString = "\nEmergency Contacts:";
                List<Contact> contacts= child.getChildContacts();
                for (Contact contact : contacts) {
                    contactsString += "\nContact Name: " + contact.getName();
                    contactsString += "\nContact Detail: " + contact.getContactDetail();
                }
                testString = "CHILD INFO\nName: " + child.getFirstName() + " " + child.getLastName()
                        + "\nID: " + child.getId()
                        + "\nClass Code: " + child.getClassCode()
                        + contactsString;
                testText.setText(testString);
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


        return v;
    }
}