package com.example.wsbapp3.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wsbapp3.database.ChildProvider;
import com.example.wsbapp3.models.Contact;
import com.example.wsbapp3.R;
import com.example.wsbapp3.models.Child;

import java.util.List;

/**
 * Fragment accepts a childId, fetches the child object from the database, and displays
 * the childs details.
 * Currently accessed from bus stop booking list (BusStopsFragment) and
 * onboard passenger list (PassengersFragment)
 * TODO: do the UI for this fragment
 */
public class ChildInfoFragment extends Fragment {

    // Input parameter: childId of the child to be displayed
    private static final String CHILD_ID = "childId";

    private String childId;

    //Textview to display all the child data
    TextView testText;

    public ChildInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of this fragment using the provided childId
     */
    //
    public static ChildInfoFragment newInstance(String childId) {
        ChildInfoFragment fragment = new ChildInfoFragment();
        Bundle args = new Bundle();
        args.putString(CHILD_ID, childId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * onCreate of fragment, fetch arguments
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            childId = getArguments().getString(CHILD_ID);
            Log.d("CIA", "ChildId =" + childId);
        }
    }

    /**
     * onCreate View:
     * Finds the textview.
     * Fetches the child object from the database by its id.
     * Populates a string with the child data.
     * Sets the textview to display the data.
     * TODO: update this doc when UI updated
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
        View v = inflater.inflate(R.layout.fragment_child_info, container, false);
        testText = v.findViewById(R.id.textview);
        ChildProvider provider = new ChildProvider();
        provider.fetchChildById(childId, new ChildProvider.FetchChildCallback() {
            @Override
            public void onChildFetched(Child child) {
                String testString;
                String contactsString = "\nEmergency Contacts:";
                List<Contact> contacts = child.getChildContacts();
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