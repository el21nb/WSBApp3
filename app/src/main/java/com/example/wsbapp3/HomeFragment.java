package com.example.wsbapp3;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RelativeLayout routeButton;
    RelativeLayout scanButton;
    RelativeLayout passengersButton;
    RelativeLayout infoButton;
    RelativeLayout settingsButton;
    RelativeLayout signoutButton;

    TextView hometext;

    public HomeFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        routeButton = view.findViewById(R.id.routebutton);
        scanButton = view.findViewById(R.id.scanbutton);
        passengersButton = view.findViewById(R.id.passengersbutton);
        infoButton = view.findViewById(R.id.infobutton);
        settingsButton = view.findViewById(R.id.settingsbutton);
        signoutButton = view.findViewById(R.id.signout);

        hometext= view.findViewById(R.id.hometext);
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

        hometext= view.findViewById(R.id.hometext);

        return view;
    }

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

    private void navigateToFragment(Fragment fragment) {
        // Perform fragment transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);  // Add to back stack for back navigation
        transaction.commit();
    }
}