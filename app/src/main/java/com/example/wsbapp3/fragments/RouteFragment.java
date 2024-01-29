package com.example.wsbapp3.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wsbapp3.R;
import com.example.wsbapp3.fragments.BusStopsFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Displays a nested google maps MapFragment.
 * TODO: set up to display a route
 * Button to open BusStopsFragment.
 * Adapted from:
 * https://stackoverflow.com/questions/15433820/mapfragment-in-fragment-alternatives
 */
public class RouteFragment extends Fragment {

    //Button to open list of bus stops
    AppCompatButton busstopbutton;

    /**
     * Callback allows the map to be manipulated.
     */

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Callback is triggered when the map is available to be manipulated.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };

    /**
     * OnCreateView finds the views, sets up the MapFragment, and sets onClickListener for the
     * bus stop list button.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return the view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_route, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this.callback);
        }
        busstopbutton = v.findViewById(R.id.busstopbutton);
        busstopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusStopsFragment busStopsFragment = BusStopsFragment.newInstance();
                // Use FragmentManager to replace the current fragment with ScanFragment
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, busStopsFragment)
                        .addToBackStack(null)  // Add to back stack so the user can navigate back
                        .commit();
            }
        });
        return v;
    }

    /**
     * Sets up nested MapFragment
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}