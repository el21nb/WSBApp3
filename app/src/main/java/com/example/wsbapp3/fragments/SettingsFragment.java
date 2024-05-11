package com.example.wsbapp3.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wsbapp3.R;

/**
 * Generate a test warning
 */
public class SettingsFragment extends Fragment {

    AppCompatButton warningButton;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    /**
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
        View v =  inflater.inflate(R.layout.fragment_settings, container, false);
        warningButton = v.findViewById(R.id.warningButton);
        //Initialise views
        warningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onWarningButtonClick();
            }
        });
        return v;

    }

    /**
     * Generate a fake out-of range jacket warning
     */
    private void onWarningButtonClick() {
        WarningFragment warningFragment = WarningFragment.newInstance("JA05");
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, warningFragment)
                .addToBackStack(null)  // Don't add to backstack. This only accessible via warning notification
                .commit();

    }
}