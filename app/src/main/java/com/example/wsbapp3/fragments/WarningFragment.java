package com.example.wsbapp3.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wsbapp3.R;
import com.example.wsbapp3.activities.MainActivity;
import com.example.wsbapp3.database.ChildProvider;
import com.example.wsbapp3.database.JourneyProvider;
import com.example.wsbapp3.models.Child;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * WarningFragment- accepts a jacketId, communicates which child is out of range, offers navigation options to
 * childInfoFragment, InfoFragment, or to return to previous fragment
 * TODO: update the UI.
 */
public class WarningFragment extends Fragment {

    //Parameter arguments, passed from AssignJacketFragment
    private static final String JACKET_ID = "jacketId";
    private String jacketId;

    private String currentJourneyId;

    private Child fragmentChild;

    private Vibrator vibrator;

    //ensure handler destroyed when fragment exited
    private static final Handler handler = new Handler();

    TextView testText;

    //button to dismiss warning
    Button dismissButton;

    //Button to go to info Fragment
    Button helpButton;

    //Button to go to ChildInfo Fragment
    Button childButton;

    public WarningFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param jacketId Parameter 1.
     * @return A new instance of fragment WarningFragment.
     */
    public static com.example.wsbapp3.fragments.WarningFragment newInstance(String jacketId) {
        com.example.wsbapp3.fragments.WarningFragment fragment = new com.example.wsbapp3.fragments.WarningFragment();
        Bundle args = new Bundle();
        args.putString(JACKET_ID, jacketId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Find the views and set onClickListeners for the buttons
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
        // Inflate the layout  and initialise views for this fragment
        View v = inflater.inflate(R.layout.fragment_warning, container, false);
        testText = v.findViewById(R.id.testText);
        dismissButton = v.findViewById(R.id.dismissButton);
        helpButton = v.findViewById(R.id.helpButton);
        childButton = v.findViewById(R.id.childButton);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        currentJourneyId = ((MainActivity) requireActivity()).getCurrentJourneyId();
        setOnClickListeners();
        fetchChild();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startBuzzing();

    }
    @Override
    public void onDetach() {
        super.onDetach();
        stopBuzzing();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopBuzzing();
    }
    @Override
    public void onStop() {
        super.onStop();
        stopBuzzing();
    }
    @Override
    public void onPause() {
        super.onPause();
        stopBuzzing();
    }

    /**
     * fetch Child from passenger document with assigned jacket jacketId
     */
    private void fetchChild() {
        if (getArguments() != null) {
            jacketId = getArguments().getString(JACKET_ID);

            {
                jacketId = getArguments().getString(JACKET_ID);
                ChildProvider cProvider = new ChildProvider();
                JourneyProvider jProvider = new JourneyProvider();
                jProvider.fetchPassengerByJacketId(jacketId, currentJourneyId, new JourneyProvider.FetchPassengerByJacketCallback() {
                    @Override
                    public void onPassengerByJacketFetched(Child child) {
                        fragmentChild = child;
                        childFound(child);
                    }
                    @Override
                    public void onPassengerByJacketNotFound() {
                        childNotFound();
                    }

                    @Override
                    public void onFetchFailed(String errorMessage) {
                        childNotFound();
                    }
                });
            }
        }
    }

    /**
     * Upon successful child fetch.
     * Write string.
     * Make childInfo button visible and set onclicklistener
     * @param child
     */
    private void childFound(Child child) {
        String testString =
                "Jacket with ID " + jacketId + "\nassigned to " +
                child.getFirstName() + " " + child.getLastName() +
                        " is out of range!\nPlease STOP the bus and ensure that "
                + child.getFirstName() + " is safe.";
        testText.setText(testString);
        childButton.setVisibility(View.VISIBLE);
        childButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChildButtonClick(child);
            }
        });
    }

    /**
     * Upon failed fetch.
     * Write String.
     * Make childInfo button invisible
     */
    private void childNotFound() {
        String testString =
                "Jacket with ID " + jacketId + " is out of range!\n Unable to identify passenger. \n Please STOP the bus and ensure that all passengers are safe.";
        testText.setText(testString);
        childButton.setVisibility(View.INVISIBLE);
    }

    /**
     * set onclick listeners for dismiss and help buttons
     */
    private void setOnClickListeners() {
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHelpButtonClick();
            }
        });
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDismissButtonClick();
            }
        });

    }

    /**
     * Handles help button click.
     * Replaces fragment with info fragment.
     */
    private void onHelpButtonClick() {
        InfoFragment infoFragment = InfoFragment.newInstance();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, infoFragment)
                //.addToBackStack(null)  // Don't add to backstack. This only accessible via warning notification
                .commit();
    }

    /**
     * Handles dismiss button click.
     * Goes back to previously open fragment
     */
    private void onDismissButtonClick() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        // Pop back stack to return to previous fragment
        fragmentManager.popBackStack();
    }


    /**
     * Handles child button click.
     * Passes childId to childInfoFragment
     */
    private void onChildButtonClick(Child child) {
        ChildInfoFragment childInfoFragment = ChildInfoFragment.newInstance(child.getId());
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, childInfoFragment)
                //.addToBackStack(null)  // Don't add to backstack. This only accessible via warning notification
                .commit();
    }

    /**
     * Causes periodic vibrations.
     * Handler dispatches periodic vibrate messages to the main thread
     * https://stackoverflow.com/questions/33113737/how-to-loop-a-dynamic-vibration-pattern-in-android
     */
    private void startBuzzing() {
        //call vibrator service
        vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
        //check if vibrator service is available
        if (vibrator == null || !vibrator.hasVibrator()) {
            return; //abandon method if no vibrator available
        }

        // handler repeatedly sends new Runnable object to main thread message queue, to call for a vibration
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // check for required SDK version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //Set vibrator to buzz for 0.7s
                    vibrator.vibrate(VibrationEffect.createOneShot(700, VibrationEffect.DEFAULT_AMPLITUDE));
                }

                // tell handler to wait for 1s after buzz
                handler.postDelayed(this, 1000);
            }
        }, 0); //no delay before starting runnable
    }

    /**
     * Stops vibrations by removing any handler messages and canceling the vinrator
     */
    private void stopBuzzing() {
        // Remove any pending vibration callbacks
        handler.removeCallbacksAndMessages(null);
        //check that vibrator exists before cancelling it
        if (vibrator.hasVibrator() && vibrator != null) {
            vibrator.cancel();
        }
    }
}