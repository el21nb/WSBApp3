package com.example.wsbapp3.fragments;

import com.example.wsbapp3.R;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Home Fragment, displays a menu of buttons which redirect to other fragments
 */
public class HomeFragment extends Fragment {

    //menu buttons:
    RelativeLayout routeButton;
    RelativeLayout scanButton;
    RelativeLayout passengersButton;
    RelativeLayout infoButton;
    RelativeLayout settingsButton;
    RelativeLayout signoutButton;

    //Heading textview:
    TextView hometext;

    public HomeFragment() {

        // Required empty public constructor
    }

    /**
     * Creates a new instance of the home fragmenr
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
     * Finds the buttons and sets onclick listener.
     * Sets welcome text, depending on time of day.
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        routeButton = view.findViewById(R.id.routebutton);
        scanButton = view.findViewById(R.id.scanbutton);
        passengersButton = view.findViewById(R.id.passengersbutton);
        infoButton = view.findViewById(R.id.infobutton);
        settingsButton = view.findViewById(R.id.settingsbutton);
        signoutButton = view.findViewById(R.id.signout);

        hometext = view.findViewById(R.id.hometext);
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        // Set the greeting based on the time
        if (hourOfDay >= 0 && hourOfDay < 12) {
            hometext.setText("Good Morning,\nWalking Bus Driver");
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            hometext.setText("Good Afternoon,\nWalking Bus Driver");
        } else {
            hometext.setText("Good Evening,\nWalking Bus Driver");
        }

        routeButton.setOnClickListener(this::onClick);
        scanButton.setOnClickListener(this::onClick);
        passengersButton.setOnClickListener(this::onClick);
        infoButton.setOnClickListener(this::onClick);
        settingsButton.setOnClickListener(this::onClick);
        signoutButton.setOnClickListener(this::onClick);

        return view;
    }

    /**
     * onClick method for the menu button. Opens new fragment depending on which button pressed.
     *
     * @param view
     */
    public void onClick(View view) {
        // Handle button clicks
        if (view.getId() == R.id.routebutton) {
            // Navigate to the route fragment
            navigateToFragment(new RouteFragment());
        } else if (view.getId() == R.id.scanbutton) {
            // Navigate to the scan fragment
            navigateToFragment(new ScanFragment());
        } else if (view.getId() == R.id.passengersbutton) {
            // Navigate to the passengers fragment
            navigateToFragment(new PassengersFragment());
        } else if (view.getId() == R.id.infobutton) {
            // Navigate to the info fragment
            navigateToFragment(new InfoFragment());
        } else if (view.getId() == R.id.settingsbutton) {
            // Navigate to the settings fragment
            navigateToFragment(new SettingsFragment());
        } else if (view.getId() == R.id.signout) {
            // Handle signout
        }
    }

    /**
     * Replaces the HomeFragment with a new fragment
     *
     * @param fragment The new fragment to be created
     */
    private void navigateToFragment(Fragment fragment) {
        // Perform fragment transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);  // Add to back stack for back navigation
        transaction.commit();
    }

}